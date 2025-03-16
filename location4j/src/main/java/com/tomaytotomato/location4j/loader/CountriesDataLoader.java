package com.tomaytotomato.location4j.loader;

import com.tomaytotomato.location4j.model.Country;
import java.util.List;

/**
 * Loads countries from the location4j.jar and provides it to any classes that need it.
 */
public interface CountriesDataLoader {

  /**
   * Returns a list of {@link Country} after loading them from disk
   *
   * @return list of countries
   */
  List<Country> getCountries();

}
