package com.tomaytotomato.location4j.usecase.lookup;

import com.tomaytotomato.location4j.loader.DataLoader;
import com.tomaytotomato.location4j.loader.DefaultDataLoader;
import com.tomaytotomato.location4j.model.Location4JData;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;
import java.util.Collections;
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
  private final Map<String, Country> countryNameToCountryMap;
  private final Map<Integer, Country> countryIdToCountryMap;
  private final Map<String, Country> localisedNameToCountryMap;
  private final Map<String, Country> iso2CodeToCountryMap;
  private final Map<String, Country> iso3CodeToCountryMap;
  private final Map<Integer, State> stateIdToStateMap;
  private final Map<Integer, City> cityIdToCityMap;
  private final Map<String, List<State>> stateNameToStatesMap;
  private final Map<String, List<State>> stateCodeToStatesMap;
  private final Map<String, List<City>> cityNameToCitiesMap;

  private final TextNormaliser textNormaliser;

  protected LocationService(TextNormaliser textNormaliser, DataLoader dataLoader) {
    this.textNormaliser = textNormaliser;

    // Load pre-built data structures
    Location4JData location4JData = dataLoader.getLocation4JData();
    this.countries = location4JData.getCountries();
    this.countryNameToCountryMap = location4JData.getCountryNameToCountryMap();
    this.countryIdToCountryMap = location4JData.getCountryIdToCountryMap();
    this.localisedNameToCountryMap = location4JData.getLocalisedNameToCountryMap();
    this.iso2CodeToCountryMap = location4JData.getIso2CodeToCountryMap();
    this.iso3CodeToCountryMap = location4JData.getIso3CodeToCountryMap();
    this.stateIdToStateMap = location4JData.getStateIdToStateMap();
    this.cityIdToCityMap = location4JData.getCityIdToCityMap();
    this.stateNameToStatesMap = location4JData.getStateNameToStatesMap();
    this.stateCodeToStatesMap = location4JData.getStateCodeToStatesMap();
    this.cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();
  }

  public static Builder builder() {
    return new Builder();
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


  @Override
  public List<Country> findAllCountriesByStateName(String stateName) {
    if (Objects.isNull(stateName) || stateName.isEmpty()) {
      throw new IllegalArgumentException("State name cannot be null or empty");
    }
    stateName = textNormaliser.normalise(stateName);
    if (stateNameToStatesMap.containsKey(stateName)) {
      return stateNameToStatesMap.get(stateName).stream()
          .map(state -> findCountryById(state.getCountry().getId()))
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
                  var d = haversineDistance(latitude, longitude, 
                      c.getLatitudeDouble(), c.getLongitudeDouble());
                  if (d < distance[0]) {
                    city[0] = c;
                    distance[0] = d;
                  }
                }))
    );
    return city[0];
  }

  /**
   * Calculate the great circle distance between two points on Earth using the Haversine formula.
   * 
   * @param lat1 Latitude of first point in decimal degrees
   * @param lon1 Longitude of first point in decimal degrees  
   * @param lat2 Latitude of second point in decimal degrees
   * @param lon2 Longitude of second point in decimal degrees
   * @return Distance in kilometers
   */
  private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
    final double R = 6371.0; // Earth's radius in kilometers
    
    // Convert degrees to radians
    double lat1Rad = Math.toRadians(lat1);
    double lon1Rad = Math.toRadians(lon1);
    double lat2Rad = Math.toRadians(lat2);
    double lon2Rad = Math.toRadians(lon2);
    
    // Calculate differences
    double dLat = lat2Rad - lat1Rad;
    double dLon = lon2Rad - lon1Rad;
    
    // Apply Haversine formula
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(lat1Rad) * Math.cos(lat2Rad) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return R * c;
  }

  public static class Builder {

    private TextNormaliser textNormaliser = new DefaultTextNormaliser();
    private DataLoader dataLoader = new DefaultDataLoader();

    Builder() {
    }

    public Builder withDataLoader(DataLoader dataLoader) {
      this.dataLoader = dataLoader;
      return this;
    }

    public Builder withTextNormaliser(TextNormaliser textNormaliser) {
      this.textNormaliser = textNormaliser;
      return this;
    }

    public LocationService build() {
      return new LocationService(textNormaliser, dataLoader);
    }
  }
}
