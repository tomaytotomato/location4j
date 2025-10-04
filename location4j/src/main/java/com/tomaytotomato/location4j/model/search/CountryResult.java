package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a country-level search result.
 */
public record CountryResult(
    Integer id,
    String name,
    String iso2,
    String iso3,
    BigDecimal latitude,
    BigDecimal longitude,
    List<TimeZoneResult> timezones
) implements SearchLocationResult {


  @Override
  public String name() {
    return name;
  }

  @Override
  public CountryResult country() {
    return this;
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