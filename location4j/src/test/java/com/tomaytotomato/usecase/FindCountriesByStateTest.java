package com.tomaytotomato.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomaytotomato.LocationService;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import java.io.IOException;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCountriesByStateTest {

  private final FindCountry locationService;

  public FindCountriesByStateTest() {
    var textNormaliser = new DefaultTextNormaliser();
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    locationService = new LocationService(textNormaliser, dataLoader);
  }

  @Description("Find All Countries By State Name, when null or blank then throw exception")
  @Test
  void findAllCountriesWithStateName_WhenNullOrBlank_ThenThrowException() {
    //When Then
    assertThatThrownBy(() -> locationService.findAllCountriesByStateName(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("State name cannot be null or empty");
  }

  @Description("Find All Countries By State Name, when not found then return empty list")
  @Test
  void findAllCountriesWithStateName_WhenStateNameNotFound_ThenReturnEmptyList() {
    //When
    var result = locationService.findAllCountriesByStateName("Pinnatassee");

    //Then
    assertThat(result).isEmpty();
  }

  @DisplayName("Find Countries by State name, when valid code and exists, then return Countries")
  @ParameterizedTest
  @CsvSource({
      "California, 1, United States",
      "Scotland, 1, United Kingdom",
      "Victoria, 2, Malta|Australia",
      "Georgia, 1, United States",
      "Ontario, 1, Canada"
  })

  void findAllCountriesWithStateName_WhenValidStateNameAndExists_ThenReturnCountries(String stateName, Integer expectedCountryCount, String expectedCountryNames) {
    // When
    var result = locationService.findAllCountriesByStateName(stateName);

    // Then
    assertThat(result)
        .isNotEmpty()
        .hasSize(expectedCountryCount);

    // Split the expected country names by '|' and validate each one is present
    var expectedNames = expectedCountryNames.split("\\|");
    var resultCountryNames = result.stream().map(Country::getName).toList();
    assertThat(resultCountryNames).containsExactlyInAnyOrder(expectedNames);
  }

}