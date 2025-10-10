# location4j 🌎4️⃣♨️

![Build Status](https://github.com/tomaytotomato/location4j/actions/workflows/build.yml/badge.svg)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=tomaytotomato_location4j&metric=bugs)](https://sonarcloud.io/summary/new_code?id=tomaytotomato_location4j)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tomaytotomato_location4j&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tomaytotomato_location4j)
[![javadoc](https://javadoc.io/badge2/com.tomaytotomato/location4j/1.0.6/javadoc.svg)](https://javadoc.io/doc/com.tomaytotomato/location4j/1.0.6)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/tomaytotomato/location4j)
![GitHub License](https://img.shields.io/github/license/tomaytotomato/location4j)
[![libs.tech recommends](https://libs.tech/project/829971910/badge.svg)](https://libs.tech/project/829971910/location4j)

location4j is a simple Java library designed for efficient and accurate geographical data lookups
for countries, states, and cities. 🗺️

Unlike other libraries, it operates without relying on third-party APIs, making it both
cost-effective and fast. 🏎️

Its built-in dataset provides quick lookups and no need for external HTTP calls. 📀

## Requirements 📋

- Java 21 or higher (uses JPMS and Java 21 features)
- Maven 3.8+ (for building from source)

## Setup 🚀

Get the latest version of the location4j library by adding it to your Maven pom.xml

```xml

<dependency>
    <groupId>com.tomaytotomato</groupId>
    <artifactId>location4j</artifactId>
    <version>1.0.6</version>
</dependency>
```

**Gradle**

```gradle
implementation group: 'com.tomaytotomato', name: 'location4j', version: '1.0.6'
```

## Quick Example 🏗

```java
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;

public class Main {

    public static void main(String[] args) {
        SearchLocationService searchLocationService = SearchLocationService.builder().build();

        // Find all locations named San Francisco
        List<Location> results = searchLocationService.search("san francisco");
        printResults(results);

        // Narrow search to the US
        results = searchLocationService.search("san francisco, us");
        printResults(results);

        // Narrow search further to California
        results = searchLocationService.search("san francisco, us california");
        printResults(results);
    }

    private static void printResults(List<Location> results) {
        System.out.println("Locations found: " + results.size());
        results.forEach(location -> {
            System.out.println("Country: " + location.getCountryName());
            System.out.println("State: " + location.getStateName());
            System.out.println("City: " + location.getCityName());
        });
    }
}

```

## Features 🔬

| Feature                                    | Supported | Object   | Example                                                                         |
|--------------------------------------------|-----------|----------|---------------------------------------------------------------------------------|
| Search (free text)                         | ✅         | Location | `search("kyiv")` -> `"Kyiv, Ukraine, Europe, UA"`                               |
| Find All Countries                         | ✅         | Country  | `findAllCountries()` -> `["Belgium", "Canada", ...]`                            |
| Find Country by Id                         | ✅         | Country  | `findCountryById(1)` -> `["Afghanistan"]`                                       |
| Find Country by ISO2 code                  | ✅         | Country  | `findCountryByISO2Code("CA")` -> `["Canada"]`                                   |
| Find Country by ISO3 code                  | ✅         | Country  | `findCountryByISO3Code("CAN")` -> `["Canada"]`                                  |
| Find Country by Name                       | ✅         | Country  | `findCountryByName("Canada")` -> `["Canada"]`                                   |
| Find Country by Localised name             | ✅         | Country  | `findCountryByLocalisedName("Belgique")` -> `["Belgium"]`                       |
| Find Countries by State name               | ✅         | Country  | `findAllCountriesByStateName("Texas")` -> `["USA"]`                             |
| Find States by State name                  | ✅         | State    | `findAllStatesByStateName("Texas")` -> `["Texas", "USA"]`                       |
| Find State by State Id                     | ✅         | State    | `findStateById(5)` -> `["California", "USA"]`                                   |
| Find States by State code                  | ✅         | State    | `findAllStatesByStateCode("CA")` -> `["California", "USA"]`                     |
| Find City by City Id                       | ✅         | City     | `findCityById(10)` -> `["Los Angeles", "California"]`                           |
| Find All Cities                            | ✅         | City     | `findAllCities()` -> `[All cities in database]`                                 |
| Find Cities by City name                   | ✅         | City     | `findAllCitiesByCityName("San Francisco")` -> `["San Francisco", "California"]` |
| Find Closest City by latitude/longitude    | ✅         | City     | `findClosestCityByLatLong(30.438, -84.280)` -> `["Tallahassee", "Florida"]`     |
| Find Closest City by BigDecimal lat/long   | ✅         | City     | `findClosestCityByLatLong(new BigDecimal("30.438"), new BigDecimal("-84.280"))` |
| Find Closest City by String lat/long       | ✅         | City     | `findClosestCityByLatLong("30.438", "-84.280")` -> `["Tallahassee", "Florida"]` |
| Find Street or Address                     | ❌         | N/A      | Not supported - location4j does not provide street-level details                |
| Find Zipcode/Postcode                      | ❌         | N/A      | Not supported - location4j does not include postal code data                    |
| Find Small Towns/Villages                  | ❌         | N/A      | Not supported - location4j focuses on major cities and administrative divisions |

ℹ️ location4j can parse free text strings with or without punctuation or capitalisation e.g.
> San Francisco, CA, USA
>
> ca united states san francisco
>
> US, San Francisco, california

## More Examples 🧪

**Lookup countries**

For simple lookups the `LocationService` can act like a repository, allow the retrieval of
countries, states and city information.

```java

import com.tomaytotomato.location4j.usecase.lookup.LocationService;

public class LocationServiceExample {

    public static void main(String[] args) {
        LocationService locationService = LocationService.builder().build();

        // Get all countries
        List<Country> countries = locationService.findAllCountries();

        // Filter European countries
        List<Country> europeanCountries = countries.stream()
                .filter(country -> "Europe".equals(country.getRegion()))
                .toList();

        // Find Afghanistan by ID
        Country afghanistan = locationService.findCountryById(1);

        // Find all cities named San Francisco
        List<City> cities = locationService.findAllCities("San Francisco");

    }
}

```

**Search locations**

Search any text for a location, the `SearchLocationService` can handle formatted or unformatted
text. It will try and find matches against a variety of keywords it has in its dataset.

```java

import com.tomaytotomato.location4j.usecase.search.SearchLocationService;

public class SearchLocationServiceExample {

    public static void main(String[] args) {
        SearchLocationService searchLocationService = SearchLocationService.builder()
            .withTextNormaliser(new DefaultTextNormaliser())
            .build();

        // Search for Santa Clara
        List<Location> results = searchLocationService.search("Santa Clara");

        // Search for Santa Clara in the USA
        List<Location> resultsUnitedStates = searchLocationService.search("Santa Clara USA");

        // Search for Santa Clara in California (it works with ISO2 or ISO3) codes
        List<Location> resultsCalifornia = searchLocationService.search("Santa Clara US CA");
    }
}

```

## Motivation 🌱

Parsing location data efficiently is crucial for many applications, yet it can be complex and
time-consuming.

Third-party services like Google Location API can be costly, and using large language models can
introduce significant latency.

location4j offers a practical solution with its own dataset, enabling fast and cost-effective
geographical lookups to a city/town level (which is sufficient in most cases).

This allows applications to be built without another external dependency and the overheads that come
with it.

I may add other functionality in the future if needed e.g. geolocation to nearest place, geofencing
etc.

## More Info

[Testing](docs/TESTING.md)
[Development](docs/DEVELOPMENT.md)
[Build Tools](buildtools/README.md)

## Credits 🙏

Country data sourced
from [dr5shn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) [![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)

## License 📜

[MIT License](https://choosealicense.com/licenses/mit/)

