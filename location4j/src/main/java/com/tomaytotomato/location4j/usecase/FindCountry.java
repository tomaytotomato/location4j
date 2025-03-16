package com.tomaytotomato.location4j.usecase;

import com.tomaytotomato.location4j.model.Country;
import java.util.List;
import java.util.Optional;

/**
 * Interface for finding countries based on various criteria.
 */
public interface FindCountry {

  /**
   * Find a country by its unique identifier (ID), which represents a specific country. e.g.
   * Afghanistan - 1, Belgium - 22
   *
   * @param id the unique identifier of the country
   * @return an Optional containing the Country if found, otherwise an empty Optional
   */
  Optional<Country> findCountryById(Integer id);

  /**
   * Find a country by its official name.
   *
   * @param countryName the official name of the country
   * @return an Optional containing the Country if found, otherwise an empty Optional
   */
  Optional<Country> findCountryByName(String countryName);

  /**
   * Finds a country by its localized or native name.
   * <p>
   * This method searches for a country using its localized name, which may vary depending on the
   * language or region. For example, the country of Belgium may be referred to as:
   * <ul>
   *   <li>"Belgien" (German)</li>
   *   <li>"Belgique" (French)</li>
   *   <li>"Belgium" (English)</li>
   * </ul>
   *
   * @param localisedName the localized or native name of the country
   * @return an Optional containing the Country if found, otherwise an empty Optional
   */
  Optional<Country> findCountryByLocalisedName(String localisedName);


  /**
   * Retrieve a list of all countries available in the data source.
   *
   * @return a List of all Country objects
   */
  List<Country> findAllCountries();

  /**
   * Find a country by its ISO 3166-1 alpha-2 code, a two-letter country code.
   *
   * @param iso2Code the ISO 3166-1 alpha-2 code of the country
   * @return an Optional containing the Country if found, otherwise an empty Optional
   */
  Optional<Country> findCountryByISO2Code(String iso2Code);

  /**
   * Find a country by its ISO 3166-1 alpha-3 code, a three-letter country code.
   *
   * @param iso3Code the ISO 3166-1 alpha-3 code of the country
   * @return an Optional containing the Country if found, otherwise an empty Optional
   */
  Optional<Country> findCountryByISO3Code(String iso3Code);

  /**
   * Retrieve a list of all countries that have a state with the specified name.
   *
   * @param stateName the name of the state
   * @return a List of Country objects that contain the specified state name
   */
  List<Country> findAllCountriesByStateName(String stateName);

}
