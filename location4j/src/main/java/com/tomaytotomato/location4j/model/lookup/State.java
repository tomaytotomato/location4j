package com.tomaytotomato.location4j.model.lookup;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.processing.Generated;

/**
 * Represents a state/province with various attributes such as name, codes, and geographic information.
 */
public class State implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private String nativeName;
  private String type;
  private String iso2;
  private String iso31662;
  private TimeZone timezone;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private transient Country country;
  private transient List<City> cities;

  State() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNativeName() { return nativeName; }

  public String getType() {
    return type;
  }

  public String getIso2() {
    return iso2;
  }

  public String getIso31662() { return iso31662; }

  public TimeZone getTimezone() {
    return timezone;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public List<City> getCities() {
    return cities;
  }

  public Country getCountry() {
    return country;
  }

  @Generated("IntelliJ")
  public static final class Builder {
    private Integer id;
    private String name;
    private String nativeName;
    private String type;
    private String iso2;
    private String iso31662;
    private TimeZone timezone;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Country country;
    private List<City> cities;

    private Builder() {
    }

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public  Builder nativeName(String nativeName) {
      this.nativeName = nativeName;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder iso2(String iso2) {
      this.iso2 = iso2;
      return this;
    }

    public Builder iso31662(String iso31662) {
      this.iso31662 = iso31662;
      return this;
    }

    public Builder timezone(TimeZone timezone) {
      this.timezone = timezone;
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

    public Builder country(Country country) {
      this.country = country;
      return this;
    }

    public Builder cities(List<City> cities) {
      this.cities = cities;
      return this;
    }

    public State build() {
      State state = new State();
      state.id = this.id;
      state.name = this.name;
      state.nativeName = this.nativeName;
      state.type = this.type;
      state.iso2 = this.iso2;
      state.iso31662 = this.iso31662;
      state.timezone = this.timezone;
      state.latitude = this.latitude;
      state.longitude = this.longitude;
      state.country = this.country;
      state.cities = this.cities;
      return state;
    }
  }
}
