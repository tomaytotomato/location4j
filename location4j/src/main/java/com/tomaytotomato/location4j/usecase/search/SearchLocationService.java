package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.aliases.LocationAliases;
import com.tomaytotomato.location4j.loader.DataLoader;
import com.tomaytotomato.location4j.loader.DefaultDataLoader;
import com.tomaytotomato.location4j.mapper.DefaultSearchLocationResultMapper;
import com.tomaytotomato.location4j.mapper.SearchLocationResultMapper;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import com.tomaytotomato.location4j.model.search.*;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.location4j.text.tokeniser.TextTokeniser;

import java.util.*;
import java.util.logging.Logger;

public class SearchLocationService implements SearchLocation {

    private static final int COUNTRY_BASE_SCORE = 10;
    public static final int STATE_BASE_SCORE = 50;
    public static final int STATE_PUNISHMENT_SCORE = STATE_BASE_SCORE + 25;
    public static final int STATE_REWARD_SCORE = 50;
    public static final int CITY_BASE_SCORE = 100;
    public static final int CITY_PUNISHMENT_SCORE = 100;
    public static final int CITY_REWARD_SCORE = 50;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Map<String, Country> countryNameToCountryMap;
    private final Map<String, Country> countryNativeNameToCountryMap;
    private final Map<String, Country> iso2CodeToCountryMap;
    private final Map<String, Country> iso3CodeToCountryMap;

    private final Map<String, List<State>> stateNameToStatesMap;
    private final Map<String, List<State>> stateNativeNameToStateMap;
    private final Map<String, List<State>> stateCodeToStatesMap;

    private final Map<String, List<City>> cityNameToCitiesMap;

    private final TextTokeniser textTokeniser;
    private final TextNormaliser textNormaliser;
    private final SearchLocationResultMapper searchLocationResultMapper;
    private final LocationAliases locationAliases;

    protected SearchLocationService(TextTokeniser textTokeniser, TextNormaliser textNormaliser, SearchLocationResultMapper searchLocationResultMapper, DataLoader dataLoader, LocationAliases locationAliases) {
        this.textTokeniser = textTokeniser;
        this.textNormaliser = textNormaliser;
        this.searchLocationResultMapper = searchLocationResultMapper;
        this.locationAliases = locationAliases;

        var location4JData = dataLoader.getLocation4JData();
        this.countryNameToCountryMap = new HashMap<>(location4JData.getCountryNameToCountryMap());
        this.countryNativeNameToCountryMap = new HashMap<>(location4JData.getCountryNativeNameToCountryMap());
        this.iso2CodeToCountryMap = new HashMap<>(location4JData.getIso2CodeToCountryMap());
        this.iso3CodeToCountryMap = new HashMap<>(location4JData.getIso3CodeToCountryMap());
        this.stateNameToStatesMap = new HashMap<>(location4JData.getStateNameToStatesMap());
        this.stateNativeNameToStateMap = new HashMap<>(location4JData.getStateNativeNameToStateMap());
        this.stateCodeToStatesMap = new HashMap<>(location4JData.getStateIso2CodeToStateMap());
        this.cityNameToCitiesMap = new HashMap<>(location4JData.getCityNameToCitiesMap());

        addAliases();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Adds custom aliases for lookups for country, state and city..
     */
    private void addAliases() {

        logger.info("Adding aliases for location lookups");

        locationAliases.getCountryNameAliases().forEach((alias, originalKey) -> {
            var country = countryNameToCountryMap.get(keyMaker(originalKey));
            countryNameToCountryMap.put(keyMaker(alias), country);
        });

        locationAliases.getCountryIso2Aliases().forEach((alias, originalKey) -> {
            var country = iso2CodeToCountryMap.get(keyMaker(originalKey));
            countryNameToCountryMap.put(keyMaker(alias), country);
        });

        locationAliases.getCountryIso3Aliases().forEach((alias, originalKey) -> {
            var country = iso3CodeToCountryMap.get(keyMaker(originalKey));
            countryNameToCountryMap.put(keyMaker(alias), country);
        });

        locationAliases.getStateNameAliases().forEach((alias, originalKey) -> {
            var states = stateNameToStatesMap.get(keyMaker(originalKey));
            stateNameToStatesMap.put(alias, states);
        });

        locationAliases.getCityNameAliases().forEach((alias, originalKey) -> {
            var cities = cityNameToCitiesMap.get(keyMaker(originalKey));
            cityNameToCitiesMap.put(alias, cities);
        });
    }


    /**
     * Normalizes a key for consistent lookup.
     *
     * @param key The key to be normalized.
     * @return The normalized key.
     */
    private String keyMaker(String key) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        return textNormaliser.normalise(key);
    }

    @Override
    public List<SearchLocationResult> search(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("SearchLocation Text cannot be null or empty");
        } else if (text.length() < 2) {
            return List.of();
        }

