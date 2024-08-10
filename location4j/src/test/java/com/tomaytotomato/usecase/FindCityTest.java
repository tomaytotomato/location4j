package com.tomaytotomato.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tomaytotomato.LocationService;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import java.math.BigDecimal;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCityTest {

    private final FindCity locationService;

    public FindCityTest() {
        var textNormaliser = new DefaultTextNormaliser();
        var dataLoader = new DefaultCountriesDataLoaderImpl();
        locationService = new LocationService(textNormaliser, dataLoader);
    }

    @Description("Find City by Lat/long with BigDecimal")
    @Test
    void findCityByLatLongWithBigDecimal() {
        var result =
                locationService.findClosestCityByLatLong(
                        new BigDecimal("30.43826000"), new BigDecimal("-84.28073000"));
        assertEquals("Tallahassee", result.getName());
    }

    @Description("Find City by Lat/long with double")
    @Test
    void findCityByLatLongWithDouble() {
        var result = locationService.findClosestCityByLatLong(30.438, -84.280);
        assertEquals("Tallahassee", result.getName());
    }

    @Description("Find City By ID, when null then throw exception")
    @Test
    void findCityById_WhenNull_ThenThrowException() {
        // When Then
        assertThatThrownBy(() -> locationService.findCityById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City ID cannot be null");
    }

    @Description("Find City By ID, when ID does not exist then return Optional empty")
    @Test
    void findCityById_WhenIdDoesNotExist_ThenReturnEmpty() {
        // When
        var result = locationService.findCityById(900000);

        // Then
        assertThat(result).isEmpty();
    }

    @Description("Find City By ID, when ID exists then return city")
    @ParameterizedTest
    @CsvSource({
        "1, Andorra la Vella",
        "24601, Dassow",
        "26, Bani Yas City",
        "1652, Amlach",
    })
    void findCityById_WhenIdExists_ThenReturnCity(Integer id, String expectedCityName) {

        // When
        var result = locationService.findCityById(id);

        // Then
        assertThat(result).isNotEmpty().get().extracting("name").isEqualTo(expectedCityName);
    }

    @Description("Find All Cities, when called is not empty")
    @Test
    void findAllCities_WhenCalled_ThenReturnListOfCities() {

        // When
        var result = locationService.findAllCities();

        // Then
        assertThat(result).isNotEmpty().hasSize(150661);
    }

    @Description("Find All Cities By City Name, when city name null or blank then throw exception")
    @Test
    void findAllCitiesByCityName_WhenStateNameIsNullOrBlank_ThenThrowException() {
        // When Then
        assertThatThrownBy(() -> locationService.findAllCitiesByCityName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City Name cannot be null or empty");
    }

    @Description("Find All Cities By City Name, when city name not found then return empty list")
    @Test
    void findAllCitiesByCityName_WhenCityNameDoesNotExist_ThenReturnEmptyList() {
        // When
        var result = locationService.findAllCitiesByCityName("Raccoon City");

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Find All Cities By City Name, when city name is found then return list of states")
    @ParameterizedTest
    @CsvSource({
        "Glasgow, 6",
        "London,  6",
        "Delhi, 4",
        "Beijing,  1",
        "Manchester,  16",
        "San Francisco, 30"
    })
    void findAllCitiesByCityName_WhenStateNameExists_ThenReturnStates(
            String cityName, Integer expectedCount) {

        // When
        var results = locationService.findAllCitiesByCityName(cityName);

        // Then
        assertThat(results).hasSize(expectedCount);
    }
}
