package com.tomaytotomato.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Objects;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return getId() == city.getId() && getCountryId() == city.getCountryId() && getStateId() == city.getStateId() && Objects.equals(getName(), city.getName()) && Objects.equals(getLatitude(), city.getLatitude()) && Objects.equals(getLongitude(), city.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCountryId(), getStateId(), getName(), getLatitude(), getLongitude());
    }
}
