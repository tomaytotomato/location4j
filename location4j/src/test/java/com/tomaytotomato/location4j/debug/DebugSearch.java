package com.tomaytotomato.location4j.debug;

import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.location4j.mapper.DefaultSearchLocationResultMapper;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.LocationAwareTextTokeniser;
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.lookup.City;
import java.util.List;
import java.lang.reflect.Field;

public class DebugSearch {
    public static void main(String[] args) {
        var searchLocationService = SearchLocationService.builder()
            .withLocationAliases(new DefaultLocationAliases())
            .withLocationMapper(new DefaultSearchLocationResultMapper())
            .withCountriesDataLoader(new DefaultCountriesDataLoaderImpl())
            .withTextNormaliser(new DefaultTextNormaliser())
            .withTextTokeniser(new LocationAwareTextTokeniser())
            .build();

        System.out.println("[DEBUG_LOG] ===== DEBUGGING SEARCH ALGORITHM =====");
        
        // Test problematic cases
        String[] testInputs = {
            "New York, NY, USA",
            "Mexico City", 
            "Rio de Janeiro Brazil"
        };
        
        LocationAwareTextTokeniser tokeniser = new LocationAwareTextTokeniser();
        DefaultTextNormaliser normaliser = new DefaultTextNormaliser();
        
        for (String input : testInputs) {
            System.out.println("\n[DEBUG_LOG] === Testing: " + input + " ===");
            
            // Show tokenization
            String normalized = normaliser.normalise(input);
            List<String> tokens = tokeniser.tokenise(normalized);
            System.out.println("[DEBUG_LOG] Normalized: " + normalized);
            System.out.println("[DEBUG_LOG] Tokens: " + tokens);
            
            // Test search result
            var results = searchLocationService.search(input);
            System.out.println("[DEBUG_LOG] Results count: " + results.size());
            if (!results.isEmpty()) {
                var result = results.get(0);
                System.out.println("[DEBUG_LOG] Result type: " + result.getClass().getSimpleName());
                System.out.println("[DEBUG_LOG] Country: " + result.getCountry().getName());
                if (result instanceof com.tomaytotomato.location4j.model.search.CityResult) {
                    var cityResult = (com.tomaytotomato.location4j.model.search.CityResult) result;
                    System.out.println("[DEBUG_LOG] State: " + cityResult.getState().getName());
                    System.out.println("[DEBUG_LOG] City: " + cityResult.getName());
                } else if (result instanceof com.tomaytotomato.location4j.model.search.StateResult) {
                    var stateResult = (com.tomaytotomato.location4j.model.search.StateResult) result;
                    System.out.println("[DEBUG_LOG] State: " + stateResult.getName());
                }
            }
        }
        
        // Investigate available data for specific searches
        System.out.println("\n[DEBUG_LOG] ===== INVESTIGATING DATA AVAILABILITY =====");
        
        try {
            // Use reflection to access internal data structures
            Field countriesField = SearchLocationService.class.getDeclaredField("countries");
            countriesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Country> countries = (List<Country>) countriesField.get(searchLocationService);
            
            System.out.println("[DEBUG_LOG] Total countries: " + countries.size());
            
            // Look for specific locations
            searchForLocation(countries, "New York");
            searchForLocation(countries, "Mexico City");
            searchForLocation(countries, "Rio de Janeiro");
            searchForLocation(countries, "Alabama");
            
        } catch (Exception e) {
            System.out.println("[DEBUG_LOG] Error accessing internal data: " + e.getMessage());
        }
    }
    
    private static void searchForLocation(List<Country> countries, String searchTerm) {
        System.out.println("\n[DEBUG_LOG] Searching for: " + searchTerm);
        
        for (Country country : countries) {
            // Check if it's a country name
            if (country.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println("[DEBUG_LOG] Found country: " + country.getName());
            }
            
            // Check states
            for (State state : country.getStates()) {
                if (state.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    System.out.println("[DEBUG_LOG] Found state: " + state.getName() + " in " + country.getName());
                }
                
                // Check cities
                for (City city : state.getCities()) {
                    if (city.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        System.out.println("[DEBUG_LOG] Found city: " + city.getName() + " in " + state.getName() + ", " + country.getName());
                    }
                }
            }
        }
    }
}