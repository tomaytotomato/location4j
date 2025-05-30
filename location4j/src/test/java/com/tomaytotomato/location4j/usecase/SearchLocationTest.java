package com.tomaytotomato.location4j.usecase;

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
import com.tomaytotomato.location4j.usecase.search.SearchLocation;
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;
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
    assertThat(countryResult.getCountryName()).isEqualTo(countryName);
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
        .map(SearchLocationResult::getCountryName)
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
    assertThat(countryResult.getCountryName()).isEqualTo(expectedCountryName);
    assertThat(countryResult.getCountryIso3Code()).isEqualTo(iso3Code);
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
    assertThat(result.getFirst().getCountryName()).isEqualTo(countryName);
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
    assertThat(cityResult.getCountryName()).isEqualTo(expectedCountry);
    assertThat(cityResult.getCountryIso2Code()).isEqualTo(expectedIso2);
    assertThat(cityResult.getCountryIso3Code()).isEqualTo(expectedIso3);
    assertThat(cityResult.getStateName()).isEqualTo(expectedState);
    assertThat(cityResult.getCityName()).isEqualTo(expectedCity);
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
      case CountryResult countryResult -> "Country: " + countryResult.getCountryName();
      case StateResult stateResult ->
          "State: " + stateResult.getStateName() + ", " + stateResult.getCountryName();
      case CityResult cityResult -> "City: " + cityResult.getCityName() + ", " +
          cityResult.getStateName() + ", " +
          cityResult.getCountryName();
    };

    assertThat(description).isEqualTo(expectedDescription);
  }
}