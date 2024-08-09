package com.tomaytotomato.usecase;

import com.tomaytotomato.model.City;
import java.util.List;
import java.util.Optional;

/**
 * Interface for finding cities based on various criteria.
 */
public interface FindCity {

  /**
   * Find a city by its unique identifier (ID).
   *
   * @param id the unique identifier of the city
   * @return an Optional containing the City if found, otherwise an empty Optional
   */
  Optional<City> findCityById(Integer id);

  /**
   * Retrieve a list of all cities available in the data source.
   *
   * @return a List of all City objects
   */
  List<City> findAllCities();

  /**
   * Retrieve a list of all cities that match the specified city name.
   *
   * @param cityName the name of the city
   * @return a List of City objects that match the specified city name
   */
  List<City> findAllCitiesByCityName(String cityName);
}
