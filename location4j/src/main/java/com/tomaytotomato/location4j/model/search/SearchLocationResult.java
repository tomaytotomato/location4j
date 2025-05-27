package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

public sealed interface SearchLocationResult permits CountryResult, StateResult, CityResult {

  Integer getCountryId();
  String getCountryName();
  String getCountryIso2Code();
  String getCountryIso3Code();
  BigDecimal getLatitude();
  BigDecimal getLongitude();
}