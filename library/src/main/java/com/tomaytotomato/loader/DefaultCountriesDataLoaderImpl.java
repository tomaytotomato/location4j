package com.tomaytotomato.loader;

import com.tomaytotomato.model.Country;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The default loader for retrieving Countries from the DEFAULT_FILE located in the location4j.jar
 *
 */
public class DefaultCountriesDataLoaderImpl implements CountriesDataLoader {

  private static final String DEFAULT_FILE = "/location4j.bin";
  private final List<Country> countries = new ArrayList<>();

  /**
   * Loads a list of {@link Country} from the location4j.bin file.
   */
  public DefaultCountriesDataLoaderImpl() {
    var logger = Logger.getLogger(this.getClass().getName());
    try (InputStream inputStream = getClass().getResourceAsStream(DEFAULT_FILE)) {
      logger.info("Attempting to load countries from " + DEFAULT_FILE);
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: " + DEFAULT_FILE);
      }

      loadLocationsFromBinary(inputStream, logger);
    } catch (IOException | ClassNotFoundException e) {
      logger.log(Level.SEVERE, "Failed to load countries file: " + e.getMessage(), e);
    }
  }

  private void loadLocationsFromBinary(InputStream inputStream, Logger logger)
      throws IOException, ClassNotFoundException {
    try (var objectInputStream = new ObjectInputStream(inputStream)) {
      List<Country> loadedCountries = (List<Country>) objectInputStream.readObject();
      this.countries.addAll(loadedCountries);
      logger.info("Successfully loaded countries binary file");
    } catch (IOException | ClassNotFoundException e) {
      logger.log(Level.SEVERE, "Failed to parse countries file: " + e.getMessage(), e);
    }
  }

  @Override
  public List<Country> getCountries() {
    return countries;
  }
}
