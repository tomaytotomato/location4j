package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.model.search.CountryResult;

import java.util.List;

public interface SearchCountry {

    List<CountryResult> searchCountries(String searchText);
}
