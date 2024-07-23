package com.tomaytotomato.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomaytotomato.model.Country;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DefaultCountriesDataLoaderImpl implements CountriesDataLoader {

  private static final String DEFAULT_FILE = "location4j-countries.json";
  private final List<Country> countries = new ArrayList<>();

  /**
   * Loads a list of {@link Country} from the location4j-countries.json file
   *
   * @throws IOException if it cannot read the file
   */
  public DefaultCountriesDataLoaderImpl() throws IOException {
    var logger = Logger.getLogger(this.getClass().getName());
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_FILE)) {
      logger.info("Attempting to load countries from " + DEFAULT_FILE);
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: " + DEFAULT_FILE);
      }

      loadLocationsFromJson(inputStream, logger);
    } catch (IOException e) {
      logger.severe("Failed to load countries file: " + e.getMessage());
      throw new IOException("Failed to load countries field : " + e.getMessage());
    }

  }

  private void loadLocationsFromJson(InputStream inputStream, Logger logger) throws IOException {
    var objectMapper = new ObjectMapper();
    try {
      this.countries.addAll(objectMapper.readValue(inputStream, new TypeReference<>() {
      }));
      logger.info("Successfully loaded countries from Json file");
    } catch (IOException e) {
      logger.severe("Failed to parse countries file: " + e.getMessage());
      throw new IOException("Failed to load countries field : " + e.getMessage());
    }
  }

  @Override
  public List<Country> getCountries() {
    return countries;
  }

}
