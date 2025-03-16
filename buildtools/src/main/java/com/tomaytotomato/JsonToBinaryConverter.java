package com.tomaytotomato;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.location4j.model.City;
import com.tomaytotomato.location4j.model.Country;
import com.tomaytotomato.location4j.model.State;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

  public static void main(String[] args) {
    logger.setLevel(Level.ALL);

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
      List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<List<Country>>() {
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

      Path outputFile = Paths.get(OUTPUT_FILE).toAbsolutePath();
      logger.log(Level.INFO,
          () -> String.format("Serializing data to binary file at:  %s", outputFile));

      try (var fileOutputStream = new FileOutputStream(outputFile.toFile());
          var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(updatedCountries);
        logger.info("Data successfully serialized to binary format.");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE,
          String.format("IO Exception occurred during serialization: %s", e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, String.format("Argument exception: %s", e.getMessage()), e);
    }
  }

  private static String fixJSONPropertyNames(String jsonString) {
    // Use String::replace for simple string replacements
    var modifiedJson = jsonString.replace("\"native\"", "\"native_name\"");
    modifiedJson = modifiedJson.replace("\"zoneName\"", "\"zone_name\"");
    modifiedJson = modifiedJson.replace("\"gmtOffset\"", "\"gmt_offset\"");
    modifiedJson = modifiedJson.replace("\"gmtOffsetName\"", "\"gmt_offset_name\"");
    modifiedJson = modifiedJson.replace("\"tzName\"", "\"tz_name\"");
    modifiedJson = modifiedJson.replace("\"emojiU\"", "\"emoji_u\"");
    modifiedJson = modifiedJson.replace("\"iso2\"", "\"iso2_code\"");
    modifiedJson = modifiedJson.replace("\"iso3\"", "\"iso3_code\"");

    return modifiedJson;
  }

  private static Country buildCountry(Country country, List<State> states) {
    return Country.builder()
        .id(country.getId())
        .name(country.getName())
        .iso2Code(country.getIso2Code())
        .iso3Code(country.getIso3Code())
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
        .countryId(country.getId())
        .countryName(country.getName())
        .countryIso2Code(country.getIso2Code())
        .countryIso3Code(country.getIso3Code())
        .stateCode(state.getStateCode())
        .latitude(state.getLatitude())
        .longitude(state.getLongitude())
        .cities(cities)
        .build();
  }

  private static City buildCityLinksToStateAndCountry(City city, State state, Country country) {
    return City.builder()
        .id(city.getId())
        .countryId(country.getId())
        .countryName(country.getName())
        .countryIso2Code(country.getIso2Code())
        .countryIso3Code(country.getIso3Code())
        .stateCode(state.getStateCode())
        .stateId(state.getId())
        .stateName(state.getName())
        .name(city.getName())
        .longitude(city.getLongitude())
        .latitude(city.getLatitude())
        .build();
  }
}
