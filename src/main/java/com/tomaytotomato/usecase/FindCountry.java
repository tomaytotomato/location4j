package com.tomaytotomato.usecase;

import com.tomaytotomato.model.Country;

import java.util.List;
import java.util.Optional;

public interface FindCountry {

    Optional<Country> findCountryById(Integer id);

    Optional<Country> findCountryByName(String countryName);

    Optional<Country> findCountryByNativeName(String nativeName);

    List<Country> findAllCountries();

    Optional<Country> findCountryByISO2Code(String iso2Code);

    Optional<Country> findCountryByISO3Code(String iso3Code);

    List<Country> findAllCountriesByStateName(String stateName);

}
