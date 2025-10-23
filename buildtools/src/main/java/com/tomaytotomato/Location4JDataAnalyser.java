package com.tomaytotomato;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tomaytotomato.Location4JDataUtils.fixJsonPropertyNames;
import static com.tomaytotomato.Location4JDataUtils.getLocation4JDataset;

public class Location4JDataAnalyser {

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

        List<Country> countries = mapper.readValue(modifiedJson, new TypeReference<>() {
        });

        Map<String, NameClash> nameIndex = new HashMap<>();

        System.out.println("\n[1/3] Indexing all location names...");

        for (Country country : countries) {
            var countryName = normalize(country.getName());
            nameIndex.computeIfAbsent(countryName, k -> {
                var nc = new NameClash();
                nc.setName(country.getName());
                return nc;
            }).addCountry(country.getName());

            for (State state : country.getStates()) {
                var stateName = normalize(state.getName());
                nameIndex.computeIfAbsent(stateName, k -> {
                    var nc = new NameClash();
                    nc.setName(state.getName());
                    return nc;
                }).addState(state.getName(), country.getName());

                for (City city : state.getCities()) {
                    var cityName = normalize(city.getName());
                    nameIndex.computeIfAbsent(cityName, k -> {
                        var nc = new NameClash();
                        nc.setName(city.getName());
                        return nc;
                    }).addCity(city.getName(), state.getName(), country.getName());
                }
            }
        }

        System.out.println("   Total unique location names: " + nameIndex.size());

        System.out.println("\n[2/3] Analyzing clashes...");

        var clashes = nameIndex.values().stream()
                .filter(NameClash::hasClash)
                .sorted((a, b) -> {
                    int severityCompare = getSeverityRank(b.getSeverity()) - getSeverityRank(a.getSeverity());
                    if (severityCompare != 0) return severityCompare;
                    int aTotal = a.getCountries().size() + a.getStates().size() + a.getCities().size();
                    int bTotal = b.getCountries().size() + b.getStates().size() + b.getCities().size();
                    return bTotal - aTotal;
                })
                .toList();

        System.out.println("   Found " + clashes.size() + " name clashes");

        System.out.println("\n[3/3] CLASH REPORT");
        System.out.println("================================================================================");

        var bySeverity = clashes.stream()
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
