package com.tomaytotomato.aliases;

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
    return Map.of(
        "Scotland", "United Kingdom",
        "England", "United Kingdom",
        "Northern Ireland", "United Kingdom",
        "Wales", "United Kingdom",
        "Cymru", "United Kingdom"
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
