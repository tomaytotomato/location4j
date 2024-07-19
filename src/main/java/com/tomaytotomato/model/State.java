package com.tomaytotomato.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class State {
    private Integer id;
    @JsonIgnore
    private Integer countryId;
    private String name;
    private String type;
    @JsonProperty("state_code")
    private String stateCode;
    private List<City> cities;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public State() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(getId(), state.getId()) && Objects.equals(getCountryId(), state.getCountryId()) && Objects.equals(getName(), state.getName()) && Objects.equals(getType(), state.getType()) && Objects.equals(getStateCode(), state.getStateCode()) && Objects.equals(getCities(), state.getCities()) && Objects.equals(getLatitude(), state.getLatitude()) && Objects.equals(getLongitude(), state.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCountryId(), getName(), getType(), getStateCode(), getCities(), getLatitude(), getLongitude());
    }
}
