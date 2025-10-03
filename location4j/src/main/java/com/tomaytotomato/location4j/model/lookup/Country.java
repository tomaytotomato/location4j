package com.tomaytotomato.location4j.model.lookup;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

/**
 * Represents a country with various attributes such as name, codes, and geographic information.
 * <p>
 * This class provides methods to access country details, including currency, region, and states.
 * </p>
 */
public class Country implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private String iso3;
  private String iso2;
  private String phoneCode;
  private String numericCode;
  private String capital;
  private String currency;
  private String currencyName;
  private String currencySymbol;
  private String tld;
  private String nativeName;
  private String region;
  private String regionId;
  private String subregion;
  private String subregionId;
  private List<State> states;
  private String nationality;
  private List<TimeZone> timezones;
  private Map<String, String> translations;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String emoji;
  private String emojiU;
  private Integer population;
  private Integer gdp;

  Country() {
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

  public String getIso3() {
    return iso3;
  }

  public String getIso2() {
    return iso2;
  }

  public String getPhoneCode() {
    return phoneCode;
  }

  public String getNumericCode() {
    return numericCode;
  }

  public String getCapital() {
    return capital;
  }

  public String getCurrency() {
    return currency;
  }

  public String getCurrencyName() {
    return currencyName;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public String getTld() {
    return tld;
  }

  public String getNativeName() {
    return nativeName;
  }

  public String getRegion() {
    return region;
  }

  public String getRegionId() {
    return regionId;
  }

  public String getSubregion() {
    return subregion;
  }

  public String getSubregionId() {
    return subregionId;
  }

  public List<State> getStates() {
    return states;
  }

  public String getNationality() {
    return nationality;
  }

  public List<TimeZone> getTimezones() {
    return timezones;
  }

  public Map<String, String> getTranslations() {
    return translations;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public String getEmoji() {
    return emoji;
  }

  public String getEmojiU() {
    return emojiU;
  }

  public Integer getPopulation() { return population; }

  public Integer getGdp() { return gdp; }

  @Generated("IntelliJ")
  public static final class Builder {

    private Integer id;
    private String name;
    private String iso3;
    private String iso2;
    private String phoneCode;
    private String numericCode;
    private String capital;
    private String currency;
    private String currencyName;
    private String currencySymbol;
    private String tld;
    private String nativeName;
    private String region;
    private String regionId;
    private String subregion;
    private String subregionId;
    private List<State> states;
    private String nationality;
    private List<TimeZone> timezones;
    private Map<String, String> translations;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String emoji;
    private String emojiU;
    private Integer population;
    private Integer gdp;

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

    public Builder iso3Code(String iso3Code) {
      this.iso3 = iso3Code;
      return this;
    }

    public Builder iso2Code(String iso2Code) {
      this.iso2 = iso2Code;
      return this;
    }

    public Builder phoneCode(String phoneCode) {
      this.phoneCode = phoneCode;
      return this;
    }

    public Builder numericCode(String numericCode) {
      this.numericCode = numericCode;
      return this;
    }

    public Builder capital(String capital) {
      this.capital = capital;
      return this;
    }

    public Builder currency(String currency) {
      this.currency = currency;
      return this;
    }

    public Builder currencyName(String currencyName) {
      this.currencyName = currencyName;
      return this;
    }

    public Builder currencySymbol(String currencySymbol) {
      this.currencySymbol = currencySymbol;
      return this;
    }

    public Builder tld(String tld) {
      this.tld = tld;
      return this;
    }

    public Builder nativeName(String nativeName) {
      this.nativeName = nativeName;
      return this;
    }

    public Builder region(String region) {
      this.region = region;
      return this;
    }

    public Builder regionId(String regionId) {
      this.regionId = regionId;
      return this;
    }

    public Builder subregion(String subregion) {
      this.subregion = subregion;
      return this;
    }

    public Builder subregionId(String subregionId) {
      this.subregionId = subregionId;
      return this;
    }

    public Builder states(List<State> states) {
      this.states = states;
      return this;
    }

    public Builder nationality(String nationality) {
      this.nationality = nationality;
      return this;
    }

    public Builder timezones(List<TimeZone> timezones) {
      this.timezones = timezones;
      return this;
    }

    public Builder translations(Map<String, String> translations) {
      this.translations = translations;
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

    public Builder emoji(String emoji) {
      this.emoji = emoji;
      return this;
    }

    public Builder emojiU(String emojiU) {
      this.emojiU = emojiU;
      return this;
    }

    public Builder population(Integer population) {
      this.population = population;
      return this;
    }

    public Builder gdp(Integer gdp) {
      this.gdp = gdp;
      return this;
    }

    public Country build() {
      Country country = new Country();
      country.name = this.name;
      country.phoneCode = this.phoneCode;
      country.iso3 = this.iso3;
      country.longitude = this.longitude;
      country.currencyName = this.currencyName;
      country.numericCode = this.numericCode;
      country.subregionId = this.subregionId;
      country.capital = this.capital;
      country.region = this.region;
      country.translations = this.translations;
      country.iso2 = this.iso2;
      country.currencySymbol = this.currencySymbol;
      country.timezones = this.timezones;
      country.tld = this.tld;
      country.currency = this.currency;
      country.nativeName = this.nativeName;
      country.emoji = this.emoji;
      country.subregion = this.subregion;
      country.regionId = this.regionId;
      country.latitude = this.latitude;
      country.id = this.id;
      country.states = this.states;
      country.nationality = this.nationality;
      country.emojiU = this.emojiU;
      country.population = this.population;
      country.gdp = this.gdp;
      return country;
    }
  }
}
