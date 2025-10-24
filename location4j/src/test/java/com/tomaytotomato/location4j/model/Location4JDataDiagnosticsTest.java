package com.tomaytotomato.location4j.model;

import com.tomaytotomato.location4j.loader.TestDataLoader;
import com.tomaytotomato.location4j.model.lookup.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Location4JDataDiagnosticsTest extends TestDataLoader {

    private final Location4JData location4JData;

    public Location4JDataDiagnosticsTest() {
        location4JData = this.getDataLoader().getLocation4JData();
    }

    @DisplayName("Print sample of city names from the dataset")
    @Test
    void printSampleCityNames() {
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();

        System.out.println("Total unique city names: " + cityNameToCitiesMap.size());
        System.out.println("\nFirst 50 city names in the dataset:");

        cityNameToCitiesMap.keySet().stream()
            .limit(50)
            .forEach(name -> {
                List<City> cities = cityNameToCitiesMap.get(name);
                City firstCity = cities.get(0);
                System.out.println(name + " -> " + firstCity.getCountry().getName());
            });
    }

    @DisplayName("Search for specific problematic cities")
    @Test
    void searchForProblematicCities() {
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();

        String[] searchTerms = {"New York", "New York City", "NYC", "Mexico", "Mexico City",
                                "Rio", "Rio de Janeiro", "London", "Paris", "Tokyo"};

        System.out.println("Searching for cities matching these patterns:");
        for (String searchTerm : searchTerms) {
            List<String> matches = cityNameToCitiesMap.keySet().stream()
                .filter(name -> name.toLowerCase().contains(searchTerm.toLowerCase()))
                .limit(10)
                .collect(Collectors.toList());

            System.out.println("\n'" + searchTerm + "' matches:");
            if (matches.isEmpty()) {
                System.out.println("  No matches found");
            } else {
                matches.forEach(match -> System.out.println("  - " + match));
            }
        }
    }

    @DisplayName("Check cities by ID to verify data is loaded")
    @Test
    void checkCitiesById() {
        Map<Integer, City> cityIdToCityMap = location4JData.getCityIdToCityMap();

        System.out.println("Total cities by ID: " + cityIdToCityMap.size());
        System.out.println("\nFirst 20 cities by ID:");

        cityIdToCityMap.values().stream()
            .limit(20)
            .forEach(city -> System.out.println(city.getId() + ": " + city.getName() +
                ", " + city.getState().getName() + ", " + city.getCountry().getName()));
    }
}

