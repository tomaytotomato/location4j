package com.tomaytotomato.location4j;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.tomaytotomato.location4j.loader.CountriesDataLoader;
import com.tomaytotomato.location4j.model.City;
import com.tomaytotomato.location4j.model.Country;
import com.tomaytotomato.location4j.model.State;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;
import com.tomaytotomato.location4j.usecase.FindCity;
import com.tomaytotomato.location4j.usecase.FindCountry;
import com.tomaytotomato.location4j.usecase.FindState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides simple lookups for Country, State and City information.
 */
public class LocationService implements FindCountry, FindState, FindCity {

  private final List<Country> countries;

  /**
   * One-to-one mappings (1:1)
   */
  private final Map<String, Country> countryNameToCountryMap = new HashMap<>();

  private final Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
  private final Map<String, Country> localisedNameToCountryMap = new HashMap<>();
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

  protected LocationService(TextNormaliser textNormaliser, CountriesDataLoader dataLoader) {
    this.textNormaliser = textNormaliser;
    countries = dataLoader.getCountries();
    buildDataStructures();
  }

  public static LocationServiceBuilder builder() {
    return new LocationServiceBuilder();
  }

  private void buildDataStructures() {

    countries.forEach(
        country -> {
          countryIdToCountryMap.put(country.getId(), country);
          countryNameToCountryMap.put(keyMaker(country.getName()), country);

          localisedNameToCountryMap.put(keyMaker(country.getNativeName()), country);
          country.getTranslations().values().stream()
              .map(this::keyMaker)
              .forEach(
                  translatedName ->
                      localisedNameToCountryMap.put(translatedName, country));
          iso2CodeToCountryMap.put(keyMaker(country.getIso2Code()), country);
          iso3CodeToCountryMap.put(keyMaker(country.getIso3Code()), country);

          country.getStates()
              .forEach(
                  state -> {
                    state.setCountryId(country.getId());

                    stateIdToStateMap.put(state.getId(), state);
                    stateNameToStatesMap
                        .computeIfAbsent(
                            keyMaker(state.getName()),
                            k -> new ArrayList<>())
                        .add(state);

                    if (!Objects.isNull(state.getStateCode())) {
                      stateCodeToStatesMap
                          .computeIfAbsent(
                              keyMaker(state.getStateCode()),
                              k -> new ArrayList<>())
                          .add(state);
                    }

                    state.getCities()
                        .forEach(
                            city -> {
                              city.setCountryId(country.getId());
                              city.setStateId(state.getId());
                              cityNameToCitiesMap
                                  .computeIfAbsent(
                                      keyMaker(
                                          city.getName()),
                                      k -> new ArrayList<>())
                                  .add(city);
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
      throw new IllegalArgumentException(
          "Country Name is too short, the shortest country name is 4 characters (Oman)");
    }
    countryName = textNormaliser.normalise(countryName);
    return Optional.ofNullable(countryNameToCountryMap.get(countryName));
  }

  @Override
  public Optional<Country> findCountryByLocalisedName(String localisedName) {
    if (Objects.isNull(localisedName) || localisedName.isEmpty()) {
      throw new IllegalArgumentException("Country Localised Name cannot be null or empty");
    }
    localisedName = textNormaliser.normalise(localisedName);
    return Optional.ofNullable(localisedNameToCountryMap.get(localisedName));
  }

  @Override
  public Optional<Country> findCountryByISO2Code(String iso2Code) {
    if (Objects.isNull(iso2Code) || iso2Code.isEmpty()) {
      throw new IllegalArgumentException("Country ISO2 code cannot be null or empty");
    } else if (iso2Code.length() != 2) {
      throw new IllegalArgumentException(
          "Country ISO2 must be two characters long e.g. GB, US, FR, DE");
    }
    iso2Code = textNormaliser.normalise(iso2Code);
    return Optional.ofNullable(iso2CodeToCountryMap.get(iso2Code));
  }

  @Override
  public Optional<Country> findCountryByISO3Code(String iso3Code) {
    if (Objects.isNull(iso3Code) || iso3Code.isEmpty()) {
      throw new IllegalArgumentException("Country ISO3 code cannot be null or empty");
    } else if (iso3Code.length() != 3) {
      throw new IllegalArgumentException(
          "Country ISO3 must be three characters long e.g. USA, GBR, FRA, GER");
    }
    iso3Code = textNormaliser.normalise(iso3Code);
    return Optional.ofNullable(iso3CodeToCountryMap.get(iso3Code));
  }

  public List<Country> findAllCountries() {
    return Collections.unmodifiableList(countries);
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
      return stateNameToStatesMap.get(stateName).stream()
          .map(state -> findCountryById(state.getCountryId()))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .collect(
              Collectors.collectingAndThen(
                  Collectors.toList(), Collections::unmodifiableList));
    } else {
      return Collections.emptyList();
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
      throw new IllegalArgumentException(
          "State Name is too short, the shortest State with name is 3 characters (Goa,"
              + " India)");
    }
    stateName = textNormaliser.normalise(stateName);
    if (stateNameToStatesMap.containsKey(stateName)) {
      return Collections.unmodifiableList(stateNameToStatesMap.get(stateName));
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public List<State> findAllStatesByStateCode(String stateCode) {
    if (Objects.isNull(stateCode) || stateCode.isEmpty()) {
      throw new IllegalArgumentException("State Code cannot be null or empty");
    }
    stateCode = textNormaliser.normalise(stateCode);
    if (stateCodeToStatesMap.containsKey(stateCode)) {
      return Collections.unmodifiableList(stateCodeToStatesMap.get(stateCode));
    } else {
      return Collections.emptyList();
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
    return cityNameToCitiesMap.values().stream()
        .flatMap(List::stream)
        .collect(
            Collectors.collectingAndThen(
                Collectors.toList(), Collections::unmodifiableList));
  }

  @Override
  public List<City> findAllCitiesByCityName(String cityName) {
    if (Objects.isNull(cityName) || cityName.isEmpty()) {
      throw new IllegalArgumentException("City Name cannot be null or empty");
    }
    cityName = textNormaliser.normalise(cityName);
    if (cityNameToCitiesMap.containsKey(cityName)) {
      return Collections.unmodifiableList(cityNameToCitiesMap.get(cityName));
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public City findClosestCityByLatLong(double latitude, double longitude) {
    var cities = cityNameToCitiesMap.values();
    double[] distance = {Double.MAX_VALUE};
    City[] city = {null};
    cities.forEach(
        it ->
            it.forEach(
                (c -> {
                  var d =
                      sqrt(
                          pow(latitude - c.getLatitudeDouble(), 2)
                              + pow(
                              longitude - c.getLongitudeDouble(),
                              2));
                  if (d < distance[0]) {
                    city[0] = c;
                    distance[0] = d;
                  }
                }))
    );
    return city[0];
  }
}
