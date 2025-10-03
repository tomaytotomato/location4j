package com.tomaytotomato.location4j.mapper;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.lookup.TimeZone;
import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import com.tomaytotomato.location4j.model.search.TimeZoneResult;

/**
 * Converts search results into the appropriate type, whether that is a Country, State or City
 */
public interface SearchLocationResultMapper {

  CountryResult toCountryResult(Country country);

  StateResult toStateResult(State state);

  CityResult toCityResult(City location);

  TimeZoneResult toTimeZoneResult(TimeZone timezone);
}
