package com.tomaytotomato.location4j.loader;

import com.tomaytotomato.location4j.model.Location4JData;
import com.tomaytotomato.location4j.model.lookup.Country;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * The default loader for retrieving Countries from the DEFAULT_FILE located in the location4j.jar
 */
public class DefaultDataLoader implements DataLoader {

  private static final String DEFAULT_FILE = "/location4j.bin";
  private static final Logger logger = Logger.getLogger(DefaultDataLoader.class.getName());

  private Location4JData location4JData;

  /**
   * Loads a list of {@link Country} from the location4j.bin file.
   */
  public DefaultDataLoader() {
    loadData();
  }

  protected void loadData() {
    try (InputStream inputStream = this.getClass().getResourceAsStream(DEFAULT_FILE)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: " + DEFAULT_FILE);
      }

      try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
        location4JData = (Location4JData) objectInputStream.readObject();
        logger.info("Location4J data loaded successfully");
      }
    } catch (IOException | ClassNotFoundException e) {
      logger.severe("Failed to load Location4J data: " + e.getMessage());
    }
  }

  @Override
  public List<Country> getCountries() {
    return location4JData.getCountries();
  }

  @Override
  public Location4JData getLocation4JData() {
    return location4JData;
  }
}