        text = textNormaliser.normalise(text);

        var directMatches = findDirectMatches(text);
        if (!directMatches.isEmpty()) {
            return directMatches;
        }

        return findTokenizedMatches(textTokeniser.tokenise(text));
    }

    @Override
    public List<SearchLocationResult> search(String text, Class<? extends SearchLocationResult> resultType) {
        return search(text)
                .stream()
                .filter(resultType::isInstance)
                .toList();
    }

    /**
     * Attempts to find direct matches for the given normalized search text.
     *
     * @param text the normalized search text
     * @return a list of search location results for direct matches
     * Note: results size will be 0 or 1 for countries, states, and cities respectively
     */
    private List<SearchLocationResult> findDirectMatches(String text) {
        List<SearchLocationResult> matches = new ArrayList<>();

        if (countryNameToCountryMap.containsKey(text)) {
            matches.add(searchLocationResultMapper.toCountryResult(countryNameToCountryMap.get(text)));
            return matches;
        }

        if (text.length() == 3 && iso3CodeToCountryMap.containsKey(text)) {
            matches.add(searchLocationResultMapper.toCountryResult(iso3CodeToCountryMap.get(text)));
            return matches;
        }

        if (text.length() == 2) {
            if (iso2CodeToCountryMap.containsKey(text)) {
                matches.add(searchLocationResultMapper.toCountryResult(iso2CodeToCountryMap.get(text)));
            }
            if (stateCodeToStatesMap.containsKey(text)) {
                stateCodeToStatesMap.get(text).forEach(state -> matches.add(searchLocationResultMapper.toStateResult(state)));
            }
            return matches;
        }

        if (stateNameToStatesMap.containsKey(text)) {
            stateNameToStatesMap.get(text).forEach(state -> matches.add(searchLocationResultMapper.toStateResult(state)));
            return matches;
        }

        if (cityNameToCitiesMap.containsKey(text)) {
            cityNameToCitiesMap.get(text).forEach(city -> matches.add(searchLocationResultMapper.toCityResult(city)));
            return matches;
        }

        return matches;
    }

    /**
     * Finds all matches for countries, states, and cities from the tokenized text.
     * Then builds and prioritizes search results based on hierarchical scoring.
     * @param tokenizedText tokenized search text
     * @return list of prioritized search location results
     */
    private List<SearchLocationResult> findTokenizedMatches(List<String> tokenizedText) {

        var countryMatches = findAllCountryMatches(tokenizedText);
        var stateMatches = findAllStateMatches(tokenizedText);
        var cityMatches = findAllCityMatches(tokenizedText);

        return buildSearchResults(countryMatches, stateMatches, cityMatches, tokenizedText);
    }

    /**
     * Builds and prioritizes search results based on hierarchical scoring.
     * All countries are given a baseline score as they are at the top of the hierarchy.
     * States are scored higher if their parent country is also matched.
     * Cities are scored highest if both their parent state and country are matched.
     * @param countryMatches list of matched countries
     * @param stateMatches list of matched states
     * @param cityMatches list of matched cities
     * @param tokenizedText the tokenized search text
     * @return list of prioritized search location results
     */
    private List<SearchLocationResult> buildSearchResults(List<CountryResult> countryMatches, List<StateResult> stateMatches, List<CityResult> cityMatches, List<String> tokenizedText) {

        if (countryMatches.isEmpty() && stateMatches.isEmpty() && cityMatches.isEmpty()) {
            return List.of();
        }

        List<ScoredResult> scoredResults = new ArrayList<>();

        for (CityResult city : cityMatches) {
            int score = calculateCityScore(city, countryMatches, stateMatches);
            scoredResults.add(new ScoredResult(city, score));
        }

        for (StateResult state : stateMatches) {
            int score = calculateStateScore(state, countryMatches);
            scoredResults.add(new ScoredResult(state, score));
        }

        for (CountryResult country : countryMatches) {
            int score = calculateCountryScore(country);
            scoredResults.add(new ScoredResult(country, score));
        }

        scoredResults.sort((a, b) -> {
            int scoreCompare = Integer.compare(b.score(), a.score());
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            return Integer.compare(getSpecificityLevel(b.result()), getSpecificityLevel(a.result()));
        });

        var searchResults = new ArrayList<SearchLocationResult>();
        if (!scoredResults.isEmpty()) {
            searchResults.add(scoredResults.get(0).result());
        }

        return searchResults;
    }

    private int calculateCountryScore(CountryResult country) {
        return COUNTRY_BASE_SCORE;
    }

    private int calculateStateScore(StateResult state, List<CountryResult> countryMatches) {
        var score = STATE_BASE_SCORE;

        if (countryMatches.isEmpty()) {
            return score;
        }

        var stateCountryMatched = countryMatches.stream().anyMatch(country -> country.id().equals(state.country().id()));

        if (stateCountryMatched) {
            score += STATE_REWARD_SCORE;
        } else {
            score -= STATE_PUNISHMENT_SCORE;
        }
        return score;
    }

    private int calculateCityScore(CityResult city, List<CountryResult> countryMatches, List<StateResult> stateMatches) {
        int score = CITY_BASE_SCORE;

        boolean cityStateMatched = stateMatches.stream().anyMatch(state -> state.id().equals(city.state().id()));

        if (cityStateMatched) {
            score += CITY_REWARD_SCORE;
        } else {
            score -= CITY_PUNISHMENT_SCORE;
        }

        boolean cityCountryMatched = countryMatches.stream().anyMatch(country -> country.id().equals(city.country().id()));

        if (cityCountryMatched) {
            score += CITY_REWARD_SCORE;
        } else {
            score -= CITY_PUNISHMENT_SCORE;
        }

        return score;
    }

    private int getSpecificityLevel(SearchLocationResult result) {
        return switch (result) {
            case CityResult _ -> 3;
            case StateResult _ -> 2;
            case CountryResult _ -> 1;
        };
    }

    private List<CityResult> findAllCityMatches(List<String> tokenizedText) {
        var cityMatches = new ArrayList<CityResult>();
        for (String token : tokenizedText) {
            if (cityNameToCitiesMap.containsKey(token)) {
                cityNameToCitiesMap.get(token).forEach(city -> cityMatches.add(searchLocationResultMapper.toCityResult(city)));
            }
        }
        return cityMatches;
    }

    private List<StateResult> findAllStateMatches(List<String> tokenizedText) {
        var stateMatches = new ArrayList<StateResult>();
        for (String token : tokenizedText) {
            if (stateNameToStatesMap.containsKey(token)) {
                stateNameToStatesMap.get(token).forEach(state -> stateMatches.add(searchLocationResultMapper.toStateResult(state)));
            } else if (stateNativeNameToStateMap.containsKey(token)) {
                stateNativeNameToStateMap.get(token).forEach(state -> stateMatches.add(searchLocationResultMapper.toStateResult(state)));
            } else if (stateCodeToStatesMap.containsKey(token)) {
                stateCodeToStatesMap.get(token).forEach(state -> stateMatches.add(searchLocationResultMapper.toStateResult(state)));
            }
        }
        return stateMatches;
    }

    private List<CountryResult> findAllCountryMatches(List<String> tokenizedText) {
        var countryMatches = new ArrayList<CountryResult>();
        for (String token : tokenizedText) {
            if (countryNameToCountryMap.containsKey(token)) {
                var country = countryNameToCountryMap.get(token);
                countryMatches.add(searchLocationResultMapper.toCountryResult(country));
            } else if (countryNativeNameToCountryMap.containsKey(token)) {
                var country = countryNativeNameToCountryMap.get(token);
                countryMatches.add(searchLocationResultMapper.toCountryResult(country));
            } else if (iso3CodeToCountryMap.containsKey(token)) {
                var country = iso3CodeToCountryMap.get(token);
                countryMatches.add(searchLocationResultMapper.toCountryResult(country));
            } else if (iso2CodeToCountryMap.containsKey(token)) {
                var country = iso2CodeToCountryMap.get(token);
                countryMatches.add(searchLocationResultMapper.toCountryResult(country));
            }
        }
        return countryMatches;
    }

    public static class Builder {

        private TextTokeniser textTokeniser = new DefaultTextTokeniser();
        private TextNormaliser textNormaliser = new DefaultTextNormaliser();
        private SearchLocationResultMapper searchLocationResultMapper = new DefaultSearchLocationResultMapper();
        private LocationAliases locationAliases = new DefaultLocationAliases();
        private DataLoader dataLoader = new DefaultDataLoader();

        Builder() {
        }

        public Builder withTextTokeniser(TextTokeniser textTokeniser) {
            this.textTokeniser = textTokeniser;
            return this;
        }

        public Builder withTextNormaliser(TextNormaliser textNormaliser) {
            this.textNormaliser = textNormaliser;
            return this;
        }

        public Builder withLocationMapper(SearchLocationResultMapper searchLocationResultMapper) {
            this.searchLocationResultMapper = searchLocationResultMapper;
            return this;
        }

        public Builder withLocationAliases(LocationAliases locationAliases) {
            this.locationAliases = locationAliases;
            return this;
        }

        public Builder withDataLoader(DataLoader dataLoader) {
            this.dataLoader = dataLoader;
            return this;
        }

        public SearchLocationService build() {
            return new SearchLocationService(textTokeniser, textNormaliser, searchLocationResultMapper, dataLoader, locationAliases);
        }
    }
}
