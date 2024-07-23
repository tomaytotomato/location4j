package com.tomaytotomato;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class SearchLocationService implements SearchLocation {

  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final List<Country> countries;
  /**
   * 1 to 1 mappings
   */
  private final Map<String, Country> countryNameToCountryMap = new HashMap<>();
  private final Map<String, Country> countryNativeNameToCountry = new HashMap<>();
  private final Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
  private final Map<String, Country> iso3CodeToCountryMap = new HashMap<>();
  /**
   * 1 to many mappings
   */
  private final Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
  private final Map<String, List<State>> stateCodeToStatesMap = new HashMap<>();
  private final Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

  private final TextTokeniser textTokeniser;
  private final TextNormaliser textNormaliser;
  private final LocationMapper locationMapper;

  public SearchLocationService() throws IOException {
    textTokeniser = new DefaultTextTokeniser();
    textNormaliser = new DefaultTextNormaliser();
    locationMapper = new DefaultLocationMapper();
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public SearchLocationService(TextTokeniser textTokeniser, TextNormaliser textNormaliser,
      LocationMapper locationMapper, CountriesDataLoader dataLoader) {
    this.textTokeniser = textTokeniser;
    this.textNormaliser = textNormaliser;
    this.locationMapper = locationMapper;
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public void buildDataStructures() {
    countries.forEach(country -> {
      mapCountry(country);
      country.getStates().forEach(state -> mapState(state, country));
    });
    specialMappings();
  }

  /**
   * Adds special mappings for countries.
   */
  private void specialMappings() {
    Country ukCountry = countryNameToCountryMap.get(keyMaker("United Kingdom"));
    if (ukCountry != null) {
      mapCountryAliases(ukCountry, "Scotland", "England", "Northern Ireland", "Wales");
      iso2CodeToCountryMap.put("uk", ukCountry);
    } else {
      logger.warning(
          "United Kingdom not found in the country map, unable to add special mappings for Scotland, England, Northern Ireland, and Wales.");
    }
  }

  /**
   * Maps a country to various lookup maps.
   *
   * @param country The country to be mapped.
   */
  private void mapCountry(Country country) {
    countryNameToCountryMap.put(keyMaker(country.getName()), country);
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
  private void mapState(State state, Country country) {
    state.setCountryId(country.getId());
    state.setCountryName(country.getName());
    state.setCountryIso2Code(country.getIso2());
    state.setCountryIso3Code(country.getIso3());

    stateNameToStatesMap.computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>())
        .add(state);
    stateCodeToStatesMap.computeIfAbsent(keyMaker(state.getStateCode()), k -> new ArrayList<>())
        .add(state);

    state.getCities().forEach(city -> mapCity(city, state, country));
  }

  /**
   * Maps a city to various lookup maps.
   *
   * @param city    The city to be mapped.
   * @param state   The state the city belongs to.
   * @param country The country the city belongs to.
   */
  private void mapCity(City city, State state, Country country) {
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

  /**
   * Adds aliases for a given country.
   *
   * @param country The country to which aliases will be mapped.
   * @param aliases Aliases for the country.
   */
  private void mapCountryAliases(Country country, String... aliases) {
    for (String alias : aliases) {
      countryNameToCountryMap.put(keyMaker(alias), country);
    }
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
    Map<String, Integer> countryHitsCount = new HashMap<>();
    Map<String, Integer> stateHitsCount = new HashMap<>();
    Map<String, Integer> cityHitsCount = new HashMap<>();

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
      Map<String, Integer> countryHitsCount) {
    Iterator<String> iterator = tokenizedText.iterator();

    while (iterator.hasNext()) {
      String token = iterator.next();
      if (countryNameToCountryMap.containsKey(token)) {
        countryHitsCount.put(token, countryHitsCount.getOrDefault(token, 0) + 1);
        iterator.remove();
        return true;
      }
      if (iso3CodeToCountryMap.containsKey(token)) {
        countryHitsCount.put(token, countryHitsCount.getOrDefault(token, 0) + 1);
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
      Map<String, Integer> countryHitsCount,
      Map<String, Integer> stateHitsCount, Map<String, Integer> cityHitsCount) {
    for (String token : tokenizedText) {
      if (stateNameToStatesMap.containsKey(token)) {
        stateNameToStatesMap.get(token).forEach(state -> {
          stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
          countryHitsCount.put(state.getCountryName(),
              countryHitsCount.getOrDefault(state.getCountryName(), 0) + 1);
        });
      }
      if (stateCodeToStatesMap.containsKey(token)) {
        stateCodeToStatesMap.get(token).forEach(state -> {
          stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
          countryHitsCount.put(state.getCountryName(),
              countryHitsCount.getOrDefault(state.getCountryName(), 0) + 1);
        });
      }
      if (cityNameToCitiesMap.containsKey(token)) {
        cityNameToCitiesMap.get(token).forEach(city -> {
          cityHitsCount.put(city.getName(), cityHitsCount.getOrDefault(city.getName(), 0) + 1);
          stateHitsCount.put(city.getStateName(),
              stateHitsCount.getOrDefault(city.getStateName(), 0) + 1);
          countryHitsCount.put(city.getCountryName(),
              countryHitsCount.getOrDefault(city.getCountryName(), 0) + 1);
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
      Map<String, Integer> countryHitsCount,
      Map<String, Integer> stateHitsCount, Map<String, Integer> cityHitsCount) {
    String topCountry = getTopCountry(countryHitsCount);

    for (String token : tokenizedText) {
      if (stateNameToStatesMap.containsKey(token)) {
        stateNameToStatesMap.get(token).stream()
            .filter(state -> state.getCountryName().equals(topCountry))
            .forEach(state -> stateHitsCount.put(state.getName(),
                stateHitsCount.getOrDefault(state.getName(), 0) + 1));
      }
      if (stateCodeToStatesMap.containsKey(token)) {
        stateCodeToStatesMap.get(token).stream()
            .filter(state -> state.getCountryName().equals(topCountry))
            .forEach(state -> stateHitsCount.put(state.getName(),
                stateHitsCount.getOrDefault(state.getName(), 0) + 1));
      }
      if (cityNameToCitiesMap.containsKey(token)) {
        cityNameToCitiesMap.get(token).stream()
            .filter(city -> city.getCountryName().equals(topCountry))
            .forEach(city -> {
              cityHitsCount.put(city.getName(), cityHitsCount.getOrDefault(city.getName(), 0) + 1);
              stateHitsCount.put(city.getStateName(),
                  stateHitsCount.getOrDefault(city.getStateName(), 0) + 1);
            });
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
  private List<Location> getTopMatchingLocations(Map<String, Integer> countryHitsCount,
      Map<String, Integer> stateHitsCount,
      Map<String, Integer> cityHitsCount) {
    var topCountry = getTopCountry(countryHitsCount);
    var topState = getTopHit(stateHitsCount);

    if (Objects.isNull(topState)) {
      return List.of(locationMapper.toLocation(countryNameToCountryMap.get(topCountry)));
    }

    var topCity = getTopHit(cityHitsCount);

    if (!Objects.isNull(topCity) && !Objects.isNull(topState) && !Objects.isNull(topCountry)) {
      var cityMatches = cityNameToCitiesMap.get(topCity.toLowerCase());
      for (City city : cityMatches) {
        if (city.getStateName().equals(topState) && city.getCountryName().equals(topCountry)) {
          return List.of(locationMapper.toLocation(city));
        }
      }
    }

    return List.of();
  }

  /**
   * Gets the top country based on hit counts.
   *
   * @param countryHitsCount The map of country hits.
   * @return The top country name.
   */
  private String getTopCountry(Map<String, Integer> countryHitsCount) {
    return countryHitsCount.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  /**
   * Gets the top hit from the given hit count map.
   *
   * @param hitsCount The map of hits.
   * @return The top hit name.
   */
  private String getTopHit(Map<String, Integer> hitsCount) {
    return hitsCount.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);
  }
}
