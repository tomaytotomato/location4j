package com.tomaytotomato.location4j.aliases;

import java.util.Map;

public class DefaultLocationAliases implements LocationAliases {

  @Override
  public Map<String, String> getCountryIso2Aliases() {
    return Map.of("uk", "gb",
        "en", "gb"
    );
  }

  @Override
  public Map<String, String> getCountryIso3Aliases() {
    return Map.of(
        "eng", "gbr",
        "sco", "gbr",
        "wal", "gbr",
        "cym", "gbr",
        "nil", "gbr"
    );
  }

  @Override
  public Map<String, String> getCountryNameAliases() {
    var unitedKingdom = "United Kingdom";
    return Map.of(
        "Scotland", unitedKingdom,
        "England", unitedKingdom,
        "Northern Ireland", unitedKingdom,
        "Wales", unitedKingdom,
        "Cymru", unitedKingdom
    );
  }

  @Override
  public Map<String, String> getStateNameAliases() {
    return Map.of();
  }

  @Override
  public Map<String, String> getCityNameAliases() {
    return Map.ofEntries(
        // Major world capitals with English/local name differences
        Map.entry("Mexico City", "Ciudad de Mexico"),
        Map.entry("Mexico", "Ciudad de Mexico"),
        Map.entry("Rio de Janeiro", "Rio de Janeiro"),
        Map.entry("Rio", "Rio de Janeiro"),
        Map.entry("The Hague", "Den Haag"),
        Map.entry("Brussels", "Bruxelles"),
        Map.entry("Munich", "Munchen"),
        Map.entry("Vienna", "Wien"),
        Map.entry("Prague", "Praha"),
        Map.entry("Moscow", "Moskva"),
        Map.entry("Warsaw", "Warszawa"),
        Map.entry("Bucharest", "Bucuresti"),
        Map.entry("Athens", "Athina"),
        Map.entry("Copenhagen", "Kobenhavn"),
        Map.entry("Helsinki", "Helsinki"),
        Map.entry("Lisbon", "Lisboa"),
        Map.entry("Rome", "Roma"),
        Map.entry("Belgrade", "Beograd"),
        Map.entry("Beijing", "Beijing"),
        Map.entry("Peking", "Beijing"),
        Map.entry("Bombay", "Mumbai"),
        Map.entry("Calcutta", "Kolkata"),
        Map.entry("Madras", "Chennai"),
        Map.entry("Saigon", "Ho Chi Minh City"),
        Map.entry("Cape Town", "Cape Town"),
        Map.entry("NYC", "New York City"),
        Map.entry("New York", "New York City"),
        Map.entry("LA", "Los Angeles"),
        Map.entry("San Fran", "San Francisco"),
        Map.entry("Philly", "Philadelphia")
    );
  }
}
