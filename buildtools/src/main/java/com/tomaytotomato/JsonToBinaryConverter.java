package com.tomaytotomato;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tool is used to transform the opensource JSON data into a binary format for use by
 * location4j.
 */
public class JsonToBinaryConverter {

  private static final String JSON_FILE = "/location4j-countries.json";
  private static final String OUTPUT_FILE = "location4j/src/main/resources/location4j.bin";

  private static final Logger logger = Logger.getLogger(JsonToBinaryConverter.class.getName());
  private static final TextNormaliser textNormaliser = new DefaultTextNormaliser();

  public static void main(String[] args) {
    logger.setLevel(Level.ALL);
    int countryCounter, stateCounter, cityCounter = 0;

    try (InputStream inputStream = JsonToBinaryConverter.class.getResourceAsStream(JSON_FILE)) {
      if (inputStream == null) {
        logger.severe("JSON file not found at path: " + JSON_FILE);
        throw new IllegalArgumentException("File not found!");
      }

      logger.info("Starting JSON deserialization...");

      var jsonString = new String(inputStream.readAllBytes());

      // Fix data for inconsistent namings of JSON properties
      var modifiedJson = fixJSONPropertyNames(jsonString);

      ObjectMapper mapper = new ObjectMapper();
      mapper.setPropertyNamingStrategy(new SnakeCaseStrategy());

      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
      mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
      mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
      mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

      List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<>() {
      });

      // Build links between each object
      List<Country> updatedCountries = countries.stream().map(country -> {
        List<State> updatedStates = country.getStates().stream().map(state -> {

          List<City> cities = state.getCities().stream()
              .map(city -> buildCityLinksToStateAndCountry(city, state, country)).toList();

          return buildStateLinksToCountry(country, state, cities);
        }).toList();
        return buildCountry(country, updatedStates);
      }).toList();

      // Pre-build all data structures
      Location4JData location4JData = buildLocation4JData(updatedCountries);

      Path outputFile = Paths.get(OUTPUT_FILE).toAbsolutePath();
      logger.log(Level.INFO,
          () -> String.format("Serializing data to binary file at:  %s", outputFile));

      try (var fileOutputStream = new FileOutputStream(outputFile.toFile());
          var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(location4JData);
        logger.info("Data successfully serialized to binary format.");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE,
          String.format("IO Exception occurred during serialization: %s", e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, String.format("Argument exception: %s", e.getMessage()), e);
    }
  }

  /**
   * Pre-builds all data structures used by LocationService and SearchLocationService
   */
  private static Location4JData buildLocation4JData(List<Country> countries) {
    logger.info("Building pre-computed data structures...");

    Location4JData data = new Location4JData();
    data.setCountries(countries);

    // Initialize maps
    Map<String, Country> countryNameToCountryMap = new HashMap<>();
    Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
    Map<String, Country> localisedNameToCountryMap = new HashMap<>();
    Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
    Map<String, Country> iso3CodeToCountryMap = new HashMap<>();
    Map<Integer, State> stateIdToStateMap = new HashMap<>();
    Map<Integer, City> cityIdToCityMap = new HashMap<>();
    Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
    Map<String, List<State>> stateCodeToStatesMap = new HashMap<>();
    Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

    countries.forEach(country -> {
      countryIdToCountryMap.put(country.getId(), country);
      countryNameToCountryMap.put(keyMaker(country.getName()), country);

      localisedNameToCountryMap.put(keyMaker(country.getNativeName()), country);
      country.getTranslations().values().stream()
          .map(JsonToBinaryConverter::keyMaker)
          .forEach(translatedName -> localisedNameToCountryMap.put(translatedName, country));
      iso2CodeToCountryMap.put(keyMaker(country.getIso2()), country);
      iso3CodeToCountryMap.put(keyMaker(country.getIso3()), country);

      country.getStates().forEach(state -> {
        stateIdToStateMap.put(state.getId(), state);
        stateNameToStatesMap
            .computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>())
            .add(state);

        if (!Objects.isNull(state.getIso2())) {
          stateCodeToStatesMap
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

    // Set all the maps
    data.setCountryNameToCountryMap(countryNameToCountryMap);
    data.setCountryIdToCountryMap(countryIdToCountryMap);
    data.setLocalisedNameToCountryMap(localisedNameToCountryMap);
    data.setIso2CodeToCountryMap(iso2CodeToCountryMap);
    data.setIso3CodeToCountryMap(iso3CodeToCountryMap);
    data.setStateIdToStateMap(stateIdToStateMap);
    data.setCityIdToCityMap(cityIdToCityMap);
    data.setStateNameToStatesMap(stateNameToStatesMap);
    data.setStateCodeToStatesMap(stateCodeToStatesMap);
    data.setCityNameToCitiesMap(cityNameToCitiesMap);
    data.setSearchCityNameToCitiesMap(new HashMap<>(cityNameToCitiesMap)); // Copy for search service

    logger.info("Pre-computed data structures built successfully.");
    return data;
  }

  private static String fixJSONPropertyNames(String jsonString) {
    // Use String::replace for simple string replacements
    var modifiedJson = jsonString.replace("\"native\"", "\"native_name\"");
    modifiedJson = modifiedJson.replace("\"zoneName\"", "\"zone_name\"");
    modifiedJson = modifiedJson.replace("\"phonecode\"", "\"phone_code\"");
    modifiedJson = modifiedJson.replace("\"gmtOffset\"", "\"gmt_offset\"");
    modifiedJson = modifiedJson.replace("\"gmtOffsetName\"", "\"gmt_offset_name\"");
    modifiedJson = modifiedJson.replace("\"tzName\"", "\"tz_name\"");
    modifiedJson = modifiedJson.replace("\"emojiU\"", "\"emoji_u\"");
    modifiedJson = modifiedJson.replace("\"iso3166_2\"", "\"iso31662\"");
    // Remove problematic timezone strings that can't be deserialized
    modifiedJson = modifiedJson.replaceAll("\"timezone\":\\s*\"[^\"]*\"", "\"timezone\": null");

    return modifiedJson;
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
        .build();
  }

  private static State buildStateLinksToCountry(Country country, State state, List<City> cities) {
    return State.builder()
        .id(state.getId())
        .name(state.getName())
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
