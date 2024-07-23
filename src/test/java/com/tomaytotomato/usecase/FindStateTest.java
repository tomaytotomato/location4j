package com.tomaytotomato.usecase;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomaytotomato.LocationService;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import java.io.IOException;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindStateTest {

  private final FindState locationService;

  public FindStateTest() throws IOException {
    var textNormaliser = new DefaultTextNormaliser();
    var dataLoader = new DefaultCountriesDataLoaderImpl();
    locationService = new LocationService(textNormaliser, dataLoader);
  }

  @Description("Find State By ID, when null then throw exception")
  @Test
  void findStateById_WhenNull_ThenThrowException() {
    // When Then
    assertThatThrownBy(() -> locationService.findStateById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("State ID cannot be null");
  }

  @Description("Find State By ID, when ID not found then return Optional empty")
  @Test
  void findStateById_WhenStateNotFound_ThenReturnOptionalEmpty() {
    // When
    var result = locationService.findStateById(99999);

    // Then
    assertThat(result).isEmpty();
  }

  @DisplayName("Find State by ID, when ID exists then return State")
  @ParameterizedTest
  @CsvSource({
      "32,  Žabljak Municipality",
      "66,  Fiorentino",
      "5000, Gers",
      "123,  Żabbar",
      "2,  Somali Region"
  })
  void findStateById_WhenStateIdExists_ThenReturnState(Integer id, String expectedName) {
    // When
    var result = locationService.findStateById(id);

    // Then
    assertThat(result).isNotEmpty().get().extracting("name").isEqualTo(expectedName);
  }

  @Description("Find States By State Name, when null or blank then throw exception")
  @Test
  void findAllStatesByStateName_WhenStateNameIsNullOrBlank_ThenThrowException() {
    // When Then
    assertThatThrownBy(() -> locationService.findAllStatesByStateName(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("State Name cannot be null or empty");
  }

  @Description("Find States By State Name, when state name not found then return empty list")
  @Test
  void findAllStatesByStateName_WhenStateNameDoesNotExists_ThenReturnEmptyList() {
    // When
    var result = locationService.findAllStatesByStateName("Gotham");

    // Then
    assertThat(result).isEmpty();
  }

  @Description("Find States By State Name, when state name too short then throw exception")
  @Test
  void findAllStatesByStateName_WhenStateNameTooShort_ThenThrowException() {
    // When Then
    assertThatThrownBy(() -> locationService.findAllStatesByStateName("AT"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("State Name is too short, the shortest State with name is 3 characters (Goa, India)");
  }

  @DisplayName("Find States By Name, when state name is found then return list of states")
  @ParameterizedTest
  @CsvSource({
      "Scotland, 1",
      "Victoria,  2",
      "Aberdeen, 1",
      "Saxony,  1",
      "Campania,  1"
  })
  void findStatesByStateName_WhenStateNameExists_ThenReturnAllStates(String stateName, Integer expectedCount) {

    // When
    var results = locationService.findAllStatesByStateName(stateName);

    // Then
    assertThat(results).hasSize(expectedCount);
  }

  @Description("Find States By State Code, when null or blank then throw exception")
  @Test
  void findAllStatesByStateCode_WhenStateCodeIsNullOrBlank_ThenThrowException() {
    // When Then
    assertThatThrownBy(() -> locationService.findAllStatesByStateCode(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("State Code cannot be null or empty");
  }

  @Description("Find States By State Code, when state code not found then return empty list")
  @Test
  void findAllStatesByStateCode_WhenStateCodeDoesNotExists_ThenReturnEmptyList() {
    // When
    var result = locationService.findAllStatesByStateCode("WOMP");

    // Then
    assertThat(result).isEmpty();
  }

  @DisplayName("Find States By Code, when state code is found then return list of states")
  @ParameterizedTest
  @CsvSource({
      "CA, 11",
      "ENG,  1",
      "TX, 1",
      "SP,  3",
      "SCO,  1"
  })
  void findStatesByStateCode_WhenStateCodeExists_ThenReturnAllStates(String stateCode, Integer expectedCount) {

    // When
    var results = locationService.findAllStatesByStateCode(stateCode);

    // Then
    assertThat(results).hasSize(expectedCount);
  }
}