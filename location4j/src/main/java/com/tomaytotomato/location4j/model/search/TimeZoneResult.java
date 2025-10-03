package com.tomaytotomato.location4j.model.search;

import java.io.Serializable;

/**
 * Represents a timezone data class.
 * <p>
 * This class provides methods to access timezone details, including the zone name, abbreviation,
 * and GMT offset information.
 * </p>
 */
public record TimeZoneResult(
    String zoneName,
    String abbreviation,
    String tzName,
    Integer gmtOffset,
    String gmtOffsetName
) implements Serializable {
}
