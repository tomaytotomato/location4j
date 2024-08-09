# location4j - Tool

This is the tool module, it is not included in the library.

It is used to prepare the data used by location4j by converting it from a JSON format to binary etc.


## How to build a new location4j data binary

```shell
cd tools
maven clean compile
mvn exec:java -Dexec.mainClass="com.tomaytotomato.JsonToBinaryConverter"

```

This will generate a new location4j.bin file in the library's resources folder - [library/src/main/resources](library/src/main/resources)

## Credits 🙏

Country data sourced from [dr5shn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) [![License: ODbL](https://img.shields.io/badge/License-ODbL-brightgreen.svg)](https://opendatacommons.org/licenses/odbl/)