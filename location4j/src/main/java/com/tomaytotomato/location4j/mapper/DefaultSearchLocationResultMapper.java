package com.tomaytotomato.location4j.mapper;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import java.util.Objects;

public class DefaultSearchLocationResultMapper implements SearchLocationResultMapper {

  @Override
  public CountryResult toCountryResult(Country country) {
    Objects.requireNonNull(country);
    return new CountryResult(
        country.getId(),
        country.getName(),
        country.getIso2Code(),
        country.getIso3Code(),
        country.getLatitude(),
        country.getLongitude()
    );
  }

  @Override
  public StateResult toStateResult(State state) {
    Objects.requireNonNull(state);
    return new StateResult(
        state.getCountryId(),
        state.getCountryName(),
        state.getCountryIso2Code(),
        state.getCountryIso3Code(),
        state.getId(),
        state.getName(),
        state.getStateCode(),
        state.getLatitude(),
        state.getLongitude()
    );
  }

  @Override
  public CityResult toCityResult(City city) {
    Objects.requireNonNull(city);
    return new CityResult(
        city.getCountryId(),
        city.getCountryName(),
        city.getCountryIso2Code(),
        city.getCountryIso3Code(),
        city.getStateId(),
        city.getStateName(),
        city.getStateCode(),
        city.getId(),
        city.getName(),
        city.getLatitude(),
        city.getLongitude()
    );
  }
}
