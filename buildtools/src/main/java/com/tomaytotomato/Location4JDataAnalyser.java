package com.tomaytotomato;

import static com.tomaytotomato.Location4JDataUtils.fixJsonPropertyNames;
import static com.tomaytotomato.Location4JDataUtils.getLocation4JDataset;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Analyzes the geographical dataset to identify name clashes between countries, states, and cities.
 * This tool helps identify ambiguous location names that may cause issues in search algorithms.
 *
 * <p>Name clashes occur when the same name is used for multiple geographical entity types.
 * For example, "Mexico" is both a country and appears as city names in multiple locations.
 *
 * <p>Severity levels:
 * <ul>
 *   <li><strong>CRITICAL:</strong> Same name used as Country + State + City</li>
 *   <li><strong>HIGH:</strong> Same name used as Country + City</li>
 *   <li><strong>MEDIUM:</strong> Same name used as Country + State</li>
 *   <li><strong>LOW:</strong> Same name used as State + City (most common, usually not problematic)</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 * mvn exec:java -Dexec.mainClass="com.tomaytotomato.Location4JDataAnalyser"
 * </pre>
 */
public class Location4JDataAnalyser {

  private static final Logger logger = Logger.getLogger(Location4JDataAnalyser.class.getName());

  private static class NameClash {
        String name;
        List<String> countries = new ArrayList<>();
        List<String> states = new ArrayList<>();
        List<String> cities = new ArrayList<>();

        void addCountry(String country) {
            countries.add(country);
        }

        void addState(String state, String country) {
            states.add(state + " (in " + country + ")");
        }

        void addCity(String city, String state, String country) {
            cities.add(city + " (in " + state + ", " + country + ")");
        }

        boolean hasClash() {
            int types = 0;
            if (!countries.isEmpty()) types++;
            if (!states.isEmpty()) types++;
            if (!cities.isEmpty()) types++;
            return types > 1;
        }

        String getSeverity() {
            if (!countries.isEmpty() && !states.isEmpty() && !cities.isEmpty()) {
                return "CRITICAL (Country + State + City)";
            } else if (!countries.isEmpty() && !cities.isEmpty()) {
                return "HIGH (Country + City)";
            } else if (!countries.isEmpty() && !states.isEmpty()) {
                return "MEDIUM (Country + State)";
            } else if (!states.isEmpty() && !cities.isEmpty()) {
                return "LOW (State + City)";
            }
            return "NONE";
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n  NAME: ").append(name);
            sb.append("\n  SEVERITY: ").append(getSeverity());
            if (!countries.isEmpty()) {
                sb.append("\n  → Countries: ").append(countries);
            }
            if (!states.isEmpty()) {
                sb.append("\n  → States: ").append(states);
            }
            if (!cities.isEmpty()) {
                sb.append("\n  → Cities: ").append(cities);
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("================================================================================");
        System.out.println("DATASET CLASH ANALYZER - Identifying Country/State/City Name Overlaps");
        System.out.println("================================================================================");

        var inputStream = getLocation4JDataset();
        var jsonString = new String(inputStream.readAllBytes());
        var modifiedJson = fixJsonPropertyNames(jsonString);

        var mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<>() {});

        Map<String, NameClash> nameIndex = new HashMap<>();

        System.out.println("\n[1/3] Indexing all location names...");

        for (Country country : countries) {
            String countryName = normalize(country.getName());
            nameIndex.computeIfAbsent(countryName, k -> {
                NameClash nc = new NameClash();
                nc.name = country.getName();
                return nc;
            }).addCountry(country.getName());

            for (State state : country.getStates()) {
                String stateName = normalize(state.getName());
                nameIndex.computeIfAbsent(stateName, k -> {
                    NameClash nc = new NameClash();
                    nc.name = state.getName();
                    return nc;
                }).addState(state.getName(), country.getName());

                for (City city : state.getCities()) {
                    String cityName = normalize(city.getName());
                    nameIndex.computeIfAbsent(cityName, k -> {
                        NameClash nc = new NameClash();
                        nc.name = city.getName();
                        return nc;
                    }).addCity(city.getName(), state.getName(), country.getName());
                }
            }
        }

        System.out.println("   Total unique location names: " + nameIndex.size());

        System.out.println("\n[2/3] Analyzing clashes...");

        List<NameClash> clashes = nameIndex.values().stream()
                .filter(NameClash::hasClash)
                .sorted((a, b) -> {
                    int severityCompare = getSeverityRank(b.getSeverity()) - getSeverityRank(a.getSeverity());
                    if (severityCompare != 0) return severityCompare;
                    int aTotal = a.countries.size() + a.states.size() + a.cities.size();
                    int bTotal = b.countries.size() + b.states.size() + b.cities.size();
                    return bTotal - aTotal;
                })
                .toList();

        System.out.println("   Found " + clashes.size() + " name clashes");

        System.out.println("\n[3/3] CLASH REPORT");
        System.out.println("================================================================================");

        Map<String, List<NameClash>> bySeverity = clashes.stream()
                .collect(Collectors.groupingBy(NameClash::getSeverity));

        for (String severity : Arrays.asList(
                "CRITICAL (Country + State + City)",
                "HIGH (Country + City)",
                "MEDIUM (Country + State)",
                "LOW (State + City)")) {

            List<NameClash> group = bySeverity.get(severity);
            if (group != null && !group.isEmpty()) {
                System.out.println("\n" + "=".repeat(80));
                System.out.println(severity + " - " + group.size() + " case(s)");
                System.out.println("=".repeat(80));

                for (NameClash clash : group) {
                    System.out.println(clash);
                }
            }
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("SUMMARY STATISTICS");
        System.out.println("=".repeat(80));
        System.out.println("Total countries: " + countries.size());
        System.out.println("Total states: " + countries.stream().mapToInt(c -> c.getStates().size()).sum());
        System.out.println("Total cities: " + countries.stream()
                .flatMap(c -> c.getStates().stream())
                .mapToInt(s -> s.getCities().size())
                .sum());
        System.out.println();
        System.out.println("Name clashes by severity:");
        for (String severity : Arrays.asList(
                "CRITICAL (Country + State + City)",
                "HIGH (Country + City)",
                "MEDIUM (Country + State)",
                "LOW (State + City)")) {
            List<NameClash> group = bySeverity.get(severity);
            int count = group != null ? group.size() : 0;
            System.out.println("  " + severity + ": " + count);
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("ANALYSIS NOTES");
        System.out.println("=".repeat(80));
        System.out.println("- CRITICAL and HIGH severity clashes may cause ambiguity in location search");
        System.out.println("- LOW severity clashes (State + City) are common and usually resolved by context");
        System.out.println("- Consider these clashes when implementing disambiguation logic");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("END OF REPORT");
        System.out.println("=".repeat(80));
    }

    private static String normalize(String name) {
        if (name == null) return "";
        return name.toLowerCase().trim();
    }

    private static int getSeverityRank(String severity) {
        return switch (severity) {
            case "CRITICAL (Country + State + City)" -> 4;
            case "HIGH (Country + City)" -> 3;
            case "MEDIUM (Country + State)" -> 2;
            case "LOW (State + City)" -> 1;
            default -> 0;
        };
    }
}
