package com.tomaytotomato.location4j.usecase.search;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomaytotomato.location4j.loader.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SearchStateTest extends TestDataLoader {

    private SearchState searchState;

    @BeforeEach
    public void setUp() {
        searchState = getSearchLocationService();
    }

    @ParameterizedTest
    @CsvSource({
            "california",
            "texas",
            "new york",
            "london",
			"westminister",
            "bavaria",
            "florida"})
    void search_WhenTextMatchesStates_ThenReturnStateResults(String searchText) {
        var results = searchState.searchStates(searchText);

        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().name()).isEqualToIgnoringCase(searchText);
    }

}
