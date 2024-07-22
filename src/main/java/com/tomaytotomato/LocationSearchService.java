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
                    city.setStateCode(state.getStateCode());
                    cityNameToCitiesMap.computeIfAbsent(keyMaker(city.getName()), k -> new ArrayList<>()).add(city);
                    cityIdToCityMap.put(city.getId(), city);
                });
            });
        });
        specialMappings();
    }

    private void specialMappings() {

        // Add special mappings for Scotland, England, Northern Ireland, and Wales
        Country ukCountry = countryNameToCountryMap.get(keyMaker("United Kingdom"));
        if (ukCountry != null) {
            countryNameToCountryMap.put(keyMaker("Scotland"), ukCountry);
            countryNameToCountryMap.put(keyMaker("England"), ukCountry);
            countryNameToCountryMap.put(keyMaker("Northern Ireland"), ukCountry);
            countryNameToCountryMap.put(keyMaker("Wales"), ukCountry);
            iso2CodeToCountryMap.put("uk", ukCountry);
        } else {
            logger.warning("United Kingdom not found in the country map, unable to add special mappings for Scotland, England, Northern Ireland, and Wales.");
        }
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


        //try and direct match
        if (countryNameToCountryMap.containsKey(text)) {
            var country = countryNameToCountryMap.get(text);
            return List.of(locationMapper.toLocation(country));
        }

        if (text.length() == 3 && iso3CodeToCountryMap.containsKey(text)) {
            var country = iso3CodeToCountryMap.get(text);
            return List.of(locationMapper.toLocation(country));
        }

        if (text.length() == 2 && iso2CodeToCountryMap.containsKey(text) || stateCodeToStatesMap.containsKey(text)) {
            var country = locationMapper.toLocation(iso2CodeToCountryMap.get(text));
            var states = new ArrayList<>(stateCodeToStatesMap.get(text).stream().map(locationMapper::toLocation).toList());
            states.add(country);
            return states;
        }

        if(stateNameToStatesMap.containsKey(text)) {
            var states = stateNameToStatesMap.get(text);
            return states.stream().map(locationMapper::toLocation).toList();
        }

        if (cityNameToCitiesMap.containsKey(text)) {
            var cities = cityNameToCitiesMap.get(text);
            return cities.stream().map(locationMapper::toLocation).toList();
        }


        //if no direct match

        var tokenizedText = textTokeniser.tokenise(text);

        Map<String, Integer> countryHitsCount = new HashMap<>();
        Map<String, Integer> stateHitsCount = new HashMap<>();
        Map<String, Integer> cityHitsCount = new HashMap<>();

        var iterator = tokenizedText.iterator();

        var countryFound = false;
        while (iterator.hasNext()) {
            var token = iterator.next();
            if (countryNameToCountryMap.containsKey(token)) {
                //strongest match
                var country = countryNameToCountryMap.get(token);
                countryHitsCount.put(country.getName(), countryHitsCount.getOrDefault(token, 0) + 1);
                countryFound = true;
                iterator.remove();
                break;
            }
            if (iso3CodeToCountryMap.containsKey(token)) {
                var country = iso3CodeToCountryMap.get(token);
                countryHitsCount.put(country.getName(), countryHitsCount.getOrDefault(token, 0) + 1);
                countryFound = true;
                iterator.remove();
                break;
            }
        }

        if (!countryFound) {
            for (String token : tokenizedText) {
                if (stateNameToStatesMap.containsKey(token)) {
                    stateNameToStatesMap.get(token).forEach(state -> {
                        stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
                        countryHitsCount.put(state.getCountryName(), countryHitsCount.getOrDefault(state.getCountryName(), 0) + 1);
                    });
                }
                if (stateCodeToStatesMap.containsKey(token)) {
                    // Recompute country hits count by iterating through each state and getting its country
                    stateCodeToStatesMap.get(token).forEach(state -> {
                        stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
                        countryHitsCount.put(state.getCountryName(), countryHitsCount.getOrDefault(state.getCountryName(), 0) + 1);
                    });
                }
                if (cityNameToCitiesMap.containsKey(token)) {
                    var cities = cityNameToCitiesMap.get(token);

                    cities.forEach(city -> {
                        cityHitsCount.put(city.getName(), cityHitsCount.getOrDefault(city.getName(), 0) + 1);
                        stateHitsCount.put(city.getStateName(), stateHitsCount.getOrDefault(city.getStateName(), 0) + 1);
                        countryHitsCount.put(city.getCountryName(), countryHitsCount.getOrDefault(city.getCountryName(), 0) + 1);
                    });
                }
            }
        } else {
            for (String token : tokenizedText) {
                if (stateNameToStatesMap.containsKey(token)) {
                    stateNameToStatesMap.get(token).stream().filter(state -> {

                        String topCountry = countryHitsCount.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);

                        return state.getCountryName().equals(topCountry);

                    }).forEach(state -> {
                        stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
                    });
                }
                if (stateCodeToStatesMap.containsKey(token)) {
                    // Recompute country hits count by iterating through each state and getting its country
                    stateCodeToStatesMap.get(token).stream().filter(state -> {

                        String topCountry = countryHitsCount.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);

                        return state.getCountryName().equals(topCountry);

                    }).forEach(state -> {
                        stateHitsCount.put(state.getName(), stateHitsCount.getOrDefault(state.getName(), 0) + 1);
                    });
                }
                if (cityNameToCitiesMap.containsKey(token)) {
                    var cities = cityNameToCitiesMap.get(token);

                    cities.stream().filter(state -> {

                        String topCountry = countryHitsCount.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);

                        return state.getCountryName().equals(topCountry);

                    }).forEach(city -> {
                        cityHitsCount.put(city.getName(), cityHitsCount.getOrDefault(city.getName(), 0) + 1);
                        stateHitsCount.put(city.getStateName(), stateHitsCount.getOrDefault(city.getStateName(), 0) + 1);
                    });
                }
            }
        }


        // Get the highest scoring hits
        String topCountry = countryHitsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        String topState = stateHitsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (Objects.isNull(topState)) {
            var country = countryNameToCountryMap.get(topCountry.toLowerCase());
            return List.of(locationMapper.toLocation(country));
        }

        String topCity = cityHitsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Validate the combination of country, state, and city
        if (topCity != null && topState != null && topCountry != null) {
            var cityMatches = cityNameToCitiesMap.get(topCity.toLowerCase());

            for (City city : cityMatches) {
                if (city.getStateName().equals(topState) && city.getCountryName().equals(topCountry)) {
                    return List.of(locationMapper.toLocation(city));
                }
            }
        }

        // If no valid combination is found, return an empty list
        return List.of();
    }
}
