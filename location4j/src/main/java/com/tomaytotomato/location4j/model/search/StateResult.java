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
  public String getName() {
    return name;
  }

  @Override
  public CountryResult getCountry() {
    return country;
  }

  @Override
  public TimeZoneResult getTimeZone() {
    return timezone;
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