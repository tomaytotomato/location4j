# location4j Tools

The location4j Tools module is a utility for developing the dataset. 
This module is responsible for converting open-source JSON data into a binary format that the location4j library can efficiently utilize.

Note: it is not included in the released version of the library!

## How to build a new location4j data binary

```shell
cd tools
maven clean compile
mvn exec:java -Dexec.mainClass="com.tomaytotomato.JsonToBinaryConverter"

```

This will generate a new location4j.bin file in the library's resources folder - [library/src/main/resources](library/src/main/resources)

## Credits 🙏

Country data sourced from [dr5shn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) [![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)
