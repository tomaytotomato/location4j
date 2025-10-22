package com.tomaytotomato;

import static com.tomaytotomato.Location4JDataUtils.fixJsonPropertyNames;
import static com.tomaytotomato.Location4JDataUtils.getLocation4JDataset;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.location4j.model.Location4JData;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This tool is used to transform the opensource JSON data into a binary format for use by
 * location4j.
 */
public class Location4JDataBuilder {

  private static final String OUTPUT_FILE = "../location4j/location4j/target/generated-resources/location4j.bin";

  private static final TextNormaliser textNormaliser = new DefaultTextNormaliser();

  private static int countryCounter = 0;
  private static int stateCounter = 0;
  private static int cityCounter = 0;

  public static void main(String[] args) {
    try (InputStream inputStream = getLocation4JDataset()) {
      System.out.println("Starting JSON deserialization...");

      var jsonString = new String(inputStream.readAllBytes());
      var modifiedJson = fixJsonPropertyNames(jsonString);

      ObjectMapper mapper = new ObjectMapper();
      mapper.setPropertyNamingStrategy(new SnakeCaseStrategy());

      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
      mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
      mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
      mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

      List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<>() {
      });

      countryCounter = countries.size();
      System.out.println("Number of countries loaded: " + countryCounter);

      List<Country> updatedCountries = countries.stream().map(country -> {
        List<State> updatedStates = country.getStates().stream().map(state -> {

          List<City> cities = state.getCities().stream()
              .map(city -> buildCityLinksToStateAndCountry(city, state, country)).toList();

          cityCounter += cities.size();

          return buildStateLinksToCountry(country, state, cities);
        }).toList();

        stateCounter += updatedStates.size();

        return buildCountry(country, updatedStates);
      }).toList();

      System.out.println("Number of states processed: " + stateCounter);
      System.out.println("Number of cities processed: " + cityCounter);

      var location4JData = buildLocation4JData(updatedCountries);

      var outputFile = Paths.get(OUTPUT_FILE).toAbsolutePath();
      outputFile.getParent().toFile().mkdirs();
      System.out.println("Serializing data to binary file at: " + outputFile);

      try (var fileOutputStream = new FileOutputStream(outputFile.toFile());
          var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(location4JData);
        System.out.println("Data successfully serialized to binary format.");
      }
      System.out.println(String.format("Summary: Countries=%d, States=%d, Cities=%d",
          countryCounter, stateCounter, cityCounter));
    } catch (IOException e) {
      System.err.println("IO Exception occurred during serialization: " + e.getMessage());
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Argument exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Pre-builds all data structures used by LocationService and SearchLocationService
   */
  private static Location4JData buildLocation4JData(List<Country> countries) {
    System.out.println("Building pre-computed data structures...");
    System.out.println("Countries to process in data structures: " + countries.size());

    var data = new Location4JData();
    data.setCountries(countries);

    // Country Maps
    Map<String, Country> countryNameToCountryMap = new HashMap<>();
    Map<String, Country> countryNativeNameToCountryMap = new HashMap<>();
    Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
    Map<String, Country> localisedNameToCountryMap = new HashMap<>();
    Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
    Map<String, Country> iso3CodeToCountryMap = new HashMap<>();

    // State Maps
    Map<Integer, State> stateIdToStateMap = new HashMap<>();
    Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
    Map<String, List<State>> stateNativeNameToStateMap = new HashMap<>();
    Map<String, List<State>> stateIso2CodeToStateMap = new HashMap<>();
    Map<String, State> stateIso31662ToStateMap = new HashMap<>();

    // City Maps
    Map<Integer, City> cityIdToCityMap = new HashMap<>();
    Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

    countries.forEach(country -> {
      countryIdToCountryMap.put(country.getId(), country);
      countryNameToCountryMap.put(keyMaker(country.getName()), country);
      countryNativeNameToCountryMap.put(keyMaker(country.getNativeName()), country);
      localisedNameToCountryMap.put(keyMaker(country.getNativeName()), country);
      country.getTranslations().values().stream()
          .map(Location4JDataBuilder::keyMaker)
          .forEach(translatedName -> localisedNameToCountryMap.put(translatedName, country));
      iso2CodeToCountryMap.put(keyMaker(country.getIso2()), country);
      iso3CodeToCountryMap.put(keyMaker(country.getIso3()), country);

      country.getStates().forEach(state -> {
        stateIdToStateMap.put(state.getId(), state);
        stateNameToStatesMap
            .computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>())
            .add(state);

        if (!Objects.isNull(state.getNativeName()) && !state.getNativeName().isEmpty()) {
          stateNativeNameToStateMap
              .computeIfAbsent(keyMaker(state.getNativeName()), k -> new ArrayList<>())
              .add(state);
        }

        if (!Objects.isNull(state.getIso2())) {
          stateIso2CodeToStateMap
              .computeIfAbsent(keyMaker(state.getIso2()), k -> new ArrayList<>())
              .add(state);
        }

        if (!Objects.isNull(state.getCities())) {
          state.getCities().forEach(city -> {
            cityNameToCitiesMap
                .computeIfAbsent(keyMaker(city.getName()), k -> new ArrayList<>())
                .add(city);
            cityIdToCityMap.put(city.getId(), city);
          });
        }
      });
    });

    data.setCountryNameToCountryMap(countryNameToCountryMap);
    data.setCountryNativeNameToCountryMap(countryNativeNameToCountryMap);
    data.setCountryIdToCountryMap(countryIdToCountryMap);
    data.setLocalisedNameToCountryMap(localisedNameToCountryMap);
    data.setIso2CodeToCountryMap(iso2CodeToCountryMap);
    data.setIso3CodeToCountryMap(iso3CodeToCountryMap);
    data.setStateIdToStateMap(stateIdToStateMap);
    data.setCityIdToCityMap(cityIdToCityMap);
    data.setStateNameToStatesMap(stateNameToStatesMap);
    data.setStateNativeNameToStateMap(stateNativeNameToStateMap);
    data.setStateIso2CodeToStateMap(stateIso2CodeToStateMap);
    data.setStateIso361662ToStateMap(stateIso31662ToStateMap);
    data.setCityNameToCitiesMap(cityNameToCitiesMap);

    System.out.println("Pre-computed data structures built successfully.");
    return data;
  }

