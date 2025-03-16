package com.tomaytotomato.location4j.model;

import java.math.BigDecimal;

/**
 * Represents a location result class, used for returning information on a location search.
 * <p>
 * This class provides methods to access details of a location, including its geographic
 * coordinates, country, state, and city information.
 * </p>
 */
public class Location {

  private String countryName;
  private Integer countryId;
  private String countryIso2Code;
  private String countryIso3Code;
  private String state;
  private Integer stateId;
  private String stateCode;
  private String stateName;
  private String city;
  private Integer cityId;
  private BigDecimal latitude;
  private BigDecimal longitude;

  /**
   * Default constructor for Location.
   */
  public Location() {
  }

  /**
   * Constructs a new Location with the specified attributes.
   *
   * @param countryName     the name of the country
   * @param countryId       the unique identifier of the country
   * @param countryIso2Code the ISO 3166-1 alpha-2 code of the country
   * @param countryIso3Code the ISO 3166-1 alpha-3 code of the country
   * @param state           the name of the state
   * @param stateId         the unique identifier of the state
   * @param stateCode       the code of the state
   * @param stateName       the full name of the state
   * @param city            the name of the city
   * @param cityId          the unique identifier of the city
   * @param latitude        the latitude of the location
   * @param longitude       the longitude of the location
   */
  private Location(String countryName, Integer countryId, String countryIso2Code,
      String countryIso3Code, String state, Integer stateId, String stateCode, String stateName,
      String city,
      Integer cityId, BigDecimal latitude, BigDecimal longitude) {
    this.countryName = countryName;
    this.countryId = countryId;
    this.countryIso2Code = countryIso2Code;
    this.countryIso3Code = countryIso3Code;
    this.state = state;
    this.stateId = stateId;
    this.stateCode = stateCode;
    this.stateName = stateName;
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

  public Integer getCountryId() {
    return countryId;
  }

  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  public String getCountryIso3Code() {
    return countryIso3Code;
  }

  public String getState() {
    return state;
  }

  public Integer getStateId() {
    return stateId;
  }

  public String getStateCode() {
    return stateCode;
  }

  public String getStateName() {
    return stateName;
  }

  public String getCity() {
    return city;
  }

  public Integer getCityId() {
    return cityId;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public static class Builder {

    private String countryName;
    private Integer countryId;
    private String countryIso2Code;
    private String countryIso3Code;
    private String state;
    private Integer stateId;
    private String stateCode;
    private String stateName;
    private String city;
    private Integer cityId;
    private BigDecimal latitude;
    private BigDecimal longitude;

    Builder() {
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

    public Builder stateName(String stateName) {
      this.stateName = stateName;
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
          stateCode, stateName, city, cityId, latitude, longitude);
    }
  }
}
