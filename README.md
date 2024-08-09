# location4j 🌎4️⃣♨️

![GitHub branch check runs](https://img.shields.io/github/check-runs/tomaytotomato/location4j/master)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=tomaytotomato_location4j&metric=bugs)](https://sonarcloud.io/summary/new_code?id=tomaytotomato_location4j)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tomaytotomato_location4j&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tomaytotomato_location4j)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/tomaytotomato/location4j)
![GitHub License](https://img.shields.io/github/license/tomaytotomato/location4j)

location4j is a simple Java library designed for efficient and accurate geographical data lookups for countries, states, and cities. 🗺️

Unlike other libraries, it operates without relying on third-party APIs, making it both cost-effective and fast. 🏎️

Its built-in dataset provides quick lookups and no need for external HTTP calls. 📀

## Quick Example 🏗

```java
import com.tomaytotomato.SearchLocationService;

public class Main {

  public static void main(String[] args) {
    SearchLocationService service = new SearchLocationService();

    // Find all locations named San Francisco
    List<Location> results = service.search("san francisco");
    printResults(results);

    // Narrow search to the US
    results = service.search("san francisco, us");
    printResults(results);

    // Narrow search further to California
    results = service.search("san francisco, us california");
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

| Feature                        | Supported | Object   | Example                                                                 |
|--------------------------------|-----------|----------|-------------------------------------------------------------------------|
| Search (free text)             | ✅         | Location | `search("Canada, Alberta")` -> `[Location("Alberta", "Canada")]`       |
| Find All Countries             | ✅         | Country  | `findAllCountries()` -> `[Country("Belgium"), Country("Canada"), ...]` |
| Find Country by Id             | ✅         | Country  | `findCountryById(1)` -> `Optional[Country("Afghanistan")]`             |
| Find Country by ISO2 code      | ✅         | Country  | `findCountryByISO2Code("CA")` -> `Optional[Country("Canada")]`         |
| Find Country by ISO3 code      | ✅         | Country  | `findCountryByISO3Code("CAN")` -> `Optional[Country("Canada")]`        |
| Find Country by Name           | ✅         | Country  | `findCountryByName("Canada")` -> `Optional[Country("Canada")]`         |
| Find Country by Localised name | ✅         | Country  | `findCountryByLocalisedName("Belgique")` -> `Optional[Country("Belgium")]` |
| Find Countries by State name   | ✅         | Country  | `findAllCountriesByStateName("Texas")` -> `[Country("USA")]`           |
| Find States by State name      | ✅         | State    | `findAllStatesByStateName("Texas")` -> `[State("Texas", "USA")]`       |
| Find State by State Id         | ✅         | State    | `findStateById(5)` -> `Optional[State("California", "USA")]`           |
| Find States by State code      | ✅         | State    | `findAllStatesByStateCode("CA")` -> `[State("California", "USA")]`     |
| Find City by City Id           | ✅         | City     | `findCityById(10)` -> `Optional[City("Los Angeles", "California")]`    |
| Find Cities by City name       | ✅         | City     | `findAllCitiesByCityName("San Francisco")` -> `[City("San Francisco", "California")]` |


🟢 location4j can parse free text strings with or without punctuation or capitalisation e.g.
> San Francisco, CA, USA
> 
> ca united states san francisco
> 
> US, San Francisco, california

🔴 location4j cannot find a location based on a small town, street, latitude/longitude or
zipcode/postcode

## Setup 🚀

Get the latest version of the location4j library by adding it to your Maven pom.xml

```xml

<dependency>
  <groupId>com.tomaytotomato</groupId>
  <artifactId>location4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Gradle**
```gradle
compile "com.tomaytotomato:location4j:1.0.0"
```

## Examples

**Lookup countries**

```java

import com.tomaytotomato.LocationService;

public class LocationServiceExample {

  public static void main(String[] args) {
    LocationService locationService = new LocationService();

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

```java

import com.tomaytotomato.SearchLocationService;

public class LocationSearchServiceExample {
  public static void main(String[] args) {
    SearchLocationService locationSearchService = new SearchLocationService();

    // Search for Santa Clara
    List<Location> results = locationSearchService.search("Santa Clara");

    // Search for Santa Clara in the USA
    List<Location> resultsUnitedStates = locationSearchService.search("Santa Clara USA");

    // Search for Santa Clara in California
    List<Location> resultsCalifornia = locationSearchService.search("Santa Clara US CA");
  }
}

```

## Motivation 🌱

Parsing location data efficiently is crucial for many applications, yet it can be complex and time-consuming. 

Third-party services like Google Location API can be costly, and using large language models can introduce significant latency. 

location4j offers a practical solution with its own dataset, enabling fast and cost-effective geographical lookups to a city/town level (which is sufficient in most cases).

This allows applications to be built without another external dependency and the overheads that come with it.

I may add other functionality in the future if needed e.g. geolocation to nearest place, geofencing etc.

## Credits 🙏

Country data sourced
from [dr5shn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) [![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)

## License 📜

[MIT License](https://choosealicense.com/licenses/mit/)

