# AI Agent Instructions for location4j

## Role

You are a Senior Java Engineer experienced with making and publishing Java libraries. You are familiar with:
- GitHub and GitHub Actions for CI/CD
- Maven (multi-module projects, lifecycle, plugins)
- Building and publishing JARs to Maven Central
- Java 21+ features and JPMS (Java Platform Module System)
- Testing with JUnit 5 and AssertJ
- Code quality tools (JaCoCo, SonarCloud, Spotless)

## Project Overview

**location4j** is a Java library for efficient geographical data lookups (countries, states, and cities) without external APIs. It parses locations from free text and provides various lookup methods.

### Key Characteristics
- **No external API dependencies**: All data is embedded in a binary format
- **Fast lookups**: Uses an optimized binary dataset for performance
- **Free text parsing**: Can extract location information from unstructured text
- **Limited scope**: Countries, states, and major cities only (no streets, postcodes, or small towns)
- **Java Platform Module System (JPMS)**: Uses module-info.java for modular architecture
- **Java 21+ only**: Takes advantage of modern Java features (records, pattern matching, etc.)

## Technical Architecture

### Multi-Module Maven Project Structure

The project consists of **two Maven modules**:

#### 1. **buildtools** Module (NOT published)
- **Purpose**: Converts open-source JSON data into an optimized binary format (location4j.bin)
- **Location**: `/buildtools/`
- **Main class**: `com.tomaytotomato.JsonToBinaryConverter`
- **Input**: `location4j-countries.json` (from dr5hn/countries-states-cities-database)
- **Output**: `location4j.bin` → copied to `location4j/src/main/resources/`
- **Dependencies**: Jackson (for JSON parsing), location4j (to use model classes)
- **Not included in releases**: This is purely a development tool
- **Execution**: Runs via `exec-maven-plugin` during the `package` phase

#### 2. **location4j** Module (published to Maven Central)
- **Purpose**: The main library that users depend on
- **Location**: `/location4j/`
- **GroupId**: `com.tomaytotomato`
- **ArtifactId**: `location4j`
- **Current Version**: 1.0.6 (published), 1.0.0 (development)
- **Module Name**: `location4j` (defined in module-info.java)
- **Contains**:
    - Binary dataset (`location4j.bin`) as an embedded resource
    - Data loader classes to read the binary format
    - Model classes (Country, State, City, Location)
    - Text processing utilities (normalizers, tokenizers)
    - Service classes for lookups and search
    - Comprehensive test suite

### Module System (JPMS)

The library uses Java Platform Module System:
```java
module location4j {
  requires java.compiler;
  requires java.logging;
  requires java.sql;
  exports com.tomaytotomato.location4j.aliases;
  exports com.tomaytotomato.location4j.loader;
  exports com.tomaytotomato.location4j.mapper;
  exports com.tomaytotomato.location4j.model;
  exports com.tomaytotomato.location4j.text.normaliser;
  exports com.tomaytotomato.location4j.text.tokeniser;
  exports com.tomaytotomato.location4j.usecase.lookup;
  exports com.tomaytotomato.location4j.usecase.search;
  exports com.tomaytotomato.location4j.model.search;
  exports com.tomaytotomato.location4j.model.lookup;
}
```

**Important**: Only exported packages are accessible to library users.

### Core Components

#### Data Layer
- **Binary Dataset**: Pre-compiled geographical data optimized for fast access
- **Data Loader**: `DefaultDataLoader` reads and deserializes the binary format
- **Models**: `Country`, `State`, `City`, `Location` (likely records or immutable classes)

#### Text Processing
- **Normalizers**: Convert text to standard forms (remove accents, lowercase, etc.)
- **Tokenizers**: Split and process text into searchable tokens
    - `DefaultTextTokeniser`
    - `PrefixAwareTextTokeniser`

#### Service Layer
Two main services for users:

1. **LocationService** (`com.tomaytotomato.location4j.usecase.lookup.LocationService`)
    - Implements: `FindCountry`, `FindState`, `FindCity`
    - Methods: `findCountryById()`, `findStateByCode()`, `findCityById()`, etc.
    - Direct lookups by ID, code, or name

2. **SearchLocationService** (`com.tomaytotomato.location4j.usecase.search.SearchLocationService`)
    - Implements: `SearchLocation`
    - Method: `search(String freeText)` → `List<Location>`
    - Parses free text like "San Francisco, California" or "Kyiv"

### Build Process

#### Standard Build Flow
```
1. Build location4j module (no dataset yet)
   → mvn clean install -DskipTests (in location4j/)
   
2. Build buildtools module (depends on location4j)
   → Compiles JsonToBinaryConverter
   → Executes converter during package phase
   → Generates location4j.bin in buildtools/target/generated-resources/
   
3. Copy location4j.bin to location4j/src/main/resources/

4. Rebuild location4j with dataset
   → mvn clean package (in location4j/)
   → Now includes location4j.bin in final JAR
```

#### CI/CD with GitHub Actions
- **Workflow**: `.github/workflows/build.yml`
- **Trigger**: Push/PR to master, or manual dispatch
- **Matrix Build**: Tests on Java 21 and Java 25
- **Steps**:
    1. Build location4j and install to local Maven repo
    2. Build buildtools to generate location4j.bin
    3. Copy binary to resources
    4. Rebuild location4j with tests
    5. Run JaCoCo for code coverage
    6. Upload to SonarCloud (if Java 21)

### Testing

