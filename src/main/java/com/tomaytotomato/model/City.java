package com.tomaytotomato.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class City {
    private Integer id;
    @JsonIgnore
    private Integer countryId;
    @JsonIgnore
    private Integer stateId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public City() {
    }

    public int getId() {
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

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
