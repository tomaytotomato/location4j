package com.tomaytotomato.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a timezone data class.
 * <p>
 * This class provides methods to access timezone details, including the zone name,
 * abbreviation, and GMT offset information.
 * </p>
 */
public class TimeZone implements Serializable {

  private static final long serialVersionUID = 1L;

  private String zoneName;
  private String abbreviation;
  private String tzName;
  private Integer gmtOffset;
  private String gmtOffsetName;

  /**
   * Default constructor for TimeZone.
   */
  TimeZone() {
  }

  /**
   * Constructs a new TimeZone with the specified attributes.
   *
   * @param zoneName the name of the timezone
   * @param abbreviation the abbreviation of the timezone
   * @param tzName the full name of the timezone
   * @param gmtOffset the GMT offset in seconds
   * @param gmtOffsetName the human-readable name for the GMT offset
   */
  public TimeZone(String zoneName, String abbreviation, String tzName, Integer gmtOffset,
      String gmtOffsetName) {
    this.zoneName = zoneName;
    this.abbreviation = abbreviation;
    this.tzName = tzName;
    this.gmtOffset = gmtOffset;
    this.gmtOffsetName = gmtOffsetName;
  }

  /**
   * Gets the name of the timezone.
   *
   * @return the name of the timezone
   */
  public String getZoneName() {
    return zoneName;
  }

  /**
   * Sets the name of the timezone.
   *
   * @param zoneName the name to set for the timezone
   */
  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  /**
   * Gets the abbreviation of the timezone.
   *
   * @return the abbreviation of the timezone
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  /**
   * Sets the abbreviation of the timezone.
   *
   * @param abbreviation the abbreviation to set for the timezone
   */
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * Gets the full name of the timezone.
   *
   * @return the full name of the timezone
   */
  public String getTzName() {
    return tzName;
  }

  /**
   * Sets the full name of the timezone.
   *
   * @param tzName the full name to set for the timezone
   */
  public void setTzName(String tzName) {
    this.tzName = tzName;
  }

  /**
   * Gets the GMT offset in seconds.
   *
   * @return the GMT offset in seconds
   */
  public Integer getGmtOffset() {
    return gmtOffset;
  }

  /**
   * Sets the GMT offset in seconds.
   *
   * @param gmtOffset the GMT offset to set in seconds
   */
  public void setGmtOffset(Integer gmtOffset) {
    this.gmtOffset = gmtOffset;
  }

  /**
   * Gets the human-readable name for the GMT offset.
   *
   * @return the name for the GMT offset
   */
  public String getGmtOffsetName() {
    return gmtOffsetName;
  }

  /**
   * Sets the human-readable name for the GMT offset.
   *
   * @param gmtOffsetName the name to set for the GMT offset
   */
  public void setGmtOffsetName(String gmtOffsetName) {
    this.gmtOffsetName = gmtOffsetName;
  }

  /**
   * Checks whether two timezone objects are equal based on their attributes.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeZone timeZone = (TimeZone) o;
    return Objects.equals(getZoneName(), timeZone.getZoneName()) && Objects.equals(
        getAbbreviation(), timeZone.getAbbreviation()) && Objects.equals(getTzName(),
        timeZone.getTzName()) && Objects.equals(getGmtOffset(), timeZone.getGmtOffset())
        && Objects.equals(getGmtOffsetName(), timeZone.getGmtOffsetName());
  }

  /**
   * Computes the hash code for the timezone object based on its attributes.
   *
   * @return the hash code of the timezone
   */
  @Override
  public int hashCode() {
    return Objects.hash(getZoneName(), getAbbreviation(), getTzName(), getGmtOffset(),
        getGmtOffsetName());
  }
}