  private static Country buildCountry(Country country, List<State> states) {
    return Country.builder()
        .id(country.getId())
        .name(country.getName())
        .iso2Code(country.getIso2())
        .iso3Code(country.getIso3())
        .phoneCode(country.getPhoneCode())
        .numericCode(country.getNumericCode())
        .capital(country.getCapital())
        .currency(country.getCurrency())
        .currencyName(country.getCurrencyName())
        .currencySymbol(country.getCurrencySymbol())
        .tld(country.getTld())
        .nativeName(country.getNativeName())
        .region(country.getRegion())
        .regionId(country.getRegionId())
        .subregion(country.getSubregion())
        .subregionId(country.getSubregionId())
        .states(states)
        .nationality(country.getNationality())
        .timezones(country.getTimezones())
        .translations(country.getTranslations())
        .latitude(country.getLatitude())
        .longitude(country.getLongitude())
        .emoji(country.getEmoji())
        .emojiU(country.getEmojiU())
        .population(country.getPopulation())
        .gdp(country.getGdp())
        .build();
  }

  private static State buildStateLinksToCountry(Country country, State state, List<City> cities) {
    return State.builder()
        .id(state.getId())
        .name(state.getName())
        .nativeName(state.getNativeName())
        .iso31662(state.getIso31662())
        .type(state.getType())
        .country(country)
        .iso2(state.getIso2())
        .latitude(state.getLatitude())
        .longitude(state.getLongitude())
        .cities(cities)
        .build();
  }

  private static City buildCityLinksToStateAndCountry(City city, State state, Country country) {
    return City.builder()
        .id(city.getId())
        .name(city.getName())
        .longitude(city.getLongitude())
        .latitude(city.getLatitude())
        .country(country)
        .state(state)
        .timezone(city.getTimezone())
        .wikiDataId(city.getWikiDataId())
        .build();
  }

  private static String keyMaker(String key) {
    if (Objects.isNull(key) || key.isEmpty()) {
      throw new IllegalArgumentException("Key cannot be null or empty");
    }
    return textNormaliser.normalise(key);
  }
}
