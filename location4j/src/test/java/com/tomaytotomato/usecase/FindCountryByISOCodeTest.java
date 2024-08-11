package com.tomaytotomato.usecase;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomaytotomato.LocationServiceBuilder;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCountryByISOCodeTest {

  private final FindCountry locationService;

  public FindCountryByISOCodeTest() {
    locationService = LocationServiceBuilder.builder()
        .withCountriesDataLoader(new DefaultCountriesDataLoaderImpl())
        .withTextNormaliser(new DefaultTextNormaliser())
        .build();
  }

  @DisplayName("Get Country By ISO2 Code, when null or blank then throw exception")
  @Test
  void findCountryByISO2Code_WhenNullCode_ThenThrowException() {
    //When Then
    assertThatThrownBy(() -> locationService.findCountryByISO2Code(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ISO2 code cannot be null or empty");
  }

  @DisplayName("Get Country By ISO2 Code, when code is not 2 characters long then throw exception")
  @Test
  void findCountryByISO2Code_WhenInvalidCodeLength_ThenThrowException() {
    //When Then
    assertThatThrownBy(() -> locationService.findCountryByISO2Code("XYZ"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ISO2 must be two characters long e.g. GB, US, FR, DE");
  }

  @DisplayName("Get Country By ISO2 Code, when not found then return Optional empty")
  @Test
  void findCountryByISO2Code_WhenCodeIsNotFound_ThenReturnEmptyOptional() {
    //When
    var result = locationService.findCountryByISO2Code("XX");

    //Then
    assertThat(result).isEmpty();
  }

  @DisplayName("Get Country By ISO2 Code, when valid code and exists, then return Country")
  @ParameterizedTest
  @CsvSource({
      "GB, United Kingdom, Europe, British pound",
      "US, United States, Americas, United States dollar",
      "DK, Denmark, Europe, Danish krone",
      "KR, South Korea, Asia, Won",
      "KY, Cayman Islands, Americas, Cayman Islands dollar"
  })
  void findCountryByISO2Code_WhenValidCodeAndExists_ThenReturnCountry(String iso2Code,
      String expectedName, String expectedRegion, String expectedCurrencyName) {
    // When
    var result = locationService.findCountryByISO2Code(iso2Code);

    // Then
    assertThat(result)
        .isNotEmpty()
        .get()
        .extracting("name", "region", "currencyName")
        .contains(expectedName, expectedRegion, expectedCurrencyName);
  }

  @DisplayName("Get Country By ISO3 Code, when null or blank then throw exception")
  @Test
  void findCountryByISO3Code_WhenNullCode_ThenThrowException() {
    //When Then
    assertThatThrownBy(() -> locationService.findCountryByISO3Code(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ISO3 code cannot be null or empty");
  }

  @DisplayName("Get Country By ISO3 Code, when code is not 3 characters long then throw exception")
  @Test
  void findCountryByISO3Code_WhenInvalidCodeLength_ThenThrowException() {
    //When Then
    assertThatThrownBy(() -> locationService.findCountryByISO3Code("XYZZ"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ISO3 must be three characters long e.g. USA, GBR, FRA, GER");
  }

  @DisplayName("Get Country By ISO3 Code, when not found then return Optional empty")
  @Test
  void findCountryByISO3Code_WhenCodeIsNotFound_ThenReturnEmptyOptional() {
    //When
    var result = locationService.findCountryByISO3Code("XXX");

    //Then
    assertThat(result).isEmpty();
  }

  @DisplayName("Get Country By ISO3 Code, when valid code and exists, then return Country")
  @ParameterizedTest
  @CsvSource({
      "GBR, United Kingdom, Europe, British pound",
      "USA, United States, Americas, United States dollar",
      "FRA, France, Europe, Euro",
      "JPN, Japan, Asia, Japanese yen",
      "AUS, Australia, Oceania, Australian dollar"
  })
  void findCountryByISO3Code_WhenValidCodeAndExists_ThenReturnCountry(String iso3Code,
      String expectedName, String expectedRegion, String expectedCurrencyName) {
    // When
    var result = locationService.findCountryByISO3Code(iso3Code);

    // Then
    assertThat(result)
        .isNotEmpty()
        .get()
        .extracting("name", "region", "currencyName")
        .contains(expectedName, expectedRegion, expectedCurrencyName);
  }

}