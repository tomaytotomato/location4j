package com.tomaytotomato.location4j.mapper;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.lookup.TimeZone;
import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import com.tomaytotomato.location4j.model.search.TimeZoneResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class DefaultSearchLocationResultMapper implements SearchLocationResultMapper {

  @Override
  public CountryResult toCountryResult(Country country) {
    Objects.requireNonNull(country);
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
    return new StateResult(
        state.getId(),
        state.getName(),
        state.getIso2(),
        state.getIso31662(),
        state.getLatitude(),
        state.getLongitude(),
        toTimeZoneResult(state.getTimezone()),
        toCountryResult(state.getCountry()),
        state.getCities() == null ? null : state.getCities().stream().map(this::toCityResult).toList()
    );
  }

  @Override
  public CityResult toCityResult(City city) {
    Objects.requireNonNull(city);

    return new CityResult(
        city.getId(),
        city.getName(),
        null,
        null,
        city.getLatitude(),
        city.getLongitude(),
        null,
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
}
