# location4j 🌎4️⃣♨️



![GitHub branch status](https://img.shields.io/github/checks-status/tomaytotomato/location4j/master)
![GitHub License](https://img.shields.io/github/license/tomaytotomato/location4j)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://choosealicense.com/licenses/agpl-3.0/)


location4j is a library that provides location data and location searching within a simple API.
There are no calls to 3rd party location services or LLMs. Just a simple location service for your
Java applications!

## Motivation 🏗️

Location data is very useful to have and provide when creating datasets for analysis, or APIs for
web/mobile.

After working with several expensive location services like Google's Geolocation API etc there was a
need for a simple solution that can provide useful data related to countries, their states and also
cities.

Writing code to parse a piece of free text was also lacking so location4j provides a search location
functionality.

Also the need to not require an external network call made by HTTP was a bonus too.

e.g.

```
"Glasgow, UK" ---> 1 result [Glasgow, United Kingdom]
"Santa Clara" ---> 16 results [Santa Clara, Argentina - Santa Clara, United States etc.]
"Santa Clara CA USA" ---> 1 result [Santa Clara, California, United States]
"Saxony" ---> 1 result [Saxony, Germany]

```

## Features 🔍

location4j has two classes that provide its functionality.

**LocationService**

Provides simple lookups of Country, State and City information

| Feature                      | Implemented | Object  |
|------------------------------|-------------|---------|
| Find All Countries           | ✅           | Country |
| Find Country by Id           | ✅           | Country |
| Find Country by ISO2 code    | ✅           | Country |
| Find Country by ISO3 code    | ✅           | Country |
| Find Country by Native name  | ✅           | Country |
| Find Countries by State name | ✅           | Country |
| Find States by State name    | ✅           | State   |
| Find State by State Id       | ✅           | State   |
| Find States by State code    | ✅           | State   |
| Find City by City Id         | ✅           | City    |
| Find Cities by City name     | ✅           | City    |
| Find Cities by State         | ❌           | City    |
| Find Cities by Country       | ❌           | City    |

**LocationSearchService**

Provides a location search based on unstructured input

| Feature                      | Implemented | Object   |
|------------------------------|-------------|----------|
| Search (free text)           | ✅           | Location |
| Search (Longitude, Latitude) | ❌           | Location |

## Example Usage 🔭

LocationService

```java

import com.tomaytotomato.LocationService;

public static void main(String[] args) {
  var locationService = new LocationService();
  var countries = locationService.findAllCountries();

  var europeanCountries = countries.stream().filter(country -> country.getRegion().equals("Europe"))
      .toList();

  var afghanistan = locationService.findCountryById(1);

  var cities = locationService.findAllCities("San Francisco"); //returns all cities around the world called San Francisco
}

```

LocationSearchService

```java


import com.tomaytotomato.SearchLocationService;

public static void main(String[] args) {

  var locationSearchService = new SearchLocationService();

  var results = locationSearchService.search(
      "Santa Clara"); // will find Santa Clara cities around the world

  var resultsUnitedStates = locationSearchService.search(
      "Santa Clara USA"); // will find Santa Clara cities in USA e.g. California, Utah etc.
  
  var resultsCalifornia = locationSearchService.search("Santa Clara CA"); // will find Santa Clara in California
  
}


```

## Credits 🙏

Country data sourced from [dr5shn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) [![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)


## License 📜

[GNU Affero General Public License v3 (AGPL3)](https://choosealicense.com/licenses/agpl-3.0/)

