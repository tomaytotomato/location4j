package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.aliases.LocationAliases;
import com.tomaytotomato.location4j.loader.CountriesDataLoader;
import com.tomaytotomato.location4j.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.location4j.mapper.DefaultSearchLocationResultMapper;
import com.tomaytotomato.location4j.mapper.SearchLocationResultMapper;
import com.tomaytotomato.location4j.model.Location4JData;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.search.SearchLocationResult;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.location4j.text.tokeniser.TextTokeniser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Provides search functionality to find a Country, State or City based on text input
 */
public class SearchLocationService implements SearchLocation {

  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final List<Country> countries;
  private final Map<Integer, Country> countryIdToCountryMap;
  private final Map<String, Country> countryNameToCountryMap;
  private final Map<String, Country> iso2CodeToCountryMap;
  private final Map<String, Country> iso3CodeToCountryMap;
  private final Map<Integer, State> stateIdToStateMap;
  private final Map<String, List<State>> stateNameToStatesMap;
  private final Map<String, List<State>> stateCodeToStatesMap;
  private final Map<String, List<City>> cityNameToCitiesMap;

  private final TextTokeniser textTokeniser;
  private final TextNormaliser textNormaliser;
  private final SearchLocationResultMapper searchLocationResultMapper;
  private final LocationAliases locationAliases;

