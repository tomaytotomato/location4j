package com.tomaytotomato.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

/**
 * Represents a city with various attributes such as name, state, and country.
 * <p>
 * This class provides methods to access city details including geographic coordinates, country and
 * state associations, and other identifiers.
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
  City() {
  }

  private City(Integer id, Integer countryId, String countryName, String countryIso2Code,
      String countryIso3Code, Integer stateId, String stateName, String stateCode, String name,
      BigDecimal latitude, BigDecimal longitude) {
    this.id = id;
    this.countryId = countryId;
    this.countryName = countryName;
    this.countryIso2Code = countryIso2Code;
    this.countryIso3Code = countryIso3Code;
    this.stateId = stateId;
    this.stateName = stateName;
    this.stateCode = stateCode;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Integer getId() {
    return id;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public String getCountryName() {
    return countryName;
  }

  public String getCountryIso2Code() {
    return countryIso2Code;
  }

  public String getCountryIso3Code() {
    return countryIso3Code;
  }

  public Integer getStateId() {
    return stateId;
  }

  public String getStateName() {
    return stateName;
  }

  public String getStateCode() {
    return stateCode;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public void setCountryIso2Code(String countryIso2Code) {
    this.countryIso2Code = countryIso2Code;
  }

  public void setCountryIso3Code(String countryIso3Code) {
    this.countryIso3Code = countryIso3Code;
  }

  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public static final class Builder {

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

    private Builder() {
    }

    public static Builder aCity() {
      return new Builder();
    }

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder countryId(Integer countryId) {
      this.countryId = countryId;
      return this;
    }

    public Builder countryName(String countryName) {
      this.countryName = countryName;
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

    public Builder stateId(Integer stateId) {
      this.stateId = stateId;
      return this;
    }

    public Builder stateName(String stateName) {
      this.stateName = stateName;
      return this;
    }

    public Builder stateCode(String stateCode) {
      this.stateCode = stateCode;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
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

    public City build() {
      return new City(id, countryId, countryName, countryIso2Code, countryIso3Code, stateId,
          stateName, stateCode, name, latitude, longitude);
    }
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

  @Generated("IntelliJ")
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountryId(), getCountryName(), getCountryIso2Code(),
        getCountryIso3Code(), getStateId(), getStateName(), getStateCode(), getName(),
        getLatitude(),
        getLongitude());
  }
}
