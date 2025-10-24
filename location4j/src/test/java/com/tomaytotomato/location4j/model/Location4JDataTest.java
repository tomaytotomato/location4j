package com.tomaytotomato.location4j.model;


import com.tomaytotomato.location4j.loader.TestDataLoader;
import com.tomaytotomato.location4j.model.lookup.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class Location4JDataTest extends TestDataLoader {

    private Location4JData location4JData;

    public Location4JDataTest() {
        location4JData = this.getDataLoader().getLocation4JData();
    }

    @DisplayName("Verify that major world cities are present in the data: {0}, {1}")
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // North America - United States
        "new york city|United States",
        "los angeles|United States",
        "chicago|United States",
        "houston|United States",
        "phoenix|United States",
        "philadelphia|United States",
        "san antonio|United States",
        "san diego|United States",
        "dallas|United States",
        "san jose|United States",
        "austin|United States",
        "jacksonville|United States",
        "fort worth|United States",
        "columbus|United States",
        "san francisco|United States",
        "charlotte|United States",
        "indianapolis|United States",
        "seattle|United States",
        "denver|United States",
        "boston|United States",
        "portland|United States",
        "las vegas|United States",
        "miami|United States",
        "atlanta|United States",
        "washington|United States",

        // North America - Canada
        "toronto|Canada",
        "montreal|Canada",
        "vancouver|Canada",
        "calgary|Canada",
        "edmonton|Canada",
        "ottawa|Canada",

        // North America - Mexico
        "mexico city|Mexico",
        "guadalajara|Mexico",
        "monterrey|Mexico",
        "puebla|Mexico",
        "tijuana|Mexico",

        // South America
        "so paulo|Brazil",
        "rio de janeiro|Brazil",
        "braslia|Brazil",
        "salvador|Brazil",
        "fortaleza|Brazil",
        "belo horizonte|Brazil",
        "buenos aires|Argentina",
        "crdoba|Argentina",
        "rosario|Argentina",
        "lima|Peru",
        "bogot|Colombia",
        "medelln|Colombia",
        "cali|Colombia",
        "santiago|Chile",
        "caracas|Venezuela",
        "quito|Ecuador",
        "guayaquil|Ecuador",

        // Europe - United Kingdom
        "london|United Kingdom",
        "birmingham|United Kingdom",
        "leeds|United Kingdom",
        "glasgow|United Kingdom",
        "sheffield|United Kingdom",
        "manchester|United Kingdom",
        "edinburgh|United Kingdom",
        "liverpool|United Kingdom",
        "bristol|United Kingdom",
        "cardiff|United Kingdom",

        // Europe - Other
        "paris|France",
        "marseille|France",
        "lyon|France",
        "toulouse|France",
        "nice|France",
        "berlin|Germany",
        "hamburg|Germany",
        "munich|Germany",
        "cologne|Germany",
        "frankfurt|Germany",
        "madrid|Spain",
        "barcelona|Spain",
        "valencia|Spain",
        "seville|Spain",
        "rome|Italy",
        "milan|Italy",
        "naples|Italy",
        "turin|Italy",
        "florence|Italy",
        "venice|Italy",
        "amsterdam|Netherlands",
        "rotterdam|Netherlands",
        "the hague|Netherlands",
        "brussels|Belgium",
        "vienna|Austria",
        "warsaw|Poland",
        "krakow|Poland",
        "budapest|Hungary",
        "prague|Czech Republic",
        "bucharest|Romania",
        "athens|Greece",
        "lisbon|Portugal",
        "porto|Portugal",
        "stockholm|Sweden",
        "copenhagen|Denmark",
        "oslo|Norway",
        "helsinki|Finland",
        "dublin|Ireland",
        "zurich|Switzerland",
        "geneva|Switzerland",

        // Asia - East Asia
        "tokyo|Japan",
        "yokohama|Japan",
        "osaka|Japan",
        "nagoya|Japan",
        "sapporo|Japan",
        "fukuoka|Japan",
        "kyoto|Japan",
        "beijing|China",
        "shanghai|China",
        "guangzhou|China",
        "shenzhen|China",
        "chengdu|China",
        "chongqing|China",
        "tianjin|China",
        "wuhan|China",
        "xian|China",
        "hangzhou|China",
        "seoul|South Korea",
        "busan|South Korea",
        "incheon|South Korea",
        "taipei|Taiwan",
        "hong kong|Hong Kong",

        // Asia - South Asia
        "mumbai|India",
        "delhi|India",
        "bangalore|India",
        "hyderabad|India",
        "chennai|India",
        "kolkata|India",
        "pune|India",
        "ahmedabad|India",
        "karachi|Pakistan",
        "lahore|Pakistan",
        "islamabad|Pakistan",
        "dhaka|Bangladesh",
        "colombo|Sri Lanka",

        // Asia - Southeast Asia
        "bangkok|Thailand",
        "singapore|Singapore",
        "manila|Philippines",
        "jakarta|Indonesia",
        "surabaya|Indonesia",
        "kuala lumpur|Malaysia",
        "ho chi minh city|Vietnam",
        "hanoi|Vietnam",

        // Middle East
        "istanbul|Turkey",
        "ankara|Turkey",
        "dubai|United Arab Emirates",
        "abu dhabi|United Arab Emirates",
        "riyadh|Saudi Arabia",
        "jeddah|Saudi Arabia",
        "tel aviv|Israel",
        "jerusalem|Israel",
        "tehran|Iran",
        "baghdad|Iraq",
        "beirut|Lebanon",
        "amman|Jordan",
        "kuwait city|Kuwait",
        "doha|Qatar",

        // Africa
        "cairo|Egypt",
        "lagos|Nigeria",
        "kinshasa|Congo (Kinshasa)",
        "johannesburg|South Africa",
        "cape town|South Africa",
        "durban|South Africa",
        "nairobi|Kenya",
        "casablanca|Morocco",
        "addis ababa|Ethiopia",
        "accra|Ghana",
        "algiers|Algeria",
        "tunis|Tunisia",

        // Oceania
        "sydney|Australia",
        "melbourne|Australia",
        "brisbane|Australia",
        "perth|Australia",
        "adelaide|Australia",
        "auckland|New Zealand",
        "wellington|New Zealand",
        "christchurch|New Zealand"
    })
    void testMajorWorldCitiesArePresent(String cityName, String countryName) {
        // Given
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();

        // When
        List<City> cities = cityNameToCitiesMap.get(cityName);

        // Then
        assertThat(cities)
            .as("City '%s' should be present in the data", cityName)
            .isNotNull()
            .isNotEmpty();

        // Verify at least one city matches the country
        boolean foundMatchingCountry = cities.stream()
            .anyMatch(city -> city.getCountry().getName().equals(countryName));

        assertThat(foundMatchingCountry)
            .as("City '%s' should have an entry for country '%s'", cityName, countryName)
            .isTrue();
    }

    @DisplayName("Verify total number of cities in the dataset")
    @Test
    void testTotalCityCount() {
        // Given
        Map<Integer, City> cityIdToCityMap = location4JData.getCityIdToCityMap();

        // Then
        assertThat(cityIdToCityMap)
            .as("City data should be loaded")
            .isNotNull()
            .isNotEmpty();

        int totalCities = cityIdToCityMap.size();
        assertThat(totalCities)
            .as("Should have a reasonable number of cities (at least 100,000)")
            .isGreaterThan(100_000);
    }

    @DisplayName("Verify that city name map contains expected number of unique city names")
    @Test
    void testUniqueCityNameCount() {
        // Given
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();

        // Then
        assertThat(cityNameToCitiesMap)
            .as("City name map should be loaded")
            .isNotNull()
            .isNotEmpty();

        int uniqueCityNames = cityNameToCitiesMap.size();
        assertThat(uniqueCityNames)
            .as("Should have a reasonable number of unique city names (at least 50,000)")
            .isGreaterThan(50_000);
    }

    @DisplayName("Verify cities have required properties: {0}")
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "london|United Kingdom",
        "new york city|United States",
        "tokyo|Japan",
        "paris|France",
        "sydney|Australia"
    })
    void testCitiesHaveRequiredProperties(String cityName, String countryName) {
        // Given
        Map<String, List<City>> cityNameToCitiesMap = location4JData.getCityNameToCitiesMap();
        List<City> cities = cityNameToCitiesMap.get(cityName);

        // When
        City city = cities.stream()
            .filter(c -> c.getCountry().getName().equals(countryName))
            .findFirst()
            .orElse(null);

        // Then
        assertThat(city)
            .as("City '%s' in '%s' should exist", cityName, countryName)
            .isNotNull();

        assertThat(city.getId())
            .as("City should have an ID")
            .isNotNull()
            .isPositive();

        assertThat(city.getName())
            .as("City should have a name")
            .isNotBlank()
            .isEqualToIgnoringCase(cityName);

        assertThat(city.getCountry())
            .as("City should have a country")
            .isNotNull();

        assertThat(city.getState())
            .as("City should have a state")
            .isNotNull();
    }
}