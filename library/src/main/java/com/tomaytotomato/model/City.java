package com.tomaytotomato.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a city with various attributes such as name, state, and country.
 * <p>
 * This class provides methods to access city details including geographic coordinates,
 * country and state associations, and other identifiers.
 * </p>
 */
public class City implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer countryId;
  private String countryName;
  private String countryIso2Code;
  private String countryIso3Code;
  private Integer stateId;
  private String stateName;
  private String stateCode;
  private String name;
  private BigDecimal latitude;
  private BigDecimal longitude;

  /**
   * Default constructor for City.
   */
  City() {}

  /**
   * Gets the unique identifier of the city.
   *
   * @return the city's unique ID
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the city.
   *
   * @param id the unique ID to set for the city
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Gets the unique identifier of the country associated with the city.
   *
   * @return the country's unique ID
   */
  public Integer getCountryId() {
    return countryId;
  }

  /**
   * Sets the unique identifier of the country associated with the city.
   *
   * @param countryId the unique ID to set for the country
   */
  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  /**
   * Gets the name of the country associated with the city.
   *
   * @return the country's name
   */
  public String getCountryName() {
    return countryName;
  }

  /**
   * Sets the name of the country associated with the city.
   *
   * @param countryName the name to set for the country
   */
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  /**
   * Gets the ISO 3166-1 alpha-2 code of the country associated with the city.
   *
   * @return the country's ISO2 code
   */
  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  /**
   * Sets the ISO 3166-1 alpha-2 code of the country associated with the city.
   *
   * @param countryIso2Code the ISO2 code to set for the country
   */
  public void setCountryIso2Code(String countryIso2Code) {
    this.countryIso2Code = countryIso2Code;
  }

  /**
   * Gets the ISO 3166-1 alpha-3 code of the country associated with the city.
   *
   * @return the country's ISO3 code
   */
  public String getCountryIso3Code() {
    return countryIso3Code;
  }

  /**
   * Sets the ISO 3166-1 alpha-3 code of the country associated with the city.
   *
   * @param countryIso3Code the ISO3 code to set for the country
   */
  public void setCountryIso3Code(String countryIso3Code) {
    this.countryIso3Code = countryIso3Code;
  }

  /**
   * Gets the unique identifier of the state associated with the city.
   *
   * @return the state's unique ID
   */
  public Integer getStateId() {
    return stateId;
  }

  /**
   * Sets the unique identifier of the state associated with the city.
   *
   * @param stateId the unique ID to set for the state
   */
  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }

  /**
   * Gets the code of the state associated with the city.
   *
   * @return the state's code
   */
  public String getStateCode() {
    return stateCode;
  }

  /**
   * Sets the code of the state associated with the city.
   *
   * @param stateCode the code to set for the state
   */
  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  /**
   * Gets the name of the state associated with the city.
   *
   * @return the state's name
   */
  public String getStateName() {
    return stateName;
  }

  /**
   * Sets the name of the state associated with the city.
   *
   * @param stateName the name to set for the state
   */
  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  /**
   * Gets the name of the city.
   *
   * @return the name of the city
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the city.
   *
   * @param name the name to set for the city
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the latitude of the city.
   *
   * @return the city's latitude
   */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude of the city.
   *
   * @param latitude the latitude to set for the city
   */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude of the city.
   *
   * @return the city's longitude
   */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude of the city.
   *
   * @param longitude the longitude to set for the city
   */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /**
   * Checks whether two city objects are equal based on their attributes.
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
    City city = (City) o;
    return Objects.equals(getId(), city.getId()) && Objects.equals(getCountryId(),
        city.getCountryId()) && Objects.equals(getCountryName(), city.getCountryName())
        && Objects.equals(getCountryIso2Code(), city.getCountryIso2Code())
        && Objects.equals(getCountryIso3Code(), city.getCountryIso3Code())
        && Objects.equals(getStateId(), city.getStateId()) && Objects.equals(
        getStateName(), city.getStateName()) && Objects.equals(getStateCode(),
        city.getStateCode()) && Objects.equals(getName(), city.getName())
        && Objects.equals(getLatitude(), city.getLatitude()) && Objects.equals(
        getLongitude(), city.getLongitude());
  }

  /**
   * Computes the hash code for the city object based on its attributes.
   *
   * @return the hash code of the city
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountryId(), getCountryName(), getCountryIso2Code(),
        getCountryIso3Code(), getStateId(), getStateName(), getStateCode(), getName(),
        getLatitude(),
        getLongitude());
  }
}
