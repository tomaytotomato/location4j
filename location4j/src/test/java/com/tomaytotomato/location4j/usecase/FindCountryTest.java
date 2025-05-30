package com.tomaytotomato.location4j.usecase;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomaytotomato.location4j.usecase.lookup.LocationService;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.usecase.lookup.FindCountry;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCountryTest {

  private final FindCountry locationService;

  public FindCountryTest() {
    locationService = LocationService.builder().build();
  }

  @DisplayName("Find Country By ID, when valid and exists, then return Country")
  @ParameterizedTest
  @CsvSource({
      "1, Afghanistan, Asia",
      "55, Croatia, Europe",
      "42, Central African Republic, Africa",
      "125, Liechtenstein, Europe",
  })
  void findCountryById_WhenValidId_ThenReturnCountry(Integer id, String countryName,
      String region) {
    // When
    var result = locationService.findCountryById(id);

    // Then
    assertThat(result)
        .isNotEmpty()
        .get()
        .extracting("name", "region")
        .contains(countryName, region);
  }

  @DisplayName("Find Country By ID, when null then throw exception")
  @Test
  void findCountryById_WhenInvalidId_ThenThrowException() {

    // When Then
    assertThatThrownBy(() -> locationService.findCountryById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ID cannot be null");
  }

  @DisplayName("Find Country By ID, when out of bounds then throw exception")
  @Test
  void findCountryById_WhenNonExistentId_ThenReturnEmpty() {
    // When Then
    assertThatThrownBy(() -> locationService.findCountryById(256))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country ID must be within range of [1 to 250]");
  }

  @DisplayName("Find All Countries, when called should return 250 countries")
  @Test
  void findAllCountries_WhenCalled_ShouldReturnAllCountries() {
    //When
    List<Country> result = locationService.findAllCountries();

    //Then
    assertThat(result).isNotEmpty().hasSize(250);
  }

  @DisplayName("Find Country By Name, when country name is null or blank, then throw exception")
  @Test
  void findCountryByName_WhenNameIsNull_ThenThrowException() {
    assertThatThrownBy(() -> locationService.findCountryByName(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country Name cannot be null or empty");
  }

  @DisplayName("Find Country By Name, when country name is too short, then throw exception")
  @Test
  void findCountryByName_WhenNameIsTooShort_ThenThrowException() {
    assertThatThrownBy(() -> locationService.findCountryByName("Abc"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country Name is too short, the shortest country name is 4 characters (Oman)");
  }

  @DisplayName("Find Country By Name, when country name exists then return country")
  @ParameterizedTest
  @CsvSource({
      "Afghanistan",
      "China",
      "United Kingdom",
      "France",

  })
  void findCountryByName_WhenCountryNameExists_ThenReturnCountry(String countryName) {

    // When
    var result = locationService.findCountryByName(countryName);

    // Then
    assertThat(result).isNotEmpty().get().extracting("name").isEqualTo(countryName);
  }


  @DisplayName("Find Country By Localised Name, when country name is null or blank, then throw exception")
  @Test
  void findCountryByNativeName_WhenNameIsNull_ThenThrowException() {
    assertThatThrownBy(() -> locationService.findCountryByLocalisedName(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Country Localised Name cannot be null or empty");
  }


  @DisplayName("Find Country By Localised Name, when name exists then return country")
  @ParameterizedTest
  @CsvSource({
      "United Kingdom, United Kingdom",
      "Cote D'Ivoire, Ivory Coast",
      "Deutschland, Germany",
      "Nederland, Netherlands",
      "Belgique, Belgium",
      "Belgio, Belgium",
      "벨기에, Belgium"
  })
  void findCountryByNativeName_WhenNativeNameExists_ThenReturnCountry(String localisedName,
      String expectedCountryName) {

    // When
    var result = locationService.findCountryByLocalisedName(localisedName);

    // Then
    assertThat(result).isNotEmpty().get().extracting("name").isEqualTo(expectedCountryName);
  }

}