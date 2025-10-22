package com.tomaytotomato;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Location4JDataUtils {

  public static final String LOCATION4J_DATASET_PATH = "/location4j-countries.json";

  private Location4JDataUtils() {}

  public static InputStream getLocation4JDataset() throws IOException {
    return getLocation4JDataset(LOCATION4J_DATASET_PATH);
  }

  public static InputStream getLocation4JDataset(String location) throws IOException {
    if (Objects.isNull(location) || "".equals(location)) {
      location = LOCATION4J_DATASET_PATH;
    }
    InputStream inputStream = Location4JDataAnalyser.class.getResourceAsStream(location);
    if (inputStream == null) {
      throw new IOException("Could not find location4j-countries.json");
    }
    return inputStream;
  }

  /**
   * Fixes property names in the JSON string to match the expected Java field names.
   */
  public static String fixJsonPropertyNames(String jsonString) {
    var modifiedJson = jsonString.replace("\"native\"", "\"native_name\"");
    modifiedJson = modifiedJson.replace("\"zoneName\"", "\"zone_name\"");
    modifiedJson = modifiedJson.replace("\"phonecode\"", "\"phone_code\"");
    modifiedJson = modifiedJson.replace("\"gmtOffset\"", "\"gmt_offset\"");
    modifiedJson = modifiedJson.replace("\"gmtOffsetName\"", "\"gmt_offset_name\"");
    modifiedJson = modifiedJson.replace("\"tzName\"", "\"tz_name\"");
    modifiedJson = modifiedJson.replace("\"emojiU\"", "\"emoji_u\"");
    modifiedJson = modifiedJson.replace("\"iso3166_2\"", "\"iso31662\"");
    modifiedJson = modifiedJson.replaceAll("\"timezone\":\\s*\"[^\"]*\"", "\"timezone\": null");

    return modifiedJson;
  }

}
