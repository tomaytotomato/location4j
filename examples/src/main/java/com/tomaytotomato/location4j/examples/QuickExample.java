package com.tomaytotomato.location4j.examples;

import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.SearchLocationResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;

import java.util.List;

public class QuickExample {

  public static void main(String[] args) {
    SearchLocationService searchLocationService = SearchLocationService.builder().build();

    // Find all locations named San Francisco
    printResults(searchLocationService.search("san francisco"));

    // Narrow search to the US
    printResults(searchLocationService.search("san francisco, us"));

    // Narrow search further to California
    printResults(searchLocationService.search("san francisco, us california"));
  }

  private static void printResults(List<SearchLocationResult> results) {
    System.out.println("Locations found: " + results.size());

    results.forEach(result -> {
      switch (result) {
        case CountryResult country -> System.out.println("Country: " + country.name());
        case StateResult state -> System.out.println("State: " + state.country().name() + "/" + state.name());
        case CityResult city -> System.out.println("City: " + city.country().name() + "/" + city.state().name() + "/" + city.name());
      }
    });
  }
}
