package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.model.search.CityResult;

import java.util.List;

public interface SearchCity {

    /**
     * Searches for cities based on the provided city name.
     *
     * @param searchText the name of the city to search for
     * @return a List of {@link CityResult} objects that match the city name
     */
    List<CityResult> searchCities(String searchText);
}
