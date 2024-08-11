package com.tomaytotomato.usecase;

import com.tomaytotomato.model.City;
import java.math.BigDecimal;
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

    /**
     * Retrieve the city closest to the supplied Lat/Long pair.
     *
     * @param latitude  the latitude to search for
     * @param longitude the longitude to search for
     * @return the closest City reference
     */
    default City findClosestCityByLatLong(BigDecimal latitude, BigDecimal longitude) {
        return findClosestCityByLatLong(latitude.doubleValue(), longitude.doubleValue());
    }

    /**
     * Retrieve the city closest to the supplied Lat/Long pair.
     *
     * @param latitude  the latitude to search for
     * @param longitude the longitude to search for
     * @return the closest City reference
     */
    City findClosestCityByLatLong(double latitude, double longitude);

    /**
     * Retrieve the city closest to the supplied Lat/Long pair.
     *
     * @param latitude  the latitude to search for
     * @param longitude the longitude to search for
     * @return the closest City reference
     */
    default City findClosestCityByLatLong(String latitude, String longitude) {
        return findClosestCityByLatLong(
                Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
}