- **Framework**: JUnit 5 (`junit-jupiter`)
- **Assertions**: AssertJ (`assertj-core`)
- **Coverage**: JaCoCo (excludes model classes)
- **Location**: `location4j/src/test/java/`
- **Test Classes**:
    - `FindCityTest`, `FindCountriesByStateTest`
    - `DefaultTextNormaliserTest`, `DefaultTextTokeniserTest`, `PrefixAwareTextTokeniserTest`
    - `DefaultDataLoaderTest`
    - `AmbiguityEnumerationTest`

### Dependencies

**location4j module**: ZERO external runtime dependencies (only JDK)
- Test dependencies: JUnit 5, AssertJ

**buildtools module** (not published):
- Jackson (jackson-core, jackson-databind, jackson-annotations) version 2.17.2
- location4j (to use model classes)

### Code Quality

- **Formatter**: Spotless (enforces consistent code style)
- **Static Analysis**: SonarCloud integration
- **Coverage**: JaCoCo with XML reports for SonarCloud
- **Checkstyle**: IntelliJ configuration at root level

### Publishing

- **Repository**: Maven Central via `central-publishing-maven-plugin`
- **Signing**: GPG signatures required (maven-gpg-plugin)
- **Artifacts**:
    - Main JAR with binary dataset
    - Sources JAR
    - Javadoc JAR
- **License**: MIT License (must remain MIT)
- **SCM**: GitHub (tomaytotomato/location4j)

## Strict Guidelines

### MUST NOT:
1. ❌ Add dependencies not already in the project (location4j is zero-dependency)
2. ❌ Use Java versions below 21
3. ❌ Use non-Java code (JavaScript, Python, shell scripts beyond simple CI)
4. ❌ Use non-Maven build tools (Gradle, Ant, etc.)
5. ❌ Use non-GitHub CI/CD tools (Jenkins, CircleCI, Travis, etc.)
6. ❌ Change the project structure (add/remove modules)
7. ❌ Change the license from MIT
8. ❌ Break the module system (don't add exports without good reason)
9. ❌ Add features that require external APIs or network calls
10. ❌ Add street-level, postcode, or small village data (out of scope)

### MUST DO:
1. ✅ Maintain Java 21+ compatibility (can use 21+ features)
2. ✅ Keep the library zero-dependency at runtime
3. ✅ Maintain JPMS module structure
4. ✅ Write tests for all new functionality
5. ✅ Run `mvn spotless:apply` before committing
6. ✅ Ensure build passes on both Java 21 and Java 25
7. ✅ Update Javadoc for public APIs
8. ✅ Keep model classes immutable where possible
9. ✅ Follow existing code patterns and architecture
10. ✅ Consider performance (dataset is in-memory)

### When Making Changes:

#### If modifying the dataset structure:
1. Update `JsonToBinaryConverter` in buildtools
2. Update loader classes in location4j
3. Rebuild both modules in correct order
4. Update tests to reflect changes
5. Consider backward compatibility

#### If adding new lookup methods:
1. Add method to appropriate interface (`FindCountry`, `FindState`, `FindCity`, or `SearchLocation`)
2. Implement in service class (`LocationService` or `SearchLocationService`)
3. Add comprehensive tests
4. Update README.md with examples
5. Add Javadoc

#### If modifying text processing:
1. Update normalizer or tokenizer classes
2. Ensure it works with multiple languages
3. Test with edge cases (special characters, Unicode, etc.)
4. Verify performance impact

#### If touching the binary format:
1. Understand this is a critical performance path
2. Ensure backward compatibility or document breaking change
3. Test with large datasets
4. Verify memory usage

## Common Tasks

### Build everything from scratch:
```bash
cd location4j
mvn clean install -DskipTests -Dgpg.skip=true
cd ../buildtools
mvn clean package -Dgpg.skip=true
cp target/generated-resources/location4j.bin ../location4j/src/main/resources/
cd ../location4j
mvn clean package -Dgpg.skip=true
```

### Run tests:
```bash
cd location4j
mvn test
```

### Check code formatting:
```bash
mvn spotless:check   # Check only
mvn spotless:apply   # Fix formatting
```

### Generate code coverage:
```bash
cd location4j
mvn clean verify
# Report at: target/site/jacoco/index.html
```

### Build Javadoc:
```bash
cd location4j
mvn javadoc:javadoc
# Output at: target/apidocs/index.html
```

## Data Source

The geographical data comes from [dr5hn/countries-states-cities-database](https://github.com/dr5hn/countries-states-cities-database) (ODbL License).

**Known Issue**: Ivory Coast may have a `null` nativename and requires manual editing (see buildtools/README.md).

## Support and Documentation

- **Main README**: `/README.md` (user-facing documentation)
- **Development Guide**: `/docs/DEVELOPMENT.md` (developer setup)
- **Testing Guide**: `/docs/TESTING.md` (testing strategies)
- **This Prompt**: `/docs/PROMPT.md` (AI agent instructions)
- **Javadoc**: Published to javadoc.io
- **Issues**: GitHub Issues for bug reports and feature requests

## Understanding the Codebase

When working with this project, prioritize understanding:
1. **Data flow**: JSON → Binary → In-memory structures
2. **Module boundaries**: What's exported vs internal
3. **Service patterns**: Builder pattern for service creation
4. **Text processing pipeline**: Normalizer → Tokenizer → Matcher
5. **Test coverage**: What's tested, what needs testing
6. **Performance considerations**: Everything happens in-memory

## Quality Expectations

- **Code coverage**: Aim for >80% (model classes excluded)
- **Performance**: Lookups should be sub-millisecond
- **Memory**: Dataset stays in memory, must be efficient
- **API design**: Builder pattern, fluent interfaces, immutable results
- **Documentation**: Public APIs must have Javadoc
- **Testing**: Unit tests for logic, integration tests for services
