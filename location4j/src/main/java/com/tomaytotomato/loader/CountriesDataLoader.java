package com.tomaytotomato.loader;

import com.tomaytotomato.model.Country;
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
