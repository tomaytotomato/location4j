package com.tomaytotomato.model;

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
   * @param countryName the name of the country
   * @param countryId the unique identifier of the country
   * @param countryIso2Code the ISO 3166-1 alpha-2 code of the country
   * @param countryIso3Code the ISO 3166-1 alpha-3 code of the country
   * @param state the name of the state
   * @param stateId the unique identifier of the state
   * @param stateCode the code of the state
   * @param stateName the full name of the state
   * @param city the name of the city
   * @param cityId the unique identifier of the city
   * @param latitude the latitude of the location
   * @param longitude the longitude of the location
   */
  public Location(String countryName, Integer countryId, String countryIso2Code,
      String countryIso3Code, String state, Integer stateId, String stateCode, String stateName, String city,
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

  /**
   * Gets the name of the country.
   *
   * @return the name of the country
   */
  public String getCountryName() {
    return countryName;
  }

  /**
   * Sets the name of the country.
   *
   * @param countryName the name to set for the country
   */
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  /**
   * Gets the unique identifier of the country.
   *
   * @return the country's unique ID
   */
  public Integer getCountryId() {
    return countryId;
  }

  /**
   * Sets the unique identifier of the country.
   *
   * @param countryId the unique ID to set for the country
   */
  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  /**
   * Gets the ISO 3166-1 alpha-2 code of the country.
   *
   * @return the country's ISO2 code
   */
  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  /**
   * Sets the ISO 3166-1 alpha-2 code of the country.
   *
   * @param countryIso2Code the ISO2 code to set for the country
   */
  public void setCountryIso2Code(String countryIso2Code) {
    this.countryIso2Code = countryIso2Code;
  }

  /**
   * Gets the ISO 3166-1 alpha-3 code of the country.
   *
   * @return the country's ISO3 code
   */
  public String getCountryIso3Code() {
    return countryIso3Code;
  }

  /**
   * Sets the ISO 3166-1 alpha-3 code of the country.
   *
   * @param countryIso3Code the ISO3 code to set for the country
   */
  public void setCountryIso3Code(String countryIso3Code) {
    this.countryIso3Code = countryIso3Code;
  }

  /**
   * Gets the name of the state.
   *
   * @return the name of the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the name of the state.
   *
   * @param state the name to set for the state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Gets the unique identifier of the state.
   *
   * @return the state's unique ID
   */
  public Integer getStateId() {
    return stateId;
  }

  /**
   * Sets the unique identifier of the state.
   *
   * @param stateId the unique ID to set for the state
   */
  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }

  /**
   * Gets the code of the state.
   *
   * @return the state's code
   */
  public String getStateCode() {
    return stateCode;
  }

  /**
   * Sets the code of the state.
   *
   * @param stateCode the code to set for the state
   */
  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  /**
   * Gets the full name of the state.
   *
   * @return the state's full name
   */
  public String getStateName() {
    return stateName;
  }

  /**
   * Sets the full name of the state.
   *
   * @param stateName the full name to set for the state
   */
  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  /**
   * Gets the name of the city.
   *
   * @return the name of the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the name of the city.
   *
   * @param city the name to set for the city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Gets the unique identifier of the city.
   *
   * @return the city's unique ID
   */
  public Integer getCityId() {
    return cityId;
  }

  /**
   * Sets the unique identifier of the city.
   *
   * @param cityId the unique ID to set for the city
   */
  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  /**
   * Gets the latitude of the location.
   *
   * @return the latitude of the location
   */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude of the location.
   *
   * @param latitude the latitude to set for the location
   */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude of the location.
   *
   * @return the longitude of the location
   */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude of the location.
   *
   * @param longitude the longitude to set for the location
   */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /**
   * Creates a new {@link Builder} for constructing a {@link Location} instance.
   *
   * @return a new Builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for constructing instances of {@link Location}.
   * <p>
   * This builder allows for setting various attributes before creating a Location object.
   * </p>
   */
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

    /**
     * Default constructor for Builder.
     */
    Builder() { }

    /**
     * Sets the name of the country for the location.
     *
     * @param countryName the name of the country
     * @return the Builder instance
     */
    public Builder countryName(String countryName) {
      this.countryName = countryName;
      return this;
    }

    /**
     * Sets the unique identifier of the country for the location.
     *
     * @param countryId the unique ID of the country
     * @return the Builder instance
     */
    public Builder countryId(Integer countryId) {
      this.countryId = countryId;
      return this;
    }

    /**
     * Sets the ISO 3166-1 alpha-2 code of the country for the location.
     *
     * @param countryIso2Code the ISO2 code of the country
     * @return the Builder instance
     */
    public Builder countryIso2Code(String countryIso2Code) {
      this.countryIso2Code = countryIso2Code;
      return this;
    }

    /**
     * Sets the ISO 3166-1 alpha-3 code of the country for the location.
     *
     * @param countryIso3Code the ISO3 code of the country
     * @return the Builder instance
     */
    public Builder countryIso3Code(String countryIso3Code) {
      this.countryIso3Code = countryIso3Code;
      return this;
    }

    /**
     * Sets the name of the state for the location.
     *
     * @param state the name of the state
     * @return the Builder instance
     */
    public Builder state(String state) {
      this.state = state;
      return this;
    }

    /**
     * Sets the unique identifier of the state for the location.
     *
     * @param stateId the unique ID of the state
     * @return the Builder instance
     */
    public Builder stateId(Integer stateId) {
      this.stateId = stateId;
      return this;
    }

    /**
     * Sets the code of the state for the location.
     *
     * @param stateCode the code of the state
     * @return the Builder instance
     */
    public Builder stateCode(String stateCode) {
      this.stateCode = stateCode;
      return this;
    }

    /**
     * Sets the full name of the state for the location.
     *
     * @param stateName the full name of the state
     * @return the Builder instance
     */
    public Builder stateName(String stateName) {
      this.stateName = stateName;
      return this;
    }

    /**
     * Sets the name of the city for the location.
     *
     * @param city the name of the city
     * @return the Builder instance
     */
    public Builder city(String city) {
      this.city = city;
      return this;
    }

    /**
     * Sets the unique identifier of the city for the location.
     *
     * @param cityId the unique ID of the city
     * @return the Builder instance
     */
    public Builder cityId(Integer cityId) {
      this.cityId = cityId;
      return this;
    }

    /**
     * Sets the latitude of the location.
     *
     * @param latitude the latitude of the location
     * @return the Builder instance
     */
    public Builder latitude(BigDecimal latitude) {
      this.latitude = latitude;
      return this;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param longitude the longitude of the location
     * @return the Builder instance
     */
    public Builder longitude(BigDecimal longitude) {
      this.longitude = longitude;
      return this;
    }

    /**
     * Builds a new {@link Location} instance with the specified attributes.
     *
     * @return a new Location object
     */
    public Location build() {
      return new Location(countryName, countryId, countryIso2Code, countryIso3Code, state, stateId,
          stateCode, stateName, city, cityId, latitude, longitude);
    }
  }
}