  protected SearchLocationService(TextTokeniser textTokeniser, TextNormaliser textNormaliser,
      SearchLocationResultMapper searchLocationResultMapper, CountriesDataLoader dataLoader,
      LocationAliases locationAliases) {
    this.textTokeniser = textTokeniser;
    this.textNormaliser = textNormaliser;
    this.searchLocationResultMapper = searchLocationResultMapper;
    this.locationAliases = locationAliases;

    // Load pre-built data structures
    Location4JData location4JData = dataLoader.getLocation4JData();
    this.countries = location4JData.getCountries();
    this.countryIdToCountryMap = location4JData.getCountryIdToCountryMap();
    this.countryNameToCountryMap = new HashMap<>(location4JData.getCountryNameToCountryMap());
    this.iso2CodeToCountryMap = new HashMap<>(location4JData.getIso2CodeToCountryMap());
    this.iso3CodeToCountryMap = new HashMap<>(location4JData.getIso3CodeToCountryMap());
    this.stateIdToStateMap = location4JData.getStateIdToStateMap();
    this.stateNameToStatesMap = new HashMap<>(location4JData.getStateNameToStatesMap());
    this.stateCodeToStatesMap = new HashMap<>(location4JData.getStateCodeToStatesMap());
    this.cityNameToCitiesMap = new HashMap<>(location4JData.getSearchCityNameToCitiesMap());

    // Add custom aliases on top of pre-built data structures
    addAliases();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builds a location result based on the found entities.
   */
  private SearchLocationResult buildLocationResult(Country topCountry, State topState, City topCity) {
    if (topCity != null) {
      return searchLocationResultMapper.toCityResult(topCity);
    } else if (topState != null) {
      return searchLocationResultMapper.toStateResult(topState);
    } else {
      return searchLocationResultMapper.toCountryResult(topCountry);
    }
  }

  /**
   * Deprecated: Data structures are now pre-built. Only aliases are added at runtime.
   * @deprecated Use constructor - data structures are pre-built during serialization
   */
  public void buildDataStructures() {
    addAliases();
  }

  /**
   * Adds custom aliases for lookups for country, state and city..
   */
  private void addAliases() {

    logger.info("Adding aliases for location lookups");

    locationAliases.getCountryNameAliases().forEach((alias, originalKey) -> {
      var country = countryNameToCountryMap.get(keyMaker(originalKey));
      countryNameToCountryMap.put(keyMaker(alias), country);
    });

    locationAliases.getCountryIso2Aliases().forEach((alias, originalKey) -> {
      var country = iso2CodeToCountryMap.get(keyMaker(originalKey));
      countryNameToCountryMap.put(keyMaker(alias), country);
    });

    locationAliases.getCountryIso3Aliases().forEach((alias, originalKey) -> {
      var country = iso3CodeToCountryMap.get(keyMaker(originalKey));
      countryNameToCountryMap.put(keyMaker(alias), country);
    });

    locationAliases.getStateNameAliases().forEach((alias, originalKey) -> {
      var states = stateNameToStatesMap.get(keyMaker(originalKey));
      stateNameToStatesMap.put(alias, states);
    });

    locationAliases.getCityNameAliases().forEach((alias, originalKey) -> {
      var cities = cityNameToCitiesMap.get(keyMaker(originalKey));
      cityNameToCitiesMap.put(alias, cities);
    });
  }


  /**
   * Normalizes a key for consistent lookup.
   *
   * @param key The key to be normalized.
   * @return The normalized key.
   */
  private String keyMaker(String key) {
    if (Objects.isNull(key) || key.isEmpty()) {
      throw new IllegalArgumentException("Key cannot be null or empty");
    }
    return textNormaliser.normalise(key);
  }

  @Override
  public List<SearchLocationResult> search(String text) {
    if (Objects.isNull(text) || text.isEmpty()) {
      throw new IllegalArgumentException("SearchLocation Text cannot be null or empty");
    } else if (text.length() < 2) {
      return List.of();
    }

    text = textNormaliser.normalise(text);

    // Direct matches
    List<SearchLocationResult> directMatches = findDirectMatches(text);
    if (!directMatches.isEmpty()) {
      return directMatches;
    }

    // Tokenized search
    return findTokenizedMatches(textTokeniser.tokenise(text));
  }

  /**
   * Finds direct matches by looking up the text against various lookup maps
   *
   * @param text The normalized search text.
   * @return A list of matching locations.
   */
  private List<SearchLocationResult> findDirectMatches(String text) {
    List<SearchLocationResult> matches = new ArrayList<>();

    if (countryNameToCountryMap.containsKey(text)) {
      matches.add(searchLocationResultMapper.toCountryResult(countryNameToCountryMap.get(text)));
      return matches;
    }

    if (text.length() == 3 && iso3CodeToCountryMap.containsKey(text)) {
      matches.add(searchLocationResultMapper.toCountryResult(iso3CodeToCountryMap.get(text)));
      return matches;
    }

    if (text.length() == 2) {
      if (iso2CodeToCountryMap.containsKey(text)) {
        matches.add(searchLocationResultMapper.toCountryResult(iso2CodeToCountryMap.get(text)));
      }
      if (stateCodeToStatesMap.containsKey(text)) {
        stateCodeToStatesMap.get(text)
            .forEach(state -> matches.add(searchLocationResultMapper.toStateResult(state)));
      }
      return matches;
    }

    if (stateNameToStatesMap.containsKey(text)) {
      stateNameToStatesMap.get(text)
          .forEach(state -> matches.add(searchLocationResultMapper.toStateResult(state)));
      return matches;
    }

    if (cityNameToCitiesMap.containsKey(text)) {
      cityNameToCitiesMap.get(text).forEach(city -> matches.add(searchLocationResultMapper.toCityResult(city)));
      return matches;
    }

    return matches;
  }

  /**
   * Finds matches for the given tokenized text.
   *
   * @param tokenizedText The tokenized search text.
   * @return A list of matching locations.
   */
  private List<SearchLocationResult> findTokenizedMatches(List<String> tokenizedText) {
    Map<Country, Integer> countryHitsCount = new HashMap<>();
    Map<State, Integer> stateHitsCount = new HashMap<>();
    Map<City, Integer> cityHitsCount = new HashMap<>();

    boolean countryFound = populateCountryHits(tokenizedText, countryHitsCount);

    if (!countryFound) {
      populateStateAndCityHits(tokenizedText, countryHitsCount, stateHitsCount, cityHitsCount);
    } else {
      filterAndPopulateStateAndCityHits(tokenizedText, countryHitsCount, stateHitsCount,
          cityHitsCount);
    }

    return getTopMatchingLocations(countryHitsCount, stateHitsCount, cityHitsCount);
  }

  /**
   * Populates country hit counts based on tokenized text.
   *
   * @param tokenizedText    The tokenized search text.
   * @param countryHitsCount The map to populate with country hits.
   * @return true if a country is found, false otherwise.
   */
  private boolean populateCountryHits(List<String> tokenizedText,
      Map<Country, Integer> countryHitsCount) {
    Iterator<String> iterator = tokenizedText.iterator();

    while (iterator.hasNext()) {
      String token = iterator.next();
      if (countryNameToCountryMap.containsKey(token)) {
        var country = countryNameToCountryMap.get(token);
        countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        iterator.remove();
        return true;
      }
      if (iso3CodeToCountryMap.containsKey(token)) {
        var country = iso3CodeToCountryMap.get(token);
        countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  /**
   * Populates state and city hit counts based on tokenized text without filtering.
   * All tokens are processed to allow for disambiguation - no early termination.
   *
   * @param tokenizedText    The tokenized search text.
   * @param countryHitsCount The map to populate with country hits.
   * @param stateHitsCount   The map to populate with state hits.
   * @param cityHitsCount    The map to populate with city hits.
   */
  private void populateStateAndCityHits(List<String> tokenizedText,
      Map<Country, Integer> countryHitsCount,
      Map<State, Integer> stateHitsCount,
      Map<City, Integer> cityHitsCount) {

    for (String token : tokenizedText) {
      // Check for state matches by name
      if (stateNameToStatesMap.containsKey(token)) {
        stateNameToStatesMap.get(token).forEach(state -> {
          var country = countryIdToCountryMap.get(state.getCountry().getId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
      }
      
      // Check for state matches by code
      if (stateCodeToStatesMap.containsKey(token)) {
        stateCodeToStatesMap.get(token).forEach(state -> {
          var country = countryIdToCountryMap.get(state.getCountry().getId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
      }
      
      // Check for city matches by name
      if (cityNameToCitiesMap.containsKey(token)) {
        cityNameToCitiesMap.get(token).forEach(city -> {
          var country = countryIdToCountryMap.get(city.getCountry().getId());
          var state = stateIdToStateMap.get(city.getState().getId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          cityHitsCount.put(city, cityHitsCount.getOrDefault(city, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
      }
    }
  }

  /**
   * Filters and populates state and city hit counts based on tokenized text.
   *
   * @param tokenizedText    The tokenized search text.
   * @param countryHitsCount The map to populate with country hits.
   * @param stateHitsCount   The map to populate with state hits.
   * @param cityHitsCount    The map to populate with city hits.
   */
  private void filterAndPopulateStateAndCityHits(List<String> tokenizedText,
      Map<Country, Integer> countryHitsCount,
      Map<State, Integer> stateHitsCount,
      Map<City, Integer> cityHitsCount) {
    var topCountry = getTopCountry(countryHitsCount);
    var stateFound = false;
    var cityFound = false;

    for (String token : tokenizedText) {
      if (stateNameToStatesMap.containsKey(token) && !stateFound) {
        stateNameToStatesMap.get(token).stream()
            .filter(state -> state.getCountry().getName().equals(topCountry.getName()))
            .forEach(state -> stateHitsCount.put(state,
                stateHitsCount.getOrDefault(state, 0) + 1));
        stateFound = true;
      }
      if (stateCodeToStatesMap.containsKey(token) && !stateFound) {
        stateCodeToStatesMap.get(token).stream()
            .filter(state -> state.getCountry().getName().equals(topCountry.getName()))
            .forEach(state -> stateHitsCount.put(state,
                stateHitsCount.getOrDefault(state, 0) + 1));
        stateFound = true;
      }
      if (cityNameToCitiesMap.containsKey(token) && !cityFound) {
        cityNameToCitiesMap.get(token).stream()
            .filter(city -> city.getCountry().getName().equals(topCountry.getName()))
            .forEach(city -> {
              var state = stateIdToStateMap.get(city.getState().getId());
              cityHitsCount.put(city, cityHitsCount.getOrDefault(city, 0) + 1);
              stateHitsCount.put(state,
                  stateHitsCount.getOrDefault(state, 0) + 1);
            });
        cityFound = true;
      }
    }
  }

  /**
   * Retrieves the top matching locations based on hit counts.
   * Uses improved scoring that considers hierarchical reinforcement.
   *
   * @param countryHitsCount The map of country hits.
   * @param stateHitsCount   The map of state hits.
   * @param cityHitsCount    The map of city hits.
   * @return A list of top matching locations.
   */
  private List<SearchLocationResult> getTopMatchingLocations(Map<Country, Integer> countryHitsCount,
      Map<State, Integer> stateHitsCount,
      Map<City, Integer> cityHitsCount) {

    // Calculate composite scores for cities that consider hierarchical reinforcement
    City bestCity = null;
    int bestCityScore = 0;
    
    for (Map.Entry<City, Integer> cityEntry : cityHitsCount.entrySet()) {
      City city = cityEntry.getKey();
      int cityHits = cityEntry.getValue();
      
      // Get hits for the city's state and country
      State cityState = stateIdToStateMap.get(city.getState().getId());
      Country cityCountry = countryIdToCountryMap.get(city.getCountry().getId());
      
      int stateHits = stateHitsCount.getOrDefault(cityState, 0);
      int countryHits = countryHitsCount.getOrDefault(cityCountry, 0);
      
      // Calculate composite score: city hits + state hits + country hits
      // This favors cities where the state/country also have matches (hierarchical reinforcement)
      int compositeScore = cityHits + stateHits + countryHits;
      
      if (compositeScore > bestCityScore) {
        bestCityScore = compositeScore;
        bestCity = city;
      }
    }
    
    // Find best state (with hierarchical reinforcement)
    State bestState = null;
    int bestStateScore = 0;
    
    for (Map.Entry<State, Integer> stateEntry : stateHitsCount.entrySet()) {
      State state = stateEntry.getKey();
      int stateHits = stateEntry.getValue();
      
      Country stateCountry = countryIdToCountryMap.get(state.getCountry().getId());
      int countryHits = countryHitsCount.getOrDefault(stateCountry, 0);
      
      // Calculate composite score: state hits + country hits
      int compositeScore = stateHits + countryHits;
      
      if (compositeScore > bestStateScore) {
        bestStateScore = compositeScore;
        bestState = state;
      }
    }

    // Find best country
    Country bestCountry = getTopCountry(countryHitsCount);

    // Return the most specific result with the highest composite score
    if (bestCity != null && bestCityScore > 0) {
      return List.of(searchLocationResultMapper.toCityResult(bestCity));
    } else if (bestState != null && bestStateScore > 0) {
      return List.of(searchLocationResultMapper.toStateResult(bestState));
    } else if (bestCountry != null) {
      return List.of(searchLocationResultMapper.toCountryResult(bestCountry));
    } else {
      return List.of();
    }
  }

  /**
   * Gets the top country based on hit counts.
   *
   * @param countryHitsCount The map of country hits.
   * @return The top country name.
   */
  private Country getTopCountry(Map<Country, Integer> countryHitsCount) {
    return countryHitsCount.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  public static class Builder {

    private TextTokeniser textTokeniser = new DefaultTextTokeniser();
    private TextNormaliser textNormaliser = new DefaultTextNormaliser();
    private SearchLocationResultMapper searchLocationResultMapper = new DefaultSearchLocationResultMapper();
    private LocationAliases locationAliases = new DefaultLocationAliases();
    private CountriesDataLoader countriesDataLoader = new DefaultCountriesDataLoaderImpl();

    Builder() {
    }

    public Builder withTextTokeniser(TextTokeniser textTokeniser) {
      this.textTokeniser = textTokeniser;
      return this;
    }

    public Builder withTextNormaliser(TextNormaliser textNormaliser) {
      this.textNormaliser = textNormaliser;
      return this;
    }

    public Builder withLocationMapper(SearchLocationResultMapper searchLocationResultMapper) {
      this.searchLocationResultMapper = searchLocationResultMapper;
      return this;
    }

    public Builder withLocationAliases(LocationAliases locationAliases) {
      this.locationAliases = locationAliases;
      return this;
    }

    public Builder withCountriesDataLoader(
        CountriesDataLoader countriesDataLoader) {
      this.countriesDataLoader = countriesDataLoader;
      return this;
    }

    public SearchLocationService build() {
      return new SearchLocationService(textTokeniser, textNormaliser, searchLocationResultMapper,
          countriesDataLoader,
          locationAliases);
    }
  }
}
