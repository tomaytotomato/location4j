# Developer Guide

To run and developer on the Location4J project, follow these steps:

## Prerequisites
- Java 21 or higher (Java 25 supported)
- Maven 3.8+
- Git

## Project Structure
- `buildtools/`: Generates the location4j dataset binary from JSON. Not released, but required for building the main library.
- `location4j/`: The main library. Contains all source code, tests, and resources.

## Getting Started

### 1. Clone the Repository
```sh
git clone https://github.com/tomaytotomato/location4j.git
cd location4j
```

### 2. Build the Project
This will generate the binary dataset and run all tests:
```sh
mvn clean install -Dgpg.skip=true
```

### 3. Making Changes
- Edit or add code in `location4j/src/main/java`.
- If you update the JSON data source, the binary will be regenerated automatically during the build.
- Add or update tests in `location4j/src/test/java`.

### 4. Running Tests
To run all tests:
```sh
mvn test
```

### 5. Checking Code Style
The project uses Spotless for code formatting. To check and apply formatting:
```sh
mvn spotless:check   # Check formatting
mvn spotless:apply   # Auto-format code
```

### 6. Submitting a Pull Request
1. Fork the repository and create a feature branch.
2. Make your changes and ensure all tests pass.
3. Push your branch and open a Pull Request on GitHub.

### 7. GitHub Actions & CI
All Pull Requests are automatically built and tested using GitHub Actions. Ensure your branch builds and passes tests before submitting.

## Tips
- Do **not** commit the generated `location4j.bin` file; it is built automatically.
- Keep your changes focused and well-documented.
- If you add new features, please include tests and documentation.

## Need Help?
Open an issue on GitHub or contact the maintainers.

