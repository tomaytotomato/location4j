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
    return Map.of();
  }
}
