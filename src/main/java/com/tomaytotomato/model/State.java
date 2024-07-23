package com.tomaytotomato.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

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

    public State() {
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

    public void setCountryIso2Code(String countryIso2Code) {
        this.countryIso2Code = countryIso2Code;
    }

    public String getCountryIso2Code() {
        return countryIso2Code;
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

}
