package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

/**
 * Represents a country-level search result.
 */
public record CountryResult(
    Integer countryId,
    String countryName,
    String countryIso2Code,
    String countryIso3Code,
    BigDecimal latitude,
    BigDecimal longitude
) implements SearchLocationResult {

  @Override
  public Integer getCountryId() {
    return countryId;
  }

  @Override
  public String getCountryName() {
    return countryName;
  }

  @Override
  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  @Override
  public String getCountryIso3Code() {
    return countryIso3Code;
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