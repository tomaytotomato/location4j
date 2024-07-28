package com.tomaytotomato;

import com.tomaytotomato.aliases.DefaultLocationAliases;
import com.tomaytotomato.aliases.LocationAliases;
import com.tomaytotomato.loader.CountriesDataLoader;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.mapper.DefaultLocationMapper;
import com.tomaytotomato.mapper.LocationMapper;
import com.tomaytotomato.model.City;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.model.Location;
import com.tomaytotomato.model.State;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.text.normaliser.TextNormaliser;
import com.tomaytotomato.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.text.tokeniser.TextTokeniser;
import com.tomaytotomato.usecase.SearchLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Provides search functionality to find a Country, State or City based on text input
 *
 */
public class SearchLocationService implements SearchLocation {

  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final List<Country> countries;
  /**
   * 1 to 1 mappings
   */
  private final Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
  private final Map<String, Country> countryNameToCountryMap = new HashMap<>();
  private final Map<String, Country> countryNativeNameToCountry = new HashMap<>();
  private final Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
  private final Map<String, Country> iso3CodeToCountryMap = new HashMap<>();
  private final Map<Integer, State> stateIdToStateMap = new HashMap<>();
  /**
   * 1 to many mappings
   */
  private final Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
  private final Map<String, List<State>> stateCodeToStatesMap = new HashMap<>();
  private final Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

  private final TextTokeniser textTokeniser;
  private final TextNormaliser textNormaliser;
  private final LocationMapper locationMapper;
  private final LocationAliases locationAliases;

