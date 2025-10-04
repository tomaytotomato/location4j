package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a state-level search result.
 */
public record StateResult(
    Integer id,
    String name,
    String iso2,
    String iso31662,
    BigDecimal latitude,
    BigDecimal longitude,
    TimeZoneResult timezone,
    CountryResult country,
    List<CityResult> cities
    ) implements SearchLocationResult {

  @Override
  public String name() {
    return name;
  }

  @Override
  public CountryResult country() {
    return country;
  }

  @Override
  public BigDecimal latitude() {
    return latitude;
  }

  @Override
  public BigDecimal longitude() {
    return longitude;
  }
}