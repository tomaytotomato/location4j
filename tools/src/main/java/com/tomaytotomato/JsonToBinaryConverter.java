package com.tomaytotomato;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.tomaytotomato.model.Country;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tool is used to transform the opensource JSON data into a binary format for use by location4j
 */
public class JsonToBinaryConverter {

    private static final String JSON_FILE = "/location4j-countries.json";
    private static final String OUTPUT_FILE = "library/src/main/resources/location4j.bin";

    private static final Logger logger = Logger.getLogger(JsonToBinaryConverter.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);

        try (InputStream inputStream = JsonToBinaryConverter.class.getResourceAsStream(JSON_FILE)) {
            if (inputStream == null) {
                logger.severe("JSON file not found at path: " + JSON_FILE);
                throw new IllegalArgumentException("File not found!");
            }

            logger.info("Starting JSON deserialization...");

            var jsonString = new String(inputStream.readAllBytes());

            // fix data, for inconsistent namings of JSON properties
            var modifiedJson = fixJSONPropertyNames(jsonString);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
            var countries = mapper.readValue(modifiedJson, new TypeReference<List<Country>>() {});

            Path outputFile = Paths.get(OUTPUT_FILE).toAbsolutePath();
            logger.info("Serializing data to binary file at: " + outputFile);

            try (var fileOutputStream = new FileOutputStream(outputFile.toFile());
                var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(countries);
                logger.info("Data successfully serialized to binary format.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exception occurred during serialization: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Argument exception: " + e.getMessage(), e);
        }
    }

    private static String fixJSONPropertyNames(String jsonString) {
        var modifiedJson = jsonString.replaceAll("\"native\"", "\"native_name\"");
        modifiedJson = modifiedJson.replaceAll("\"zoneName\"", "\"zone_name\"");
        modifiedJson = modifiedJson.replaceAll("\"gmtOffset\"", "\"gmt_offset\"");
        modifiedJson = modifiedJson.replaceAll("\"gmtOffsetName\"", "\"gmt_offset_name\"");
        modifiedJson = modifiedJson.replaceAll("\"tzName\"", "\"tz_name\"");
        modifiedJson = modifiedJson.replaceAll("\"emojiU\"", "\"emoji_u\"");
        return modifiedJson;
    }
}