package com.tomaytotomato.location4j.usecase.search;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.location4j.mapper.DefaultSearchLocationResultMapper;
import com.tomaytotomato.location4j.model.search.CityResult;
import com.tomaytotomato.location4j.model.search.CountryResult;
import com.tomaytotomato.location4j.model.search.SearchLocationResult;
import com.tomaytotomato.location4j.model.search.StateResult;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.DefaultTextTokeniser;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        .withLocationMapper(new DefaultSearchLocationResultMapper())
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
    assertThat(result).isNotEmpty().hasSize(1);
    assertThat(result.getFirst()).isInstanceOf(CountryResult.class);

    var countryResult = (CountryResult) result.getFirst();
    assertThat(countryResult.getCountry().getName()).isEqualTo(countryName);
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
    assertThat(result).isNotEmpty().hasSize(expectedCountries.length);

    List<String> countryNames = result.stream()
        .map(SearchLocationResult::getCountry)
        .map(CountryResult::getName)
        .collect(Collectors.toList());

    assertThat(countryNames).containsAll(Arrays.asList(expectedCountries));
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
    assertThat(result).isNotEmpty().hasSize(1);
    assertThat(result.getFirst()).isInstanceOf(CountryResult.class);

    CountryResult countryResult = (CountryResult) result.getFirst();
    assertThat(countryResult.getCountry().getName()).isEqualTo(expectedCountryName);
    assertThat(countryResult.getCountry().iso3()).isEqualTo(iso3Code);
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
    assertThat(result).isNotEmpty().hasSize(1);
    assertThat(result.getFirst().getCountry().getName()).isEqualTo(countryName);
  }

  @ParameterizedTest
  @DisplayName("When searching with {0}, then returns a CityResult with expected data")
  @CsvSource(delimiter = '|', value = {
      "San Francisco, CA, USA|United States|US|USA|California|San Francisco",
      "KY, Glasgow USA|United States|US|USA|Kentucky|Glasgow",
      "Glasgow Kentucky USA|United States|US|USA|Kentucky|Glasgow",
      "Glasgow Scotland|United Kingdom|GB|GBR|Scotland|Glasgow"
  })
  void search_WithCombinedLocationText_ReturnsCityResult(
      String searchText,
      String expectedCountry,
      String expectedIso2,
      String expectedIso3,
      String expectedState,
      String expectedCity) {

    // When
    var results = searchLocationService.search(searchText);

    // Then
    assertThat(results).isNotEmpty().hasSize(1);
    assertThat(results.getFirst()).isInstanceOf(CityResult.class);

    CityResult cityResult = (CityResult) results.getFirst();
    assertThat(cityResult.getCountry().getName()).isEqualTo(expectedCountry);
    assertThat(cityResult.getCountry().iso2()).isEqualTo(expectedIso2);
    assertThat(cityResult.getCountry().iso3()).isEqualTo(expectedIso3);
    assertThat(cityResult.getState().getName()).isEqualTo(expectedState);
    assertThat(cityResult.getName()).isEqualTo(expectedCity);
  }

  @ParameterizedTest
  @DisplayName("Pattern matching with sealed types produces correct descriptions")
  @CsvSource(delimiter = '|', value = {
      "San Francisco, CA, USA|City: San Francisco, California, United States",
      "CA, United States|State: California, United States",
      "United States|Country: United States"
  })
  void patternMatchingWithDifferentLocationTypes(String searchText, String expectedDescription) {
    var result = searchLocationService.search(searchText).getFirst();

    var description = switch (result) {
      case CountryResult countryResult -> "Country: " + countryResult.getCountry().getName();
      case StateResult stateResult ->
          "State: " + stateResult.getName() + ", " + stateResult.getCountry().getName();
      case CityResult cityResult -> "City: " + cityResult.getName() + ", " +
          cityResult.getState().getName() + ", " +
          cityResult.getCountry().getName();
    };

    assertThat(description).isEqualTo(expectedDescription);
  }

  /**
   * Bug detected - https://github.com/tomaytotomato/location4j/issues/45
   * Test cases for special city combinations that include state and country information
   */
  @ParameterizedTest
  @DisplayName("Handling special city combinations: {0}")
  @CsvSource(delimiter = '|', value = {
      "New York, NY, USA|United States|US|USA|New York|New York City",
      "Los Angeles CA|United States|US|USA|California|Los Angeles",
      "Mexico|Mexico|MX|MEX|Mexico City|Mexico",
      "Rio de Janeiro Brazil|Brazil|BR|BRA|Rio de Janeiro|Rio de Janeiro"
  })
  void search_WithSpecialCityCombinations_ReturnsCityResult(
      String searchText,
      String expectedCountry,
      String expectedIso2,
      String expectedIso3,
      String expectedState,
      String expectedCity) {

    var results = searchLocationService.search(searchText);

    assertThat(results).isNotEmpty().hasSize(1);
    assertThat(results.getFirst()).isInstanceOf(CityResult.class);

    CityResult cityResult = (CityResult) results.getFirst();
    assertThat(cityResult.getCountry().getName()).isEqualTo(expectedCountry);
    assertThat(cityResult.getCountry().iso2()).isEqualTo(expectedIso2);
    assertThat(cityResult.getCountry().iso3()).isEqualTo(expectedIso3);
    assertThat(cityResult.getState().name()).isEqualTo(expectedState);
    assertThat(cityResult.getName()).isEqualTo(expectedCity);
  }
}