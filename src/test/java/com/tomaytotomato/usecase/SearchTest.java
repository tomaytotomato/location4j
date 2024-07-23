package com.tomaytotomato.usecase;

import com.tomaytotomato.LocationSearchService;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.mapper.DefaultLocationMapper;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.text.tokeniser.DefaultTextTokeniser;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchTest {

    private final Search searchService;

    public SearchTest() throws IOException {
        var textNormaliser = new DefaultTextNormaliser();
        var textTokeniser = new DefaultTextTokeniser();
        var locationMapper = new DefaultLocationMapper();
        var dataLoader = new DefaultCountriesDataLoaderImpl();

        searchService = new LocationSearchService(textTokeniser, textNormaliser, locationMapper, dataLoader);
    }

    @Description("Search, when null or empty text, then throw exception")
    @Test
    void search_WhenNullOrBlank_ThenThrowException() {

        // When Then
        assertThatThrownBy(() -> {
            searchService.search(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Search Text cannot be null or empty");
    }

    @Description("Search, when text is too short, then return empty list")
    @Test
    void search_WhenTextIsTooShort_ThenReturnEmptyList() {
        // When
        var result = searchService.search("A");

        // Then
        assertThat(result).isEmpty();
    }

    @Description("Search, when text contains country only, then return country match")
    @ParameterizedTest
    @CsvSource({
            "United Kingdom",
            "Dominican Republic",
            "Russia",
            "United States",
            "United Arab Emirates"
    })
    void search_WhenTextContainsCountryOnly_ThenReturnCountryMatch(String countryName) {

        // When
        var result = searchService.search(countryName);

        // Then
        assertThat(result).isNotEmpty().hasSize(1).extracting("countryName").containsOnly(countryName);
    }

    @Description("Search, when text contains country ISO2, then return country match")
    @ParameterizedTest
    @CsvSource({
            "GB, United Kingdom|Eritrea|Kyrgyzstan|Liberia|Pakistan|Tajikistan",
            "DO, Dominican Republic|Benin|Malawi|Moldova",
            "RU, Russia|Malawi",
            "TR, Turkey|Albania|India|Italy|Romania",
            "UA, Ukraine"
    })
    void search_WhenTextContainsOnlyTwoLetters_ThenReturnMatchesBasedonISO2CodeOrStateCode(String input, String expectedCountryMatches) {

        // When
        var result = searchService.search(input);
        var expectedCountries = expectedCountryMatches.split("\\|");


        // Then
        assertThat(result).isNotEmpty().hasSize(expectedCountries.length)
                .extracting("countryName")
                .containsAll(Arrays.asList(expectedCountries));
    }

    @Description("Search, when text contains country ISO3, then return country match")
    @ParameterizedTest
    @CsvSource({
            "GBR, United Kingdom",
            "USA, United States",
            "CHN, China",
            "TUR, Turkey",
            "UKR, Ukraine"
    })
    void search_WhenTextContainsCountryISO3CodeOnly_ThenReturnCountryMatch(String iso3Code, String expectedCountryName) {

        // When
        var result = searchService.search(iso3Code);

        // Then
        assertThat(result).isNotEmpty().hasSize(1)
                .extracting("countryName", "countryIso3Code")
                .containsOnly(tuple(expectedCountryName, iso3Code));
    }

    @Description("Search, when text contains state and country name, then return single match")
    @ParameterizedTest
    @CsvSource({
            "Santa Clara CA, United States",
            "Glasgow Scotland, United Kingdom",
            "Tel Aviv Israel, Israel",
            "Germany Saxony, Germany",
            "France, France",
            "England, United Kingdom"
    })
    void search_WhenTextContainsStateAndCountryName_ThenReturnSingleMatch(String text, String countryName) {
        // When
        var result = searchService.search(text);

        // Then
        assertThat(result).isNotEmpty().hasSize(1)
                .extracting("countryName")
                .containsOnly(countryName);
    }
}