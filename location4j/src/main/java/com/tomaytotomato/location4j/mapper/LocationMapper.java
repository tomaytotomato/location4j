package com.tomaytotomato.location4j.mapper;

import com.tomaytotomato.location4j.model.City;
import com.tomaytotomato.location4j.model.Country;
import com.tomaytotomato.location4j.model.Location;
import com.tomaytotomato.location4j.model.State;

/**
 * Converts {@link Country}, {@link State} and {@link City} objects into {@link Location} objects
 */
public interface LocationMapper {

  Location toLocation(Country country);

  Location toLocation(City location);

  Location toLocation(State state);

}
