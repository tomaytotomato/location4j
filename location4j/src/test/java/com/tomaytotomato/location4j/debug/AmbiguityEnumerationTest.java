package com.tomaytotomato.location4j.debug;

import com.tomaytotomato.location4j.loader.TestDataLoader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Enumerates ambiguous location names across country, state and city levels.
 * This is a diagnostic test and will always pass (unless internal structures change).
 */
class AmbiguityEnumerationTest extends TestDataLoader {

  @Disabled
  @SuppressWarnings("unchecked")
  @Test
  void enumerateAmbiguities() throws Exception {
    var service = getSearchLocationService();

    Map<String, Object> maps = new LinkedHashMap<>();
    maps.put("countryNameToCountryMap", reflectMap(service, "countryNameToCountryMap"));
    maps.put("stateNameToStatesMap", reflectMap(service, "stateNameToStatesMap"));
    maps.put("cityNameToCitiesMap", reflectMap(service, "cityNameToCitiesMap"));

    var countryNames = ((Map<String, ?>) maps.get("countryNameToCountryMap")).keySet();
    var stateNames = ((Map<String, List<?>>) maps.get("stateNameToStatesMap")).keySet();
    var cityNames = ((Map<String, List<?>>) maps.get("cityNameToCitiesMap")).keySet();

    var ambiguityRecords = new ArrayList<String>();

    for (var name : countryNames) {
      if (stateNames.contains(name) || cityNames.contains(name)) {
        ambiguityRecords.add("COUNTRY↔" + (stateNames.contains(name) ? "STATE" : "") + (cityNames.contains(name) ? "CITY" : "") + ": " + name);
      }
    }
    for (var name : stateNames) {
      if (cityNames.contains(name)) {
        ambiguityRecords.add("STATE↔CITY: " + name);
      }
    }

    // Deduplicate
    ambiguityRecords = ambiguityRecords.stream().distinct().sorted().collect(Collectors.toCollection(ArrayList::new));

    System.out.println("--- Ambiguous Location Name Summary (" + ambiguityRecords.size() + ") ---");
    ambiguityRecords.forEach(System.out::println);

    assert !ambiguityRecords.isEmpty();
  }

  private Map<String, ?> reflectMap(Object target, String fieldName) throws Exception {
    Field f = target.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    return (Map<String, ?>) f.get(target);
  }
}

