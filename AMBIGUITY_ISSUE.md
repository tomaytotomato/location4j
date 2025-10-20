# Location Ambiguity Resolution Issue

**Issue Reference:** [GitHub Issue #45](https://github.com/tomaytotomato/location4j/issues/45)  
**Status:** Open  
**Priority:** Medium  
**Affected Version:** 1.0.6+

---

## Executive Summary

The location4j library successfully handles 95% of geographical search scenarios. However, the current implementation struggles with **name ambiguity** when location names overlap across different geographical levels (country, state, city). This leads to incorrect or suboptimal search results in edge cases where names collide.

---

## Technical Background

### What is location4j?

**location4j** is a high-performance Java 21+ library for geographical data lookups. It operates entirely offline using an embedded binary dataset (`location4j.bin`), eliminating the need for external API calls.

**Key Features:**
- Free-text search for countries, states, and cities
- Multiple lookup methods (by ID, name, ISO codes, lat/long)
- Text normalization and tokenization
- Zero external dependencies for runtime lookups
- Uses Java Platform Module System (JPMS)

**Architecture:**
```
User Input → Text Normalizer → Text Tokenizer → Search Logic → Scoring → Results
                                                       ↓
                                            Pre-built Hash Maps
                                    (Country/State/City lookups)
```

---

## The Problem

### Concrete Examples of Ambiguity

The library fails to correctly disambiguate when multiple geographical entities share the same name:

#### 1. **New York, NY** (State vs. City)
- **New York** is both a **state** (New York State) and a **city** (New York City)
- The city "New York" is located in the state "New York"
- User query: `"New York, NY"` should return **New York City**, not just New York State

#### 2. **Mexico** (Country vs. City)
- **Mexico** is both a **country** and a **city** (Mexico City, the capital)
- User query: `"Mexico"` without context is ambiguous
- Query: `"Mexico City"` should clearly return the city, not the country

#### 3. **New York in Alabama**
- There's a small city called **"New York"** in **Alabama**
- Query: `"New York, NY"` should NOT return this city
- The state code "NY" should reinforce that we mean New York State, not Alabama

---

## Current Implementation Issues

### How the Current Algorithm Works

1. **Tokenization:** Input text is split into tokens (e.g., "New York NY" → ["New York", "York NY", "New", "York", "NY"])
2. **Direct Match Attempt:** Checks for exact matches in country/state/city maps
3. **Token-Based Matching:** Iterates through tokens, finding all possible matches
4. **Hit Counting:** Counts matches for each country, state, and city
5. **Composite Scoring:** Calculates scores based on hierarchical reinforcement:
   - City score = city hits + state hits + country hits
   - State score = state hits + country hits
6. **Result Selection:** Returns the single highest-scoring result

### Why It Fails

The current implementation has several limitations:

1. **No Cross-Validation:** When a city name matches, it doesn't validate whether other tokens (like state codes) agree or contradict the match
   
2. **Simple Additive Scoring:** The scoring system doesn't penalize contradictions. For example:
   - "New York" (city) in Alabama gets points
   - "NY" (state code for New York) also gets points
   - These should contradict each other, but the algorithm treats them as independent hits

3. **Early Termination:** In some code paths, the search stops after the first match, preventing full disambiguation

4. **No Popularity/Frequency Weighting:** Real-world usage shows that "New York" almost always refers to New York City (one of the world's largest cities), but the algorithm treats it equally with "New York, Alabama" (a small town)

---

## Proposed Solution

### Enhanced Token-Based Matching with Scoring

The recommended approach is to **rewrite the matching logic** to find all matching combinations and score them retrospectively with cross-validation.

### Algorithm Pseudocode

```
Input: "New York NY"

1. TOKENIZATION
   tokens = ["New York", "York NY", "New", "York", "NY"]

2. MATCH COLLECTION (Find ALL possibilities, don't filter yet)
   For each token:
     - Check against city names
     - Check against state names and codes
     - Check against country names and codes
   
   Possible matches:
     - "New York" → City: New York, NY, USA (score: 1)
     - "New York" → City: New York, AL, USA (score: 1)
     - "New York" → State: New York, USA (score: 1)
     - "York" → City: York, PA, USA (score: 1)
     - "NY" → State: New York, USA (score: 1)

3. CROSS-VALIDATION & SCORING
   For each match, evaluate consistency with other tokens:
   
   Match: New York City (NY, USA)
     ✓ "NY" matches state code → +2 (strong reinforcement)
     ✓ City is in NY state → +1 (hierarchical consistency)
     Final Score: 4
   
   Match: New York City (AL, USA)
     ✗ "NY" does NOT match Alabama → -2 (contradiction penalty)
     ✗ City is not in NY state → -1 (hierarchical inconsistency)
     Final Score: -2
   
   Match: New York State (USA)
     ✓ "NY" matches state code → +2
     ~ No city match → 0
     Final Score: 3
   
   Match: York City (PA, USA)
     ~ "New York" is different from "York" → 0 (partial match)
     ✗ "NY" does not match PA → -1
     Final Score: 0

4. SORT BY SCORE
   Results:
     1. New York City, NY, USA (score: 4) ← BEST MATCH
     2. New York State, USA (score: 3)
     3. York, PA, USA (score: 0)
     4. New York, AL, USA (score: -2)

5. RETURN TOP RESULT(S)
   → New York City, New York, USA
```

### Scoring Rules

| Condition | Score Modifier | Rationale |
|-----------|----------------|-----------|
| Exact token match (city/state/country name) | +1 | Basic match found |
| State code matches city's state | +2 | Strong geographical reinforcement |
| Country code matches | +2 | Strong geographical reinforcement |
| State name matches city's state | +1 | Hierarchical consistency |
| Token contradicts geography (e.g., wrong state code) | -2 | Penalize inconsistencies |
| Partial token match (substring) | 0 or -1 | Neutral or slight penalty |
| Population weight (optional) | +0 to +1 | Favor larger cities in tie-breakers |

### Implementation Considerations

1. **Maintain All Candidates:** Don't eliminate matches early—collect all possibilities first
2. **Add Contradiction Detection:** Compare state codes, country codes across matches
3. **Implement Hierarchical Validation:** Verify city-state-country relationships
4. **Consider Population Data:** Optionally weight results by city population for tie-breaking
5. **Return Multiple Results:** For true ambiguities, consider returning top N matches with confidence scores

---

## Alternative Solutions (Not Recommended)

### 1. Add an "Ambiguous" Flag
- **Idea:** Flag certain location names as ambiguous to trigger special logic
- **Problems:**
  - Manual maintenance required as data changes
  - Doesn't solve the scoring problem, just identifies it
  - Binary flag doesn't capture degrees of ambiguity

### 2. Use Popularity/Frequency Rankings
- **Idea:** Rank results by population or search frequency
- **Problems:**
  - Requires external data source (population statistics)
  - Doesn't help when user provides specific context (e.g., "NY" clearly means New York State)
  - May override user intent in some cases

---

## Test Cases

The following test cases should pass after implementation:

```java
@Test
void testNewYorkDisambiguation() {
    // Should return New York City, not state or Alabama city
    var result = searchLocationService.search("New York, NY");
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isInstanceOf(CityResult.class);
    assertThat(result.get(0).name()).isEqualTo("New York City");
    assertThat(result.get(0).getState().code()).isEqualTo("NY");
}

@Test
void testMexicoDisambiguation() {
    // "Mexico" alone is ambiguous, should prefer country
    var result = searchLocationService.search("Mexico");
    assertThat(result.get(0)).isInstanceOf(CountryResult.class);
    
    // "Mexico City" is specific, should return city
    var result2 = searchLocationService.search("Mexico City");
    assertThat(result2.get(0)).isInstanceOf(CityResult.class);
}

@Test
void testStateCodeReinforcement() {
    // "New York, AL" should prefer the Alabama city despite New York being more famous
    var result = searchLocationService.search("New York, AL");
    assertThat(result.get(0).getState().code()).isEqualTo("AL");
}
```

---

## Impact Assessment

### Current Behavior
- ✅ Works perfectly for unambiguous locations (95% of cases)
- ⚠️ May return wrong results for ambiguous names
- ⚠️ Test cases in `SearchLocationServiceTest.java` are currently **disabled** (see line 206)
- ⚠️ Known issue tracked with `@Disabled` annotation

### Expected After Fix
- ✅ Correct disambiguation using contextual clues
- ✅ Better scoring for hierarchical relationships
- ✅ Re-enable disabled test cases
- ✅ More predictable search results

### Backward Compatibility
- ⚠️ **Minor breaking change possible:** Some edge cases that previously returned incorrect results will now return different (correct) results
- ✅ API signature remains unchanged
- ✅ Non-ambiguous cases unaffected

---

## Implementation Checklist

- [x] Refactor `findTokenizedMatches()` method in `SearchLocationService`
- [x] Implement match collection phase (find all candidates)
- [x] Implement cross-validation scoring logic
- [x] Add contradiction detection (state code vs. actual state)
- [x] Add hierarchical consistency checks
- [x] Update scoring algorithm with new rules
- [x] Add unit tests for ambiguous cases
- [x] Re-enable disabled tests in `SearchLocationServiceTest`
- [ ] Update `AmbiguityEnumerationTest` to document resolved cases
- [ ] Performance testing (ensure no significant regression)
- [ ] Documentation updates

---

## Related Files

- **Main Implementation:** `/location4j/src/main/java/com/tomaytotomato/location4j/usecase/search/SearchLocationService.java`
- **Test Cases:** `/location4j/src/test/java/com/tomaytotomato/location4j/usecase/search/SearchLocationServiceTest.java` (lines 206-237)
- **Ambiguity Tests:** `/location4j/src/test/java/com/tomaytotomato/location4j/debug/AmbiguityEnumerationTest.java`
- **Tokenizers:** 
  - `/location4j/src/main/java/com/tomaytotomato/location4j/text/tokeniser/DefaultTextTokeniser.java`
  - `/location4j/src/main/java/com/tomaytotomato/location4j/text/tokeniser/PrefixAwareTextTokeniser.java`

---

## Notes

This issue has been partially addressed in the current codebase (see comments in tests mentioning "Core ambiguity issue fixed"), but edge cases remain. The proposed solution provides a comprehensive approach to handle all ambiguity scenarios systematically.

**Created:** October 15, 2025  
**Last Updated:** October 15, 2025
