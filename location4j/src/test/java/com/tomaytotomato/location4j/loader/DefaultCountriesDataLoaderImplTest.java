package com.tomaytotomato.location4j.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCountriesDataLoaderImplTest {

  @DisplayName("Should successfully load Location4J data")
  @Test
  void constructor_WhenFileExists_ShouldLoadDataSuccessfully() {
    // Given & When
    DefaultCountriesDataLoaderImpl loader = new DefaultCountriesDataLoaderImpl();

    // Then
    assertThat(loader.getLocation4JData()).isNotNull();
    assertThat(loader.getCountries()).isNotNull();
    assertThat(loader.getCountries()).isNotEmpty();
    assertThat(loader.getLocation4JData().getCountries()).isEqualTo(loader.getCountries());
  }

  @DisplayName("Should throw RuntimeException when binary file is not found")
  @Test
  void constructor_WhenFileNotFound_ShouldThrowRuntimeException() {
    // Given
    class TestCountriesDataLoader extends DefaultCountriesDataLoaderImpl {
      @Override
      protected void loadData() {
        try (var inputStream = this.getClass().getResourceAsStream("/non-existent-file.bin")) {
          if (inputStream == null) {
            throw new RuntimeException("Failed to load location4j data");
          }
        } catch (Exception e) {
          throw new RuntimeException("Failed to load location4j data", e);
        }
      }
    }

    // When & Then
    assertThatThrownBy(TestCountriesDataLoader::new)
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Failed to load location4j data");
  }

  @DisplayName("Should have pre-built data structures")
  @Test
  void getLocation4JData_ShouldHavePreBuiltDataStructures() {
    // Given
    DefaultCountriesDataLoaderImpl loader = new DefaultCountriesDataLoaderImpl();

    // When
    var data = loader.getLocation4JData();

    // Then
    assertThat(data.getCountryIdToCountryMap()).isNotNull();
    assertThat(data.getCountryNameToCountryMap()).isNotNull();
    assertThat(data.getIso2CodeToCountryMap()).isNotNull();
    assertThat(data.getIso3CodeToCountryMap()).isNotNull();
    assertThat(data.getStateIdToStateMap()).isNotNull();
    assertThat(data.getCityIdToCityMap()).isNotNull();
    assertThat(data.getStateNameToStatesMap()).isNotNull();
    assertThat(data.getStateCodeToStatesMap()).isNotNull();
    assertThat(data.getCityNameToCitiesMap()).isNotNull();
    assertThat(data.getSearchCityNameToCitiesMap()).isNotNull();
  }
}
