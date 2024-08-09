package com.tomaytotomato.aliases;

import java.util.Map;

/**
 * Allows adding custom aliases for countries, states and cities
 */
public interface LocationAliases {

  Map<String, String> getCountryIso2Aliases();

  Map<String, String> getCountryIso3Aliases();

  Map<String, String> getCountryNameAliases();

  Map<String, String> getStateNameAliases();

  Map<String, String> getCityNameAliases();

}
