package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

/**
 * Represents a city-level search result, that has much richer information
 */
public record CityResult(
    Integer id,
    String name,
    CountryResult country,
    StateResult state,
    BigDecimal latitude,
    BigDecimal longitude,
    TimeZoneResult timezone,
    String wikiDataId
) implements SearchLocationResult {

  public StateResult getState() {
    return state;
  }

}