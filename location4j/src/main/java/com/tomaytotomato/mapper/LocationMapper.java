package com.tomaytotomato.mapper;

import com.tomaytotomato.model.City;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.model.Location;
import com.tomaytotomato.model.State;

/**
 * Converts {@link Country}, {@link State} and {@link City} objects into {@link Location} objects
 */
public interface LocationMapper {

  Location toLocation(Country country);

  Location toLocation(City location);

  Location toLocation(State state);

}
