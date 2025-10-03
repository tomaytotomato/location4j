package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

public sealed interface SearchLocationResult permits CountryResult, StateResult, CityResult {

  String getName();
  CountryResult getCountry();
  TimeZoneResult getTimeZone();
  BigDecimal getLatitude();
  BigDecimal getLongitude();
}