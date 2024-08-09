package com.tomaytotomato;

import com.tomaytotomato.loader.CountriesDataLoader;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.model.City;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.model.State;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.text.normaliser.TextNormaliser;
import com.tomaytotomato.usecase.FindCity;
import com.tomaytotomato.usecase.FindCountry;
import com.tomaytotomato.usecase.FindState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * This class provides simple lookups for Country, State and City information.
 */
public class LocationService implements FindCountry, FindState, FindCity {

  private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName() + this.getClass().getName());

  private final List<Country> countries;
  /**
   * One-to-one mappings (1:1)
   */
  private final Map<String, Country> countryNameToCountryMap = new HashMap<>();
  private final Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
  private final Map<String, Country> countryNativeNameToCountry = new HashMap<>();
  private final Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
  private final Map<String, Country> iso3CodeToCountryMap = new HashMap<>();
  private final Map<Integer, State> stateIdToStateMap = new HashMap<>();
  private final Map<Integer, City> cityIdToCityMap = new HashMap<>();
  /**
   * One-to-many mappings (1:n)
   */
  private final Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
  private final Map<String, List<State>> stateCodeToStatesMap = new HashMap<>();
  private final Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

  private final TextNormaliser textNormaliser;

  /**
   * Default constructor, creates a LocationService class with default dependencies
   */
  public LocationService() {
    this.textNormaliser = new DefaultTextNormaliser();
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public LocationService(TextNormaliser textNormaliser) {
    this.textNormaliser = textNormaliser;
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public LocationService(TextNormaliser textNormaliser, CountriesDataLoader dataLoader) {
    this.textNormaliser = textNormaliser;
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  private void buildDataStructures() {

    countries.forEach(country -> {
      countryIdToCountryMap.put(country.getId(), country);
      countryNameToCountryMap.put(keyMaker(country.getName()), country);
      if (Objects.isNull(country.getNativeName()) || country.getNativeName().isEmpty()) {
        logger.warning("Country has null native name, skipping mapping: " + country.getName());
      } else {
        countryNativeNameToCountry.put(keyMaker(country.getNativeName()), country);
      }
      iso2CodeToCountryMap.put(keyMaker(country.getIso2()), country);
      iso3CodeToCountryMap.put(keyMaker(country.getIso3()), country);

      country.getStates().forEach(state -> {

        state.setCountryId(country.getId());

        stateIdToStateMap.put(state.getId(), state);
        stateNameToStatesMap.computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>()).add(state);
        stateCodeToStatesMap.computeIfAbsent(keyMaker(state.getStateCode()), k -> new ArrayList<>()).add(state);

        state.getCities().forEach(city -> {
          city.setCountryId(country.getId());
          city.setStateId(state.getId());
          cityNameToCitiesMap.computeIfAbsent(keyMaker(city.getName()), k -> new ArrayList<>()).add(city);
          cityIdToCityMap.put(city.getId(), city);
        });
      });
    });
  }

  @Override
  public Optional<Country> findCountryById(Integer id) {
    if (Objects.isNull(id)) {
      throw new IllegalArgumentException("Country ID cannot be null");
    } else if (id < 1 || id > 250) {
      throw new IllegalArgumentException("Country ID must be within range of [1 to 250]");
    }
    return Optional.ofNullable(countryIdToCountryMap.get(id));
  }

  @Override
  public Optional<Country> findCountryByName(String countryName) {
    if (Objects.isNull(countryName) || countryName.isEmpty()) {
      throw new IllegalArgumentException("Country Name cannot be null or empty");
    } else if (countryName.length() < 4) {
      throw new IllegalArgumentException("Country Name is too short, the shortest country name is 4 characters (Oman)");
    }
    countryName = textNormaliser.normalise(countryName);
    return Optional.ofNullable(countryNameToCountryMap.get(countryName));
  }

  @Override
  public Optional<Country> findCountryByNativeName(String nativeName) {
    if (Objects.isNull(nativeName) || nativeName.isEmpty()) {
      throw new IllegalArgumentException("Country Native Name cannot be null or empty");
    }
    nativeName = textNormaliser.normalise(nativeName);
    return Optional.ofNullable(countryNativeNameToCountry.get(nativeName));
  }

  @Override
  public Optional<Country> findCountryByISO2Code(String iso2Code) {
    if (Objects.isNull(iso2Code) || iso2Code.isEmpty()) {
      throw new IllegalArgumentException("Country ISO2 code cannot be null or empty");
    } else if (iso2Code.length() != 2) {
      throw new IllegalArgumentException("Country ISO2 must be two characters long e.g. GB, US, FR, DE");
    }
    iso2Code = textNormaliser.normalise(iso2Code);
    return Optional.ofNullable(iso2CodeToCountryMap.get(iso2Code));
  }

  @Override
  public Optional<Country> findCountryByISO3Code(String iso3Code) {
    if (Objects.isNull(iso3Code) || iso3Code.isEmpty()) {
      throw new IllegalArgumentException("Country ISO3 code cannot be null or empty");
    } else if (iso3Code.length() != 3) {
      throw new IllegalArgumentException("Country ISO3 must be three characters long e.g. USA, GBR, FRA, GER");
    }
    iso3Code = textNormaliser.normalise(iso3Code);
    return Optional.ofNullable(iso3CodeToCountryMap.get(iso3Code));
  }

  public List<Country> findAllCountries() {
    return countries;
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
  public List<Country> findAllCountriesByStateName(String stateName) {
    if (Objects.isNull(stateName) || stateName.isEmpty()) {
      throw new IllegalArgumentException("State name cannot be null or empty");
    }
    stateName = textNormaliser.normalise(stateName);
    if (stateNameToStatesMap.containsKey(stateName)) {
      return stateNameToStatesMap.get(stateName).stream().map(state -> findCountryById(state.getCountryId()))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .toList();
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<State> findStateById(Integer id) {
    if (Objects.isNull(id)) {
      throw new IllegalArgumentException("State ID cannot be null");
    }
    return Optional.ofNullable(stateIdToStateMap.get(id));
  }

  @Override
  public List<State> findAllStatesByStateName(String stateName) {
    if (Objects.isNull(stateName) || stateName.isEmpty()) {
      throw new IllegalArgumentException("State Name cannot be null or empty");
    } else if (stateName.length() < 3) {
      throw new IllegalArgumentException("State Name is too short, the shortest State with name is 3 characters (Goa, India)");
    }
    stateName = textNormaliser.normalise(stateName);
    if (stateNameToStatesMap.containsKey(stateName)) {
      return stateNameToStatesMap.get(stateName);
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public List<State> findAllStatesByStateCode(String stateCode) {
    if (Objects.isNull(stateCode) || stateCode.isEmpty()) {
      throw new IllegalArgumentException("State Code cannot be null or empty");
    }
    stateCode = textNormaliser.normalise(stateCode);
    if (stateCodeToStatesMap.containsKey(stateCode)) {
      return stateCodeToStatesMap.get(stateCode);
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<City> findCityById(Integer id) {
    if (Objects.isNull(id)) {
      throw new IllegalArgumentException("City ID cannot be null");
    }
    return Optional.ofNullable(cityIdToCityMap.get(id));
  }

  @Override
  public List<City> findAllCities() {
    return cityNameToCitiesMap.values().stream().flatMap(List::stream).toList();
  }

  @Override
  public List<City> findAllCitiesByCityName(String cityName) {
    if (Objects.isNull(cityName) || cityName.isEmpty()) {
      throw new IllegalArgumentException("City Name cannot be null or empty");
    }
    cityName = textNormaliser.normalise(cityName);
    if (cityNameToCitiesMap.containsKey(cityName)) {
      return cityNameToCitiesMap.get(cityName);
    } else {
      return new ArrayList<>();
    }
  }
}
