package com.tomaytotomato.location4j.mapper;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.lookup.TimeZone;
import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import com.tomaytotomato.location4j.model.search.TimeZoneResult;
import java.util.List;
import java.util.Objects;

public class DefaultSearchLocationResultMapper implements SearchLocationResultMapper {

  @Override
  public CountryResult toCountryResult(Country country) {
    if (Objects.isNull(country)) {
      return null;
    }
    return new CountryResult(
        country.getId(),
        country.getName(),
        country.getIso2(),
        country.getIso3(),
        country.getLatitude(),
        country.getLongitude(),
        country.getTimezones().stream().map(this::toTimeZoneResult).toList()
    );
  }

  @Override
  public StateResult toStateResult(State state) {
    if (state == null) {
      return null;
    }
    return new StateResult(
        state.getId(),
        state.getName(),
        state.getIso2(),
        state.getIso31662(),
        state.getLatitude(),
        state.getLongitude(),
        toTimeZoneResult(state.getTimezone()),
        toCountryResult(state.getCountry()),
        state.getCities().stream().map(this::toCityResultWithoutStateCities).toList()
    );
  }

  @Override
  public CityResult toCityResult(City city) {
    if (city == null) {
      return null;
    }
    return new CityResult(
        city.getId(),
        city.getName(),
        toCountryResult(city.getCountry()),
        toStateResult(city.getState()),
        city.getLatitude(),
        city.getLongitude(),
        toTimeZoneResult(city.getTimezone()),
        city.getWikiDataId()
    );
  }

  @Override
  public TimeZoneResult toTimeZoneResult(TimeZone timezone) {
    if (timezone == null) {
      return null;
    }

    return new TimeZoneResult(
        timezone.getZoneName(),
        timezone.getAbbreviation(),
        timezone.getTzName(),
        timezone.getGmtOffset(),
        timezone.getGmtOffsetName()
    );
  }

  /**
   * Prevents a circular dependency by creating a City without the State's other cities
   */
  private CityResult toCityResultWithoutStateCities(City city) {
    if (Objects.isNull(city)) {
      return null;
    }
    return new CityResult(
        city.getId(),
        city.getName(),
        toCountryResult(city.getCountry()),
        toShallowState(city.getState()),
        city.getLatitude(),
        city.getLongitude(),
        toTimeZoneResult(city.getTimezone()),
        city.getWikiDataId()
    );
  }

  /**
   * Prevents a circular dependency by creating a State without other cities
   */
  private StateResult toShallowState(State state) {
    if (Objects.isNull(state)) {
      return null;
    }
    return new StateResult(
        state.getId(),
        state.getName(),
        state.getIso2(),
        state.getIso31662(),
        state.getLatitude(),
        state.getLongitude(),
        toTimeZoneResult(state.getTimezone()),
        toCountryResult(state.getCountry()),
        List.of()
    );
  }
}
