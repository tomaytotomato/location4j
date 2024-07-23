package com.tomaytotomato.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class State {

  private Integer id;
  @JsonIgnore
  private Integer countryId;
  @JsonIgnore
  private String countryName;
  @JsonIgnore
  private String countryIso2Code;
  @JsonIgnore
  private String countryIso3Code;

  private String name;
  private String type;
  @JsonProperty("state_code")
  private String stateCode;
  private List<City> cities;
  private BigDecimal latitude;
  private BigDecimal longitude;

  State() {}

  @JsonCreator
  public State(Integer id, Integer countryId, String countryName, String countryIso2Code,
      String countryIso3Code, String name, String type, String stateCode, List<City> cities,
      BigDecimal latitude, BigDecimal longitude) {
    this.id = id;
    this.countryId = countryId;
    this.countryName = countryName;
    this.countryIso2Code = countryIso2Code;
    this.countryIso3Code = countryIso3Code;
    this.name = name;
    this.type = type;
    this.stateCode = stateCode;
    this.cities = cities;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public List<City> getCities() {
    return cities;
  }

  public void setCities(List<City> cities) {
    this.cities = cities;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    State state = (State) o;
    return Objects.equals(getId(), state.getId()) && Objects.equals(
        getCountryId(), state.getCountryId()) && Objects.equals(getCountryName(),
        state.getCountryName()) && Objects.equals(getCountryIso2Code(),
        state.getCountryIso2Code()) && Objects.equals(getCountryIso3Code(),
        state.getCountryIso3Code()) && Objects.equals(getName(), state.getName())
        && Objects.equals(getType(), state.getType()) && Objects.equals(
        getStateCode(), state.getStateCode()) && Objects.equals(getCities(),
        state.getCities()) && Objects.equals(getLatitude(), state.getLatitude())
        && Objects.equals(getLongitude(), state.getLongitude());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountryId(), getCountryName(), getCountryIso2Code(),
        getCountryIso3Code(), getName(), getType(), getStateCode(), getCities(), getLatitude(),
        getLongitude());
  }
}
