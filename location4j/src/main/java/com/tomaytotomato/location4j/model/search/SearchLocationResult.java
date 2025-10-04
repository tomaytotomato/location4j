package com.tomaytotomato.location4j.model.search;

import java.math.BigDecimal;

public sealed interface SearchLocationResult permits CountryResult, StateResult, CityResult {

  String name();
  CountryResult country();
  BigDecimal latitude();
  BigDecimal longitude();
}