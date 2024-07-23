package com.tomaytotomato.model;

import java.math.BigDecimal;

public class Location {

  private String countryName;
  private Integer countryId;
  private String countryIso2Code;
  private String countryIso3Code;
  private String state;
  private Integer stateId;
  private String stateCode;
  private String city;
  private Integer cityId;
  private BigDecimal latitude;
  private BigDecimal longitude;

  public Location() {
  }

  public Location(String countryName, Integer countryId, String countryIso2Code,
      String countryIso3Code, String state, Integer stateId, String stateCode, String city,
      Integer cityId, BigDecimal latitude, BigDecimal longitude) {
    this.countryName = countryName;
    this.countryId = countryId;
    this.countryIso2Code = countryIso2Code;
    this.countryIso3Code = countryIso3Code;
    this.state = state;
    this.stateId = stateId;
    this.stateCode = stateCode;
    this.city = city;
    this.cityId = cityId;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  public void setCountryIso2Code(String countryIso2Code) {
    this.countryIso2Code = countryIso2Code;
  }

  public String getCountryIso3Code() {
    return countryIso3Code;
  }

  public void setCountryIso3Code(String countryIso3Code) {
    this.countryIso3Code = countryIso3Code;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Integer getStateId() {
    return stateId;
  }

  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Integer getCityId() {
    return cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return "Location{" +
        "countryName='" + countryName + '\'' +
        ", countryId=" + countryId +
        ", countryIso2Code='" + countryIso2Code + '\'' +
        ", countryIso3Code='" + countryIso3Code + '\'' +
        ", state='" + state + '\'' +
        ", stateId=" + stateId +
        ", stateCode='" + stateCode + '\'' +
        ", city='" + city + '\'' +
        ", cityId=" + cityId +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        '}';
  }

  public static class Builder {

    private String countryName;
    private Integer countryId;
    private String countryIso2Code;
    private String countryIso3Code;
    private String state;
    private Integer stateId;
    private String stateCode;
    private String city;
    private Integer cityId;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Builder() {
    }

    public Builder countryName(String countryName) {
      this.countryName = countryName;
      return this;
    }

    public Builder countryId(Integer countryId) {
      this.countryId = countryId;
      return this;
    }

    public Builder countryIso2Code(String countryIso2Code) {
      this.countryIso2Code = countryIso2Code;
      return this;
    }

    public Builder countryIso3Code(String countryIso3Code) {
      this.countryIso3Code = countryIso3Code;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Builder stateId(Integer stateId) {
      this.stateId = stateId;
      return this;
    }

    public Builder stateCode(String stateCode) {
      this.stateCode = stateCode;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Builder cityId(Integer cityId) {
      this.cityId = cityId;
      return this;
    }

    public Builder latitude(BigDecimal latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(BigDecimal longitude) {
      this.longitude = longitude;
      return this;
    }

    public Location build() {
      return new Location(countryName, countryId, countryIso2Code, countryIso3Code, state, stateId,
          stateCode, city, cityId, latitude, longitude);
    }

  }
}