  public SearchLocationService() {
    textTokeniser = new DefaultTextTokeniser();
    textNormaliser = new DefaultTextNormaliser();
    locationMapper = new DefaultLocationMapper();
    locationAliases = new DefaultLocationAliases();
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public SearchLocationService(TextTokeniser textTokeniser, TextNormaliser textNormaliser,
      LocationMapper locationMapper, CountriesDataLoader dataLoader, LocationAliases locationAliases) {
    this.textTokeniser = textTokeniser;
    this.textNormaliser = textNormaliser;
    this.locationMapper = locationMapper;
    this.locationAliases = locationAliases;
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public void buildDataStructures() {
    countries.forEach(country -> {
      buildCountryLookups(country);
      country.getStates().forEach(state -> buildStateLookups(state, country));
    });
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

    locationAliases.getCountryIso2Aliases().forEach((alias,originalKey) -> {
      var country = iso2CodeToCountryMap.get(keyMaker(originalKey));
      countryNameToCountryMap.put(keyMaker(alias), country);
    });

    locationAliases.getCountryIso3Aliases().forEach((alias,originalKey) -> {
      var country = iso3CodeToCountryMap.get(keyMaker(originalKey));
      countryNameToCountryMap.put(keyMaker(alias), country);
    });

    locationAliases.getStateNameAliases().forEach((alias,originalKey) -> {
      var states = stateNameToStatesMap.get(keyMaker(originalKey));
      stateNameToStatesMap.put(alias, states);
    });

    locationAliases.getCityNameAliases().forEach((alias,originalKey) -> {
      var cities = cityNameToCitiesMap.get(keyMaker(originalKey));
      cityNameToCitiesMap.put(alias, cities);
    });
  }

  /**
   * Maps a country to various lookup maps.
   *
   * @param country The country to be mapped.
   */
  private void buildCountryLookups(Country country) {
    countryNameToCountryMap.put(keyMaker(country.getName()), country);
    countryIdToCountryMap.put(country.getId(), country);
    if (!Objects.isNull(country.getNativeName()) && !country.getNativeName().isEmpty()) {
      countryNativeNameToCountry.put(keyMaker(country.getNativeName()), country);
    }
    iso2CodeToCountryMap.put(keyMaker(country.getIso2()), country);
    iso3CodeToCountryMap.put(keyMaker(country.getIso3()), country);
  }

  /**
   * Maps state and its associated cities to various lookup maps.
   *
   * @param state   The state to be mapped.
   * @param country The country the state belongs to.
   */
  private void buildStateLookups(State state, Country country) {
    state.setCountryId(country.getId());
    state.setCountryName(country.getName());
    state.setCountryIso2Code(country.getIso2());
    state.setCountryIso3Code(country.getIso3());

    stateIdToStateMap.put(state.getId(), state);
    stateNameToStatesMap.computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>())
        .add(state);
    stateCodeToStatesMap.computeIfAbsent(keyMaker(state.getStateCode()), k -> new ArrayList<>())
        .add(state);

    state.getCities().forEach(city -> buildCityLookups(city, state, country));
  }

  /**
   * Maps a city to various lookup maps.
   *
   * @param city    The city to be mapped.
   * @param state   The state the city belongs to.
   * @param country The country the city belongs to.
   */
  private void buildCityLookups(City city, State state, Country country) {
    city.setCountryId(country.getId());
    city.setCountryName(country.getName());
    city.setCountryIso2Code(country.getIso2());
    city.setCountryIso3Code(country.getIso3());
    city.setStateId(state.getId());
    city.setStateName(state.getName());
    city.setStateCode(state.getStateCode());
    cityNameToCitiesMap.computeIfAbsent(keyMaker(city.getName()), k -> new ArrayList<>()).add(city);
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
  public List<Location> search(String text) {
    if (Objects.isNull(text) || text.isEmpty()) {
      throw new IllegalArgumentException("SearchLocation Text cannot be null or empty");
    } else if (text.length() < 2) {
      return List.of();
    }

    text = textNormaliser.normalise(text);

    // Direct matches
    List<Location> directMatches = findDirectMatches(text);
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
  private List<Location> findDirectMatches(String text) {
    List<Location> matches = new ArrayList<>();

    if (countryNameToCountryMap.containsKey(text)) {
      matches.add(locationMapper.toLocation(countryNameToCountryMap.get(text)));
      return matches;
    }

    if (text.length() == 3 && iso3CodeToCountryMap.containsKey(text)) {
      matches.add(locationMapper.toLocation(iso3CodeToCountryMap.get(text)));
      return matches;
    }

    if (text.length() == 2) {
      if (iso2CodeToCountryMap.containsKey(text)) {
        matches.add(locationMapper.toLocation(iso2CodeToCountryMap.get(text)));
      }
      if (stateCodeToStatesMap.containsKey(text)) {
        stateCodeToStatesMap.get(text)
            .forEach(state -> matches.add(locationMapper.toLocation(state)));
      }
      return matches;
    }

    if (stateNameToStatesMap.containsKey(text)) {
      stateNameToStatesMap.get(text)
          .forEach(state -> matches.add(locationMapper.toLocation(state)));
      return matches;
    }

    if (cityNameToCitiesMap.containsKey(text)) {
      cityNameToCitiesMap.get(text).forEach(city -> matches.add(locationMapper.toLocation(city)));
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
  private List<Location> findTokenizedMatches(List<String> tokenizedText) {
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

    var stateFound = false;
    var cityFound = false;

    for (String token : tokenizedText) {
      if (stateNameToStatesMap.containsKey(token) && !stateFound) {
        stateNameToStatesMap.get(token).forEach(state -> {
          var country = countryIdToCountryMap.get(state.getCountryId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
        stateFound = true;
      }
      if (stateCodeToStatesMap.containsKey(token) && !stateFound) {
        stateCodeToStatesMap.get(token).forEach(state -> {
          var country = countryIdToCountryMap.get(state.getCountryId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
        stateFound = true;
      }
      if (cityNameToCitiesMap.containsKey(token) && !cityFound) {
        cityNameToCitiesMap.get(token).forEach(city -> {
          var country = countryIdToCountryMap.get(city.getCountryId());
          var state = stateIdToStateMap.get(city.getStateId());
          stateHitsCount.put(state, stateHitsCount.getOrDefault(state, 0) + 1);
          cityHitsCount.put(city, cityHitsCount.getOrDefault(city, 0) + 1);
          countryHitsCount.put(country, countryHitsCount.getOrDefault(country, 0) + 1);
        });
        cityFound = true;
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
            .filter(state -> state.getCountryName().equals(topCountry.getName()))
            .forEach(state -> stateHitsCount.put(state,
                stateHitsCount.getOrDefault(state, 0) + 1));
        stateFound = true;
      }
      if (stateCodeToStatesMap.containsKey(token) && !stateFound) {
        stateCodeToStatesMap.get(token).stream()
            .filter(state -> state.getCountryName().equals(topCountry.getName()))
            .forEach(state -> stateHitsCount.put(state,
                stateHitsCount.getOrDefault(state, 0) + 1));
        stateFound = true;
      }
      if (cityNameToCitiesMap.containsKey(token) && !cityFound) {
        cityNameToCitiesMap.get(token).stream()
            .filter(city -> city.getCountryName().equals(topCountry.getName()))
            .forEach(city -> {
              var state = stateIdToStateMap.get(city.getStateId());
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
   *
   * @param countryHitsCount The map of country hits.
   * @param stateHitsCount   The map of state hits.
   * @param cityHitsCount    The map of city hits.
   * @return A list of top matching locations.
   */
  private List<Location> getTopMatchingLocations(Map<Country, Integer> countryHitsCount,
      Map<State, Integer> stateHitsCount,
      Map<City, Integer> cityHitsCount) {

    var topCountry = getTopCountry(countryHitsCount);

    var topCity = cityHitsCount.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

    var topState = stateHitsCount.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

    if (Objects.isNull(topCountry)) {
      if (Objects.isNull(topState)) {
        if (Objects.isNull(topCity)) {
          return List.of();
        } else {
          return List.of(locationMapper.toLocation(topCity));
        }
      } else {
        return List.of(locationMapper.toLocation(topState));
      }
    } else {
      return List.of(buildLocationResult(topCountry, topState, topCity));
    }
  }

  private static Location buildLocationResult(Country topCountry, State topState, City topCity) {
    var locationBuilder = Location.builder();

    locationBuilder.countryName(topCountry.getName());
    locationBuilder.countryId(topCountry.getId());
    locationBuilder.countryIso2Code(topCountry.getIso2());
    locationBuilder.countryIso3Code(topCountry.getIso3());
    locationBuilder.latitude(topCountry.getLatitude());
    locationBuilder.longitude(topCountry.getLongitude());

    if (!Objects.isNull(topState)) {
      locationBuilder.stateName(topState.getName());
      locationBuilder.stateCode(topState.getStateCode());
      locationBuilder.stateId(topState.getId());
      locationBuilder.latitude(topState.getLatitude());
      locationBuilder.longitude(topState.getLongitude());
    }

    if (!Objects.isNull(topCity)) {
      locationBuilder.city(topCity.getName());
      locationBuilder.cityId(topCity.getId());
      locationBuilder.latitude(topCity.getLatitude());
      locationBuilder.longitude(topCity.getLongitude());
    }
    return locationBuilder.build();
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
}
