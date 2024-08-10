package com.tomaytotomato.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a country with various attributes such as name, codes, and geographic information.
 * <p>
 * This class provides methods to access country details, including currency, region, and states.
 * </p>
 */
public class Country implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private String iso3;
  private String iso2;
  private String zoneName;
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

  Country() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIso3() {
    return iso3;
  }

  public void setIso3(String iso3) {
    this.iso3 = iso3;
  }

  public String getIso2() {
    return iso2;
  }

  public void setIso2(String iso2) {
    this.iso2 = iso2;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public String getPhoneCode() {
    return phoneCode;
  }

  public void setPhoneCode(String phoneCode) {
    this.phoneCode = phoneCode;
  }

  public String getNumericCode() {
    return numericCode;
  }

  public void setNumericCode(String numericCode) {
    this.numericCode = numericCode;
  }

  public String getCapital() {
    return capital;
  }

  public void setCapital(String capital) {
    this.capital = capital;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getTld() {
    return tld;
  }

  public void setTld(String tld) {
    this.tld = tld;
  }

  public String getNativeName() {
    return nativeName;
  }

  public void setNativeName(String nativeName) {
    this.nativeName = nativeName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getSubregion() {
    return subregion;
  }

  public void setSubregion(String subregion) {
    this.subregion = subregion;
  }

  public String getSubregionId() {
    return subregionId;
  }

  public void setSubregionId(String subregionId) {
    this.subregionId = subregionId;
  }

  public List<State> getStates() {
    return states;
  }

  public void setStates(List<State> states) {
    this.states = states;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public List<TimeZone> getTimezones() {
    return timezones;
  }

  public void setTimezones(List<TimeZone> timezones) {
    this.timezones = timezones;
  }

  public Map<String, String> getTranslations() {
    return translations;
  }

  public void setTranslations(Map<String, String> translations) {
    this.translations = translations;
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

  public String getEmoji() {
    return emoji;
  }

  public void setEmoji(String emoji) {
    this.emoji = emoji;
  }

  public String getEmojiU() {
    return emojiU;
  }

  public void setEmojiU(String emojiU) {
    this.emojiU = emojiU;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Country country = (Country) o;
    return getId() == country.getId() && Objects.equals(getName(), country.getName())
        && Objects.equals(getIso3(), country.getIso3()) && Objects.equals(getIso2(),
        country.getIso2()) && Objects.equals(getZoneName(), country.getZoneName())
        && Objects.equals(getPhoneCode(), country.getPhoneCode()) && Objects.equals(
        getNumericCode(), country.getNumericCode()) && Objects.equals(getCapital(),
        country.getCapital()) && Objects.equals(getCurrency(), country.getCurrency())
        && Objects.equals(getCurrencyName(), country.getCurrencyName()) && Objects.equals(
        getCurrencySymbol(), country.getCurrencySymbol()) && Objects.equals(getTld(),
        country.getTld()) && Objects.equals(getNativeName(), country.getNativeName())
        && Objects.equals(getRegion(), country.getRegion()) && Objects.equals(getRegionId(),
        country.getRegionId()) && Objects.equals(getSubregion(), country.getSubregion())
        && Objects.equals(getSubregionId(), country.getSubregionId()) && Objects.equals(getStates(),
        country.getStates()) && Objects.equals(getNationality(), country.getNationality())
        && Objects.equals(getTimezones(), country.getTimezones()) && Objects.equals(
        getTranslations(), country.getTranslations()) && Objects.equals(getLatitude(),
        country.getLatitude()) && Objects.equals(getLongitude(), country.getLongitude())
        && Objects.equals(getEmoji(), country.getEmoji()) && Objects.equals(getEmojiU(),
        country.getEmojiU());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getIso3(), getIso2(), getZoneName(), getPhoneCode(),
        getNumericCode(), getCapital(), getCurrency(), getCurrencyName(), getCurrencySymbol(),
        getTld(), getNativeName(), getRegion(), getRegionId(), getSubregion(), getSubregionId(),
        getStates(), getNationality(), getTimezones(), getTranslations(), getLatitude(),
        getLongitude(), getEmoji(), getEmojiU());
  }
}
