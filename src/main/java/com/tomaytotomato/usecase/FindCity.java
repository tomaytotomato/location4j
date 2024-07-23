package com.tomaytotomato.usecase;

import com.tomaytotomato.model.City;
import java.util.List;
import java.util.Optional;

public interface FindCity {

  Optional<City> findCityById(Integer id);

  List<City> findAllCities();

  List<City> findAllCitiesByCityName(String cityName);
}
