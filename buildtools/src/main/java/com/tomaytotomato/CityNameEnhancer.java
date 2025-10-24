package com.tomaytotomato;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tomaytotomato.Location4JDataUtils.fixJsonPropertyNames;
import static com.tomaytotomato.Location4JDataUtils.getLocation4JDataset;

/**
 * Analyzes the dataset to identify cities with multilingual naming issues.
 * <p>
 * This tool helps identify major cities that have different names in English vs their native language,
 * which can cause search failures. For example:
 * - "Mexico City" vs "Ciudad de México"
 * - "Munich" vs "München"
 * - "The Hague" vs "Den Haag"
 * <p>
 * The tool generates a report of capital cities and major cities to help build an alias mapping
 * or prepare data for upstream contribution.
 */
public class CityNameEnhancer {

    public static void main(String[] args) throws Exception {
        System.out.println("================================================================================");
        System.out.println("CITY NAME MULTILINGUAL ANALYSIS");
        System.out.println("================================================================================");
        System.out.println("Identifying capital cities and their native names for potential alias mapping");
        System.out.println();

        var inputStream = getLocation4JDataset();
        var jsonString = new String(inputStream.readAllBytes());
        var modifiedJson = fixJsonPropertyNames(jsonString);

        var mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<>() {});

        analyzeCapitalCities(countries);
        analyzeMajorCities(countries);
        generateAliasRecommendations(countries);
    }

    /**
     * Analyze capital cities to identify naming differences
     */
    private static void analyzeCapitalCities(List<Country> countries) {
        System.out.println("\n[1] CAPITAL CITIES ANALYSIS");
        System.out.println("─".repeat(80));
        System.out.println();

        Map<String, String> capitalNameInDataset = new HashMap<>();
        Map<String, String> capitalNameExpected = new HashMap<>();
        Map<String, String> capitalWikiDataIds = new HashMap<>();

        for (Country country : countries) {
            String capitalName = country.getCapital();
            if (capitalName == null || capitalName.isEmpty()) {
                continue;
            }

            // Find the capital city in the dataset
            City foundCapital = null;
            for (var state : country.getStates()) {
                for (var city : state.getCities()) {
                    if (city.getName().equalsIgnoreCase(capitalName)) {
                        foundCapital = city;
                        break;
                    }
                }
                if (foundCapital != null) break;
            }

            if (foundCapital != null) {
                capitalNameInDataset.put(country.getName(), foundCapital.getName());
                capitalWikiDataIds.put(country.getName(), foundCapital.getWikiDataId());
            }
            capitalNameExpected.put(country.getName(), capitalName);
        }

        System.out.println("Sample of Capital Cities (first 30):");
        System.out.println();
        capitalNameExpected.entrySet().stream()
            .limit(30)
            .forEach(entry -> {
                String countryName = entry.getKey();
                String expectedCapital = entry.getValue();
                String foundCapital = capitalNameInDataset.get(countryName);
                String wikiDataId = capitalWikiDataIds.get(countryName);

                String status = foundCapital != null ? "✓" : "✗";
                System.out.printf("%s %-25s → %-30s [WikiData: %s]%n",
                    status, countryName, expectedCapital, wikiDataId != null ? wikiDataId : "N/A");
            });

        long missingCount = capitalNameExpected.size() - capitalNameInDataset.size();
        System.out.println();
        System.out.printf("Total countries: %d, Capitals found: %d, Missing: %d%n",
            capitalNameExpected.size(), capitalNameInDataset.size(), missingCount);
    }

    /**
     * Analyze cities by population/importance to find multilingual naming issues
     */
    private static void analyzeMajorCities(List<Country> countries) {
        System.out.println("\n\n[2] MAJOR CITIES WITH WIKIDATA IDs");
        System.out.println("─".repeat(80));
        System.out.println("Cities with WikiData IDs are more likely to be major cities");
        System.out.println();

        List<City> citiesWithWikiData = countries.stream()
            .flatMap(country -> country.getStates().stream())
            .flatMap(state -> state.getCities().stream())
            .filter(city -> city.getWikiDataId() != null && !city.getWikiDataId().isEmpty())
            .limit(50)
            .collect(Collectors.toList());

        System.out.println("Sample of cities with WikiData IDs (first 50):");
        System.out.println();
        citiesWithWikiData.forEach(city ->
            System.out.printf("%-30s | %-20s | %-15s | WikiData: %s%n",
                city.getName(),
                city.getCountry().getName(),
                city.getState().getName(),
                city.getWikiDataId())
        );
    }

    /**
     * Generate recommendations for alias mappings based on common English names
     */
    private static void generateAliasRecommendations(List<Country> countries) {
        System.out.println("\n\n[3] RECOMMENDED ALIAS MAPPINGS");
        System.out.println("─".repeat(80));
        System.out.println("Common English names that should map to cities in the dataset");
        System.out.println();

        Map<String, String> recommendedAliases = new HashMap<>();

        // Common problematic cities (English name → what to search for in dataset)
        String[][] knownIssues = {
            {"Mexico City", "Ciudad de Mexico", "Mexico"},
            {"Munich", "Munchen", "Germany"},
            {"Vienna", "Wien", "Austria"},
            {"Prague", "Praha", "Czech Republic"},
            {"The Hague", "Den Haag", "Netherlands"},
            {"Brussels", "Bruxelles", "Belgium"},
            {"Moscow", "Moskva", "Russia"},
            {"Warsaw", "Warszawa", "Poland"},
            {"Athens", "Athina", "Greece"},
            {"Copenhagen", "Kobenhavn", "Denmark"},
            {"Lisbon", "Lisboa", "Portugal"},
            {"Rome", "Roma", "Italy"},
            {"Bucharest", "Bucuresti", "Romania"},
            {"Beijing", "Beijing", "China"},
            {"Peking", "Beijing", "China"}
        };

        System.out.println("Checking if these cities exist in the dataset:");
        System.out.println();

        for (String[] alias : knownIssues) {
            String englishName = alias[0];
            String nativeName = alias[1];
            String countryName = alias[2];

            boolean englishFound = findCityInDataset(countries, englishName) != null;
            boolean nativeFound = findCityInDataset(countries, nativeName) != null;

            String status;
            if (!englishFound && nativeFound) {
                status = "⚠ NEEDS ALIAS";
                recommendedAliases.put(englishName, nativeName);
            } else if (englishFound && nativeFound) {
                status = "✓ BOTH EXIST";
            } else if (englishFound && !nativeFound) {
                status = "✓ ENGLISH ONLY";
            } else {
                status = "✗ MISSING BOTH";
            }

            System.out.printf("%s | %-20s → %-20s (in %s)%n",
                status, englishName, nativeName, countryName);
        }

        System.out.println();
        System.out.println("─".repeat(80));
        System.out.println("ALIAS MAPPINGS TO ADD:");
        System.out.println();
        recommendedAliases.forEach((english, native_) ->
            System.out.printf("Map.entry(\"%s\", \"%s\"),%n", english, native_)
        );
    }

    /**
     * Helper to find a city by name (case-insensitive)
     */
    private static City findCityInDataset(List<Country> countries, String cityName) {
        return countries.stream()
            .flatMap(country -> country.getStates().stream())
            .flatMap(state -> state.getCities().stream())
            .filter(city -> city.getName().equalsIgnoreCase(cityName))
            .findFirst()
            .orElse(null);
    }
}

