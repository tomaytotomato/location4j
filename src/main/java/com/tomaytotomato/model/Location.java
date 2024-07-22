package com.tomaytotomato.model;

import java.math.BigDecimal;

public class Location {

    private String countryName;
    private String countryIso2Code;
    private String countryIso3Code;
    private String state;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Location() {
    }

    public Location(String countryName, String countryIso2Code, String countryIso3Code, String state, String city, BigDecimal latitude, BigDecimal longitude) {
        this.countryName = countryName;
        this.countryIso2Code = countryIso2Code;
        this.countryIso3Code = countryIso3Code;
        this.state = state;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String countryName;
        private String countryIso2Code;
        private String countryIso3Code;
        private String state;
        private String city;
        private BigDecimal latitude;
        private BigDecimal longitude;

        public Builder() {}

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

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
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
            return new Location(countryName, countryIso2Code, countryIso3Code, state, city, latitude, longitude);
        }
    }
}
