package com.tomaytotomato.model;

import java.util.Objects;

public class TimeZone {

  private String zoneName;
  private String abbreviation;
  private String tzName;
  private Integer gmtOffset;
  private String gmtOffsetName;

  public TimeZone() {
  }

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

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public String getTzName() {
    return tzName;
  }

  public void setTzName(String tzName) {
    this.tzName = tzName;
  }

  public Integer getGmtOffset() {
    return gmtOffset;
  }

  public void setGmtOffset(Integer gmtOffset) {
    this.gmtOffset = gmtOffset;
  }

  public String getGmtOffsetName() {
    return gmtOffsetName;
  }

  public void setGmtOffsetName(String gmtOffsetName) {
    this.gmtOffsetName = gmtOffsetName;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(getZoneName(), getAbbreviation(), getTzName(), getGmtOffset(),
        getGmtOffsetName());
  }
}
