package com.tomaytotomato.location4j.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.processing.Generated;

/**
 * Represents a timezone data class.
 * <p>
 * This class provides methods to access timezone details, including the zone name, abbreviation,
 * and GMT offset information.
 * </p>
 */
public class TimeZone implements Serializable {

  @Serial
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
   * @param zoneName      the name of the timezone
   * @param abbreviation  the abbreviation of the timezone
   * @param tzName        the full name of the timezone
   * @param gmtOffset     the GMT offset in seconds
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

  public String getZoneName() {
    return zoneName;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public String getTzName() {
    return tzName;
  }

  public Integer getGmtOffset() {
    return gmtOffset;
  }

  public String getGmtOffsetName() {
    return gmtOffsetName;
  }

  @Generated("IntelliJ")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeZone timeZone = (TimeZone) o;
    return Objects.equals(getZoneName(), timeZone.getZoneName())
        && Objects.equals(getAbbreviation(), timeZone.getAbbreviation())
        && Objects.equals(getTzName(), timeZone.getTzName()) && Objects.equals(
        getGmtOffset(), timeZone.getGmtOffset()) && Objects.equals(getGmtOffsetName(),
        timeZone.getGmtOffsetName());
  }

  @Generated("IntelliJ")
  @Override
  public int hashCode() {
    return Objects.hash(getZoneName(), getAbbreviation(), getTzName(), getGmtOffset(),
        getGmtOffsetName());
  }

  @Generated("IntelliJ")
  public static final class Builder {

    private String zoneName;
    private String abbreviation;
    private String tzName;
    private Integer gmtOffset;
    private String gmtOffsetName;

    private Builder() {
    }

    public static Builder aTimeZone() {
      return new Builder();
    }

    public Builder zoneName(String zoneName) {
      this.zoneName = zoneName;
      return this;
    }

    public Builder abbreviation(String abbreviation) {
      this.abbreviation = abbreviation;
      return this;
    }

    public Builder tzName(String tzName) {
      this.tzName = tzName;
      return this;
    }

    public Builder gmtOffset(Integer gmtOffset) {
      this.gmtOffset = gmtOffset;
      return this;
    }

    public Builder gmtOffsetName(String gmtOffsetName) {
      this.gmtOffsetName = gmtOffsetName;
      return this;
    }

    public TimeZone build() {
      return new TimeZone(zoneName, abbreviation, tzName, gmtOffset, gmtOffsetName);
    }
  }
}
