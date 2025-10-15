package com.tomaytotomato.location4j.examples;

import com.tomaytotomato.location4j.model.search.SearchLocationResult;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;

import java.util.List;

public class SearchLocationServiceExample {

    public static void main(String[] args) {
        SearchLocationService searchLocationService = SearchLocationService.builder()
            .withTextNormaliser(new DefaultTextNormaliser())
            .build();

        // Search for Santa Clara
        List<SearchLocationResult> results = searchLocationService.search("Santa Clara");

        // Search for Santa Clara in the USA
        List<SearchLocationResult> resultsUnitedStates = searchLocationService.search("Santa Clara USA");

        // Search for Santa Clara in California (it works with ISO2 or ISO3) codes
        List<SearchLocationResult> resultsCalifornia = searchLocationService.search("Santa Clara US CA");
    }
}
