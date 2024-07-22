package com.tomaytotomato;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomaytotomato.mapper.LocationMapper;
import com.tomaytotomato.mapper.LocationMapperImpl;
import com.tomaytotomato.model.City;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.model.Location;
import com.tomaytotomato.model.State;
import com.tomaytotomato.usecase.Search;
import com.tomaytotomato.util.TextNormaliser;
import com.tomaytotomato.util.TextTokeniser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class LocationSearchService implements Search {

    private final TextTokeniser textTokeniser;
    private final TextNormaliser textNormaliser;
    /**
     * One-to-one mappings (1:1)
     */
    private final Map<String, Country> countryNameToCountryMap = new HashMap<>();
    private final Map<Integer, Country> countryIdToCountryMap = new HashMap<>();
    private final Map<String, Country> countryNativeNameToCountry = new HashMap<>();
    private final Map<String, Country> iso2CodeToCountryMap = new HashMap<>();
    private final Map<String, Country> iso3CodeToCountryMap = new HashMap<>();
    private final Map<Integer, State> stateIdToStateMap = new HashMap<>();
    private final Map<Integer, City> cityIdToCityMap = new HashMap<>();
    /**
     * One-to-many mappings (1:n)
     */
    private final Map<String, List<State>> stateNameToStatesMap = new HashMap<>();
    private final Map<String, List<State>> stateCodeToStatesMap = new HashMap<>();
    private final Map<String, List<City>> cityNameToCitiesMap = new HashMap<>();

    public LocationSearchService(TextTokeniser textTokeniser, TextNormaliser textNormaliser) {
        this.textTokeniser = textTokeniser;
        this.textNormaliser = textNormaliser;
        init();
    }

    private static final String FILENAME = "location4j-countries.json";
    private List<Country> countries;

    private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName() + this.getClass().getName());

    private LocationMapper locationMapper = new LocationMapperImpl();

    private void init() {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILENAME)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + FILENAME);
            }
            parseJsonFile(inputStream);
        } catch (IOException e) {
            logger.severe("Failed to load countries file: " + e.getMessage());
        }

        countries.forEach(country -> {
            countryIdToCountryMap.put(country.getId(), country);
            countryNameToCountryMap.put(keyMaker(country.getName()), country);
            if (Objects.isNull(country.getNativeName()) || country.getNativeName().isEmpty()) {
                logger.warning("Country has null native name, skipping mapping: " + country.getName());
            } else {
                countryNativeNameToCountry.put(keyMaker(country.getNativeName()), country);
            }
            iso2CodeToCountryMap.put(keyMaker(country.getIso2()), country);
            iso3CodeToCountryMap.put(keyMaker(country.getIso3()), country);

            country.getStates().forEach(state -> {

                state.setCountryId(country.getId());
                state.setCountryName(country.getName());
                state.setCountryIso2Code(country.getIso2());
                state.setCountryIso3Code(country.getIso3());
                stateIdToStateMap.put(state.getId(), state);
                stateNameToStatesMap.computeIfAbsent(keyMaker(state.getName()), k -> new ArrayList<>()).add(state);
                stateCodeToStatesMap.computeIfAbsent(keyMaker(state.getStateCode()), k -> new ArrayList<>()).add(state);

                state.getCities().forEach(city -> {
                    city.setCountryId(country.getId());
                    city.setCountryName(country.getName());
                    city.setCountryIso2Code(country.getIso2());
                    city.setCountryIso3Code(country.getIso3());
                    city.setStateId(state.getId());
                    city.setStateName(state.getName());
                    cityNameToCitiesMap.computeIfAbsent(keyMaker(city.getName()), k -> new ArrayList<>()).add(city);
                    cityIdToCityMap.put(city.getId(), city);
                });
            });
        });
        specialMappings();
    }

    private void specialMappings() {
        iso2CodeToCountryMap.put("uk", iso2CodeToCountryMap.get("gb"));
    }


    private void parseJsonFile(InputStream inputStream) {
        var objectMapper = new ObjectMapper();
        try {
            countries = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            logger.info("Successfully parsed countries file");
        } catch (IOException e) {
            logger.severe("Failed to read countries file: " + e.getMessage());
        }
    }

    private String keyMaker(String key) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        return textNormaliser.normalise(key);
    }

    @Override
    public List<Location> search(String text) {

        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("Search Text cannot be null or empty");
        }

        if (text.length() < 2) {
            return List.of();
        }

        text = textNormaliser.normalise(text);
        var tokenHits = directMatchTextCountry(text);

        if (tokenHits.isEmpty()) {
            tokenHits = directMatchTextState(text);
        }
        if (tokenHits.isEmpty()) {
            tokenHits = directMatchTextCity(text);
        }

        if (tokenHits.isEmpty()) {
            // time to tokenize
            var tokenizedText = textTokeniser.tokenise(text);
            var locations = recursiveMatcher(new ArrayList<>(), tokenizedText);
            if (!locations.isEmpty()) {
                tokenHits.put("location", locations);
            }
        }

        return tokenHits.values().stream().flatMap(List::stream).toList();
    }


    private Map<String, Integer> countryHitsCount = new HashMap<>();
    private Map<String, Integer> stateHitsCount = new HashMap<>();
    private Map<String, Integer> cityHitsCount = new HashMap<>();

    private List<Location> recursiveMatcher(List<Location> locations, List<String> tokens) {
        if (tokens.isEmpty()) {
            return locations;
        }

        String token = tokens.get(0);
        List<String> remainingTokens = tokens.subList(1, tokens.size());


        // Try to match the token against a country
        if (countryNameToCountryMap.containsKey(token)) {
            if (!locations.isEmpty()) {
                var country = countryNameToCountryMap.get(token);

                var filteredMatches = locations.stream().filter(location -> {
                    return location.getCountryName().equals(country.getName());
                }).toList();
                recursiveMatcher(filteredMatches, remainingTokens);

            } else {
                var matchedCountries = List.of(countryNameToCountryMap.get(token)).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                locations.addAll(matchedCountries);
                recursiveMatcher(matchedCountries, remainingTokens);
            }
        }

        // Try to match the token against a country
        if (iso2CodeToCountryMap.containsKey(token)) {
            if (!locations.isEmpty()) {
                var country = iso2CodeToCountryMap.get(token);

                var filteredMatches = locations.stream().filter(location -> {
                    return location.getCountryIso2Code().equals(country.getIso2());
                }).toList();
                recursiveMatcher(filteredMatches, remainingTokens);
            } else {
                var matchedCountries = List.of(iso2CodeToCountryMap.get(token)).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCountries, remainingTokens);
            }
        }

        if (iso3CodeToCountryMap.containsKey(token)) {
            if (!locations.isEmpty()) {
                var country = iso3CodeToCountryMap.get(token);

                var filteredMatches = locations.stream().filter(location -> {
                    return location.getCountryIso3Code().equals(country.getIso3());
                }).toList();
                recursiveMatcher(filteredMatches, remainingTokens);
            } else {
                var matchedCountries = List.of(iso3CodeToCountryMap.get(token)).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCountries, remainingTokens);
            }
        }

        // Try to match the token against a city
        if (cityNameToCitiesMap.containsKey(token)) {
            if (!locations.isEmpty()) {

                var citiesMatches = cityNameToCitiesMap.get(token);

                var countriesMatched = locations.stream().map(Location::getCountryName).toList();
                var statesMatches = locations.stream().map(Location::getState).toList();

                var citiesFiltered = citiesMatches.stream().filter(city -> {
                    var country = countryIdToCountryMap.get(city.getCountryId());
                    return countriesMatched.contains(country.getName());
                }).filter(city -> {
                    var state = stateIdToStateMap.get(city.getStateId());
                    return countriesMatched.contains(state.getName());
                }).toList();
                var matchedCities = citiesFiltered.stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCities, remainingTokens);
            } else {

                var matchedCities = cityNameToCitiesMap.get(token).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCities, remainingTokens);
            }
        }

        // Try to match the token against a state
        if (stateCodeToStatesMap.containsKey(token)) {

            if (!locations.isEmpty()) {
                var statesMatched = stateCodeToStatesMap.get(token);

                var countriesMatched = locations.stream().map(Location::getCountryName).toList();

                var citiesFiltered = statesMatched.stream().filter(state -> {
                    var country = countryIdToCountryMap.get(state.getCountryId());
                    return countriesMatched.contains(country.getName());
                }).toList();
                var matchedCities = citiesFiltered.stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCities, remainingTokens);

            } else {
                var matchedStates = stateNameToStatesMap.get(token).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedStates, remainingTokens);
            }
        }

        // Try to match the token against a state
        if (stateNameToStatesMap.containsKey(token)) {

            if (!locations.isEmpty()) {

                var statesMatched = stateNameToStatesMap.get(token);

                var countriesMatched = locations.stream().map(Location::getCountryName).toList();

                var citiesFiltered = statesMatched.stream().filter(state -> {
                    var country = countryIdToCountryMap.get(state.getCountryId());
                    return countriesMatched.contains(country.getName());
                }).toList();
                var matchedCities = citiesFiltered.stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedCities, remainingTokens);
            } else {
                var matchedStates = stateNameToStatesMap.get(token).stream()
                        .map(locationMapper::toLocation)
                        .toList();
                recursiveMatcher(matchedStates, remainingTokens);
            }
        }

        // If no match is found, continue with the remaining tokens
        return recursiveMatcher(locations, remainingTokens);
    }

    private Map<String, List<Location>> directMatchTextCity(String text) {
        var matches = new HashMap<String, List<Location>>();

        if (cityNameToCitiesMap.containsKey(text)) {
            var cityLocations = cityNameToCitiesMap.get(text).stream().map(locationMapper::toLocation).toList();
            matches.put("city", cityLocations);
        }
        return matches;
    }

    private Map<String, List<Location>> directMatchTextState(String text) {
        var matches = new HashMap<String, List<Location>>();

        if (stateNameToStatesMap.containsKey(text)) {
            var stateLocations = stateNameToStatesMap.get(text).stream().map(locationMapper::toLocation).toList();
            matches.put("state", stateLocations);
        }
        if (stateCodeToStatesMap.containsKey(text)) {
            var stateLocations = stateCodeToStatesMap.get(text).stream().map(locationMapper::toLocation).toList();
            matches.put("state", stateLocations);
        }
        return matches;
    }

    private Map<String, List<Location>> directMatchTextCountry(String text) {
        var matches = new HashMap<String, List<Location>>();

        if (countryNameToCountryMap.containsKey(text)) {
            var country = countryNameToCountryMap.get(text);
            matches.put("country", List.of(locationMapper.toLocation(country)));
        }
        if (text.length() == 3 && iso3CodeToCountryMap.containsKey(text)) {
            var country = iso3CodeToCountryMap.get(text);
            matches.put("country", List.of(locationMapper.toLocation(country)));
        }
        if (text.length() == 2 && iso2CodeToCountryMap.containsKey(text)) {
            var country = iso2CodeToCountryMap.get(text);
            matches.put("country", List.of(locationMapper.toLocation(country)));
        }
        return matches;
    }
}
