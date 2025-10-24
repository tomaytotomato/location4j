package com.tomaytotomato.location4j.model;

import com.tomaytotomato.location4j.loader.TestDataLoader;
import com.tomaytotomato.location4j.model.lookup.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class FindMissingCitiesTest extends TestDataLoader {

    private final Location4JData location4JData;

    public FindMissingCitiesTest() {
        location4JData = this.getDataLoader().getLocation4JData();
    }

    @DisplayName("Find correct spellings for missing cities")
    @Test
    void findMissingCities() {
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();

        // Search with alternatives: original, without accents, common variations
        String[][] searches = {
            {"cordoba", "Argentina"},
            {"bogota", "Colombia"},
            {"bogot", "Colombia"},
            {"medellin", "Colombia"},
            {"frankfurt", "Germany"},
            {"krakow", "Poland"},
            {"cracow", "Poland"},
            {"krakw", "Poland"},
            {"zurich", "Switzerland"},
            {"zrich", "Switzerland"},
            {"xi'an", "China"},
            {"xian", "China"},
            {"hong kong", "Hong Kong"},
            {"bangalore", "India"},
            {"bengaluru", "India"},
            {"jakarta", "Indonesia"},
            {"hanoi", "Vietnam"},
            {"h ni", "Vietnam"},
            {"brasilia", "Brazil"},
            {"sao paulo", "Brazil"},
            {"so paulo", "Brazil"}
        };

        for (String[] search : searches) {
            String searchTerm = search[0];
            String expectedCountry = search[1];

            System.out.println("\n=== Searching for: '" + searchTerm + "' (expecting " + expectedCountry + ") ===");

            // Try exact match first
            if (cityNameToCitiesMap.containsKey(searchTerm)) {
                List<City> cities = cityNameToCitiesMap.get(searchTerm);
                boolean hasCountry = cities.stream()
                    .anyMatch(c -> c.getCountry().getName().equals(expectedCountry));
                if (hasCountry) {
                    System.out.println("✓ FOUND exact match: '" + searchTerm + "' in " + expectedCountry);
                } else {
                    System.out.println("FOUND '" + searchTerm + "' but not in " + expectedCountry);
                }
                continue;
            }

            // Try similar matches
            List<String> similarMatches = cityNameToCitiesMap.keySet().stream()
                .filter(name -> name.startsWith(searchTerm.substring(0, Math.min(4, searchTerm.length()))))
                .filter(name -> {
                    List<City> cities = cityNameToCitiesMap.get(name);
                    return cities.stream().anyMatch(c -> c.getCountry().getName().equals(expectedCountry));
                })
                .limit(5)
                .collect(Collectors.toList());

            if (!similarMatches.isEmpty()) {
                System.out.println("Similar matches in " + expectedCountry + ":");
                for (String match : similarMatches) {
                    System.out.println("  → '" + match + "'");
                }
            } else {
                System.out.println("✗ No matches found");
            }
        }
    }
}

