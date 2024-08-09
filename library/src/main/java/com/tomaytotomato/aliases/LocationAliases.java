package com.tomaytotomato.aliases;

import java.util.Map;

/**
 * Allows adding custom aliases for Country, State and City
 */
public interface LocationAliases {

  Map<String, String> getCountryIso2Aliases();

  Map<String, String> getCountryIso3Aliases();

  Map<String, String> getCountryNameAliases();

  Map<String, String> getStateNameAliases();

  Map<String, String> getCityNameAliases();

}
