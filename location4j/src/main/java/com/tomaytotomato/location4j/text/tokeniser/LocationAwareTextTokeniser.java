package com.tomaytotomato.location4j.text.tokeniser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

/**
 * Location-aware text tokeniser that creates composite tokens for known location patterns.
 * This tokeniser improves upon the default by prioritizing complete location names and
 * creating smarter token combinations for better location matching.
 */
public class LocationAwareTextTokeniser implements TextTokeniser {

  // Known compound location patterns that should be kept together
  private static final Set<String> COMPOUND_PATTERNS = Set.of(
      "new york", "san francisco", "los angeles", "las vegas", "new orleans",
      "mexico city", "rio de janeiro", "cape town", "buenos aires", "sao paulo",
      "hong kong", "kuala lumpur", "saint petersburg", "tel aviv"
  );
  
  // State/country codes that should be treated as units
  private static final Set<String> LOCATION_CODES = Set.of(
      "usa", "us", "uk", "can", "au", "deu", "fr", "it", "es", "br", "mx", "jp", "cn",
      "ny", "ca", "tx", "fl", "il", "pa", "oh", "ga", "nc", "mi", "nj", "va", "wa",
      "az", "ma", "tn", "in", "mo", "md", "wi", "co", "mn", "sc", "al", "la", "ky",
      "or", "ok", "ct", "ut", "ia", "nv", "ar", "ms", "ks", "nm", "ne", "wv", "id",
      "nh", "me", "hi", "ri", "mt", "del", "sd", "nd", "ak", "vt", "wy"
  );

  @Override
  public List<String> tokenise(String text) {
    if (Objects.isNull(text) || text.isEmpty()) {
      return new ArrayList<>();
    }

    List<String> tokens = new ArrayList<>();
    text = text.replaceAll("[^\\p{L}\\p{N}\\s\\-'.]", " ").trim().toLowerCase();

    String[] parts = text.split("\\s+");
    
    // Step 1: Add complete phrases that match known compound patterns
    addCompoundTokens(tokens, parts);
    
    // Step 2: Add smart pairs (avoiding overlaps with compound tokens)
    addSmartPairs(tokens, parts);
    
    // Step 3: Add individual words, but prioritize meaningful ones
    addIndividualTokens(tokens, parts);
    
    return tokens;
  }
  
  private void addCompoundTokens(List<String> tokens, String[] parts) {
    String fullText = String.join(" ", parts);
    
    // Check for known compound patterns
    for (String pattern : COMPOUND_PATTERNS) {
      if (fullText.contains(pattern)) {
        tokens.add(pattern);
      }
    }
    
    // Check for 3-word combinations that might be compound cities
    for (int i = 0; i <= parts.length - 3; i++) {
      String threeWord = parts[i] + " " + parts[i + 1] + " " + parts[i + 2];
      // Add three-word combinations for potential compound names
      tokens.add(threeWord);
    }
  }
  
  private void addSmartPairs(List<String> tokens, String[] parts) {
    for (int i = 0; i < parts.length - 1; i++) {
      String pair = parts[i] + " " + parts[i + 1];
      
      // Skip if this pair is part of a compound pattern we already added
      boolean partOfCompound = false;
      for (String pattern : COMPOUND_PATTERNS) {
        if (pattern.contains(pair)) {
          partOfCompound = true;
          break;
        }
      }
      
      if (!partOfCompound) {
        tokens.add(pair);
      }
    }
  }
  
  private void addIndividualTokens(List<String> tokens, String[] parts) {
    for (String part : parts) {
      // Prioritize location codes and meaningful words
      if (LOCATION_CODES.contains(part.toLowerCase()) || part.length() > 2) {
        tokens.add(part);
      }
    }
    
    // Add all parts at the end for completeness, but they'll have lower priority
    // in matching due to order
    tokens.addAll(Arrays.asList(parts));
  }
}