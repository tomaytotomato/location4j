package com.tomaytotomato.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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
    private double latitudeDouble = 0.0;
    private double longitudeDouble = 0.0;

    /**
     * Default constructor for City.
     */
    City() {}

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitudeDouble() {
        if (latitudeDouble == 0.0) {
            latitudeDouble = latitude.doubleValue();
        }
        return latitudeDouble;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public double getLongitudeDouble() {
        if (longitudeDouble == 0.0) {
            longitudeDouble = longitude.doubleValue();
        }

        return longitudeDouble;
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
        City city = (City) o;
        return Objects.equals(getId(), city.getId())
                && Objects.equals(getCountryId(), city.getCountryId())
                && Objects.equals(getCountryName(), city.getCountryName())
                && Objects.equals(getCountryIso2Code(), city.getCountryIso2Code())
                && Objects.equals(getCountryIso3Code(), city.getCountryIso3Code())
                && Objects.equals(getStateId(), city.getStateId())
                && Objects.equals(getStateName(), city.getStateName())
                && Objects.equals(getStateCode(), city.getStateCode())
                && Objects.equals(getName(), city.getName())
                && Objects.equals(getLatitude(), city.getLatitude())
                && Objects.equals(getLongitude(), city.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getCountryId(),
                getCountryName(),
                getCountryIso2Code(),
                getCountryIso3Code(),
                getStateId(),
                getStateName(),
                getStateCode(),
                getName(),
                getLatitude(),
                getLongitude());
    }
}
