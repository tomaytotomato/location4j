package com.tomaytotomato.location4j.model.lookup;

import java.io.Serial;
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

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private transient Country country;
  private transient State state;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private double latitudeDouble = 0.0;
  private double longitudeDouble = 0.0;
  private String timezone;
  private String wikiDataId;

  City() {

  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Country getCountry() {
    return country;
  }

  public State getState() {
    return state;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public double getLatitudeDouble() {
    return latitudeDouble;
  }

  public double getLongitudeDouble() {
    return longitudeDouble;
  }

  public String getTimezone() {
    return timezone;
  }

  public String getWikiDataId() {
    return wikiDataId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City city = (City) o;
    return Double.compare(getLatitudeDouble(), city.getLatitudeDouble()) == 0
        && Double.compare(getLongitudeDouble(), city.getLongitudeDouble()) == 0
        && Objects.equals(getId(), city.getId()) && Objects.equals(getName(),
        city.getName()) && Objects.equals(getCountry(), city.getCountry())
        && Objects.equals(getState(), city.getState()) && Objects.equals(
        getLatitude(), city.getLatitude()) && Objects.equals(getLongitude(),
        city.getLongitude()) && Objects.equals(getTimezone(), city.getTimezone())
        && Objects.equals(getWikiDataId(), city.getWikiDataId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getCountry(), getState(), getLatitude(), getLongitude(),
        getLatitudeDouble(), getLongitudeDouble(), getTimezone(), getWikiDataId());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Integer id;
    private String name;
    private Country country;
    private State state;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private double latitudeDouble;
    private double longitudeDouble;
    private String timezone;
    private String wikiDataId;

    private Builder() {
    }

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder country(Country country) {
      this.country = country;
      return this;
    }

    public Builder state(State state) {
      this.state = state;
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

    public Builder latitudeDouble(double latitudeDouble) {
      this.latitudeDouble = latitudeDouble;
      return this;
    }

    public Builder longitudeDouble(double longitudeDouble) {
      this.longitudeDouble = longitudeDouble;
      return this;
    }

    public Builder timezone(String timezone) {
      this.timezone = timezone;
      return this;
    }

    public Builder wikiDataId(String wikiDataId) {
      this.wikiDataId = wikiDataId;
      return this;
    }

    public City build() {
      City city = new City();
      city.id = this.id;
      city.name = this.name;
      city.country = this.country;
      city.state = this.state;
      city.latitudeDouble = this.latitudeDouble;
      city.longitudeDouble = this.longitudeDouble;
      city.timezone = this.timezone;
      city.latitude = this.latitude;
      city.longitude = this.longitude;
      city.wikiDataId = this.wikiDataId;
      return city;
    }
  }
}
