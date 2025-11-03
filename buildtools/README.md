# location4j Build Tools

The location4j build tools module is a utility maven module for developing and analyzing the
dataset.

This module is responsible for:

1. Converting open-source JSON data into a binary format for the location4j library
2. Analyzing the dataset for name clashes and data quality issues

**Note:** This module is not included in the released version of the library!

## Tools Available

### 1. Location4JDataBuilder

Converts the JSON dataset into an optimized binary format (location4j.bin) that the library uses.

**How to build a new location4j data binary:**

```shell
cd buildtools
mvn clean compile
mvn exec:java -Dexec.mainClass="com.tomaytotomato.Location4JDataBuilder"
```

This will generate a new `location4j.bin` file in:

- `location4j/target/generated-resources/location4j.bin`

The binary file contains pre-computed lookup maps for efficient country, state, and city searches.

**Output includes:**

- Number of countries, states, and cities processed
- Path to the generated binary file
- Summary statistics

### 2. Location4JDataAnalyser

Analyzes the dataset to identify name clashes between countries, states, and cities. This helps
identify potential ambiguity issues in search algorithms.

**How to run the dataset analyzer:**

```shell
cd buildtools
mvn exec:java -Dexec.mainClass="com.tomaytotomato.Location4JDataAnalyser"
```

**What it does:**

- Indexes all location names across countries, states, and cities
- Identifies cases where the same name is used for multiple geographical entity types
- Categorizes clashes by severity level

**Severity Levels:**

- **CRITICAL**: Same name used as Country + State + City (e.g., Djibouti)
- **HIGH**: Same name used as Country + City (e.g., Mexico, Lebanon)
- **MEDIUM**: Same name used as Country + State
- **LOW**: Same name used as State + City (common, usually not problematic)

**Output includes:**

- Complete list of all name clashes grouped by severity
- Summary statistics (total countries, states, cities, and clash counts)
- Analysis notes for implementing disambiguation logic

**Use Cases:**

- Identifying data quality issues for upstream PRs
- Understanding potential ambiguity in location searches
- Documenting known limitations

## Known Issues

### Ivory Coast

The country "Ivory Coast" has a `null` native name and may require manual editing.

See upstream PR: https://github.com/dr5hn/countries-states-cities-database/pull/979

### Name Clashes

The dataset contains 2,499+ name clashes where the same name is used for multiple geographical
entities. Run the `Location4JDataAnalyser` to see the full report. Notable examples:

- **Mexico**: Country name + cities in Philippines and USA (but Mexico City is missing from the
  dataset)
- **Lebanon**: Country + 12 cities across USA
- **Djibouti**: Country, state, AND capital city all share the same name

These clashes require disambiguation logic in search implementations (see the
`SearchLocationService` hierarchical scoring algorithm).

## Credits 🙏

Country data sourced
from [dr5hn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database)

[![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)
