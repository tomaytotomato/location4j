package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

/**
 * Represents a country-level search result.
 */
public record CountryResult(
    Integer id,
    String name,
    String iso2,
    String iso3,
    BigDecimal latitude,
    BigDecimal longitude
) implements SearchLocationResult {


  @Override
  public String getName() {
    return "";
  }

  @Override
  public CountryResult getCountry() {
    return null;
  }

  @Override
  public TimeZoneResult getTimeZone() {
    return null;
  }

  @Override
  public BigDecimal getLatitude() {
    return latitude;
  }

  @Override
  public BigDecimal getLongitude() {
    return longitude;
  }
}