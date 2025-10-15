package com.tomaytotomato.location4j.examples;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.usecase.lookup.LocationService;

import java.util.List;
import java.util.Optional;

public class LocationServiceExample {

  public static void main(String[] args) {
    LocationService locationService = LocationService.builder().build();

    // Get all countries
    List<Country> countries = locationService.findAllCountries();

    // Filter European countries
    List<Country> europeanCountries = countries.stream()
      .filter(country -> "Europe".equals(country.getRegion()))
      .toList();

    // Find Afghanistan by ID
    Optional<Country> afghanistan = locationService.findCountryById(1);

    // Find all cities named San Francisco
    List<City> cities = locationService.findAllCitiesByCityName("San Francisco");
  }
}
