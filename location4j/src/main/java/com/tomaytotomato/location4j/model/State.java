package com.tomaytotomato.location4j.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.annotation.processing.Generated;

/**
 * Represents a state data class.
 * <p>
 * This class provides methods to access state details, including its geographic coordinates,
 * country association, and the list of cities within the state.
 * </p>
 */
public class State implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer countryId;
  private String countryName;
  private String countryIso2Code;
  private String countryIso3Code;
  private String name;
  private String type;
  private String stateCode;
  private List<City> cities;
  private BigDecimal latitude;
  private BigDecimal longitude;

  /**
   * Default constructor for State.
   */
  State() {
  }

  /**
   * Constructs a new State with the specified attributes.
   *
   * @param id              the unique identifier of the state
   * @param countryId       the unique identifier of the country associated with the state
   * @param countryName     the name of the country associated with the state
   * @param countryIso2Code the ISO 3166-1 alpha-2 code of the country
   * @param countryIso3Code the ISO 3166-1 alpha-3 code of the country
   * @param name            the name of the state
   * @param type            the type or classification of the state (e.g., province, region)
   * @param stateCode       the code of the state
   * @param cities          the list of cities within the state
   * @param latitude        the latitude of the state's geographic center
   * @param longitude       the longitude of the state's geographic center
   */
  private State(Integer id, Integer countryId, String countryName, String countryIso2Code,
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

  public static Builder builder() {
    return new Builder();
  }

  public Integer getId() {
    return id;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer id) {
    this.countryId = id;
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

  public String getType() {
    return type;
  }

  public String getStateCode() {
    return stateCode;
  }

  public List<City> getCities() {
    return cities;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
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

  @Generated("IntelliJ")
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountryId(), getCountryName(), getCountryIso2Code(),
        getCountryIso3Code(), getName(), getType(), getStateCode(), getCities(), getLatitude(),
        getLongitude());
  }

  @Generated("IntelliJ")
  public static final class Builder {

    private Integer id;
    private Integer countryId;
    private String countryName;
    private String countryIso2Code;
    private String countryIso3Code;
    private String name;
    private String type;
    private String stateCode;
    private List<City> cities;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private Builder() {
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

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder stateCode(String stateCode) {
      this.stateCode = stateCode;
      return this;
    }

    public Builder cities(List<City> cities) {
      this.cities = cities;
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

    public State build() {
      return new State(id, countryId, countryName, countryIso2Code, countryIso3Code, name, type,
          stateCode, cities, latitude, longitude);
    }
  }
}
