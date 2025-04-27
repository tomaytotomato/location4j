package com.tomaytotomato.location4j.usecase;

import com.tomaytotomato.location4j.usecase.search.SearchLocationService;
import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.location4j.mapper.DefaultLocationMapper;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.location4j.usecase.search.SearchLocation;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchLocationTest {

  private final SearchLocation searchLocationService;

  public SearchLocationTest() {
    searchLocationService = SearchLocationService.builder()
        .withLocationAliases(new DefaultLocationAliases())
        .withLocationMapper(new DefaultLocationMapper())
        .withCountriesDataLoader(new DefaultCountriesDataLoaderImpl())
        .withTextNormaliser(new DefaultTextNormaliser())
        .withTextTokeniser(new DefaultTextTokeniser())
        .build();
  }

  @DisplayName("SearchLocation, when null or empty text, then throw exception")
  @Test
  void search_WhenNullOrBlank_ThenThrowException() {

    // When Then
    assertThatThrownBy(() -> searchLocationService.search(null)).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessageContaining("SearchLocation Text cannot be null or empty");
  }

  @DisplayName("SearchLocation, when text is too short, then return empty list")
  @Test
  void search_WhenTextIsTooShort_ThenReturnEmptyList() {
    // When
    var result = searchLocationService.search("A");

    // Then
    assertThat(result).isEmpty();
  }

  @DisplayName("SearchLocation, when text contains country only, then return country match")
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
    var result = searchLocationService.search(countryName);

    // Then
    assertThat(result).isNotEmpty().hasSize(1).extracting("countryName").containsOnly(countryName);
  }

  @DisplayName("SearchLocation, when text contains country ISO2, then return country match")
  @ParameterizedTest
  @CsvSource({
      "GB, United Kingdom|Eritrea|Kyrgyzstan|Liberia|Pakistan|Tajikistan",
      "DO, Dominican Republic|Benin|Malawi|Moldova",
      "RU, Russia|Malawi",
      "TR, Turkey|Albania|India|Italy|Romania",
      "UA, Ukraine"
  })
  void search_WhenTextContainsOnlyTwoLetters_ThenReturnMatchesBasedonISO2CodeOrStateCode(
      String input, String expectedCountryMatches) {

    // When
    var result = searchLocationService.search(input);
    var expectedCountries = expectedCountryMatches.split("\\|");

    // Then
    assertThat(result).isNotEmpty().hasSize(expectedCountries.length)
        .extracting("countryName")
        .containsAll(Arrays.asList(expectedCountries));
  }

  @DisplayName("SearchLocation, when text contains country ISO3, then return country match")
  @ParameterizedTest
  @CsvSource({
      "GBR, United Kingdom",
      "USA, United States",
      "CHN, China",
      "TUR, Turkey",
      "UKR, Ukraine"
  })
  void search_WhenTextContainsCountryISO3CodeOnly_ThenReturnCountryMatch(String iso3Code,
      String expectedCountryName) {

    // When
    var result = searchLocationService.search(iso3Code);

    // Then
    assertThat(result).isNotEmpty().hasSize(1)
        .extracting("countryName", "countryIso3Code")
        .containsOnly(tuple(expectedCountryName, iso3Code));
  }

  @DisplayName("SearchLocation, when text contains state and country name, then return single match")
  @ParameterizedTest
  @CsvSource({
      "Santa Clara CA, United States",
      "Glasgow Scotland, United Kingdom",
      "Tel Aviv Israel, Israel",
      "Tel Aviv ISR, Israel",
      "Germany Saxony, Germany",
      "France, France",
      "England, United Kingdom",
      "Gloucester ENG, United Kingdom",
      "Eng Sheffield, United Kingdom",
      "United Kingdom . Sheffield, United Kingdom",
      "Tacoma United States, United States",
      "Tacoma, United States",
      "USA Tacoma, United States",
  })
  void search_WhenTextContainsStateAndCountryName_ThenReturnSingleMatch(String text,
      String countryName) {
    // When
    var result = searchLocationService.search(text);

    // Then
    assertThat(result).isNotEmpty().hasSize(1)
        .extracting("countryName")
        .containsOnly(countryName);
  }


  @Test
  void search_WhenTextContainsCountryStateandCityName_ThenReturnSingleMatch() {

    // When Then
    assertThat(searchLocationService.search("San Francisco, CA, USA")).isNotEmpty().hasSize(1)
        .extracting("countryName", "countryIso2Code", "countryIso3Code", "stateName", "city")
        .containsExactly(tuple("United States", "US", "USA", "California", "San Francisco"));

    assertThat(searchLocationService.search("KY, Glasgow USA")).isNotEmpty().hasSize(1)
        .extracting("countryName", "countryIso2Code", "countryIso3Code", "stateName", "city")
        .containsExactly(tuple("United States", "US", "USA", "Kentucky", "Glasgow"));

    assertThat(searchLocationService.search("Glasgow Kentucky USA")).isNotEmpty().hasSize(1)
        .extracting("countryName", "countryIso2Code", "countryIso3Code", "stateName", "city")
        .containsExactly(tuple("United States", "US", "USA", "Kentucky", "Glasgow"));

    assertThat(searchLocationService.search("Glasgow Scotland")).isNotEmpty().hasSize(1)
        .extracting("countryName", "countryIso2Code", "countryIso3Code", "stateName", "city")
        .containsExactly(tuple("United Kingdom", "GB", "GBR", "Scotland", "Glasgow"));
  }
}