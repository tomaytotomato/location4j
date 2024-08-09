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

  /**
   * Default constructor for Country.
   */
  Country() {}

  /**
   * Gets the unique identifier of the country.
   *
   * @return the country's unique ID
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the country.
   *
   * @param id the unique ID to set for the country
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of the country.
   *
   * @return the name of the country
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the country.
   *
   * @param name the name to set for the country
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the ISO 3166-1 alpha-3 code of the country.
   *
   * @return the country's ISO3 code
   */
  public String getIso3() {
    return iso3;
  }

  /**
   * Sets the ISO 3166-1 alpha-3 code of the country.
   *
   * @param iso3 the ISO3 code to set for the country
   */
  public void setIso3(String iso3) {
    this.iso3 = iso3;
  }

  /**
   * Gets the ISO 3166-1 alpha-2 code of the country.
   *
   * @return the country's ISO2 code
   */
  public String getIso2() {
    return iso2;
  }

  /**
   * Sets the ISO 3166-1 alpha-2 code of the country.
   *
   * @param iso2 the ISO2 code to set for the country
   */
  public void setIso2(String iso2) {
    this.iso2 = iso2;
  }

  /**
   * Gets the zone name of the country.
   *
   * @return the country's zone name
   */
  public String getZoneName() {
    return zoneName;
  }

  /**
   * Sets the zone name of the country.
   *
   * @param zoneName the zone name to set for the country
   */
  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  /**
   * Gets the phone code of the country.
   *
   * @return the country's phone code
   */
  public String getPhoneCode() {
    return phoneCode;
  }

  /**
   * Sets the phone code of the country.
   *
   * @param phoneCode the phone code to set for the country
   */
  public void setPhoneCode(String phoneCode) {
    this.phoneCode = phoneCode;
  }

  /**
   * Gets the numeric code of the country.
   *
   * @return the country's numeric code
   */
  public String getNumericCode() {
    return numericCode;
  }

  /**
   * Sets the numeric code of the country.
   *
   * @param numericCode the numeric code to set for the country
   */
  public void setNumericCode(String numericCode) {
    this.numericCode = numericCode;
  }

  /**
   * Gets the capital city of the country.
   *
   * @return the capital of the country
   */
  public String getCapital() {
    return capital;
  }

  /**
   * Sets the capital city of the country.
   *
   * @param capital the capital to set for the country
   */
  public void setCapital(String capital) {
    this.capital = capital;
  }

  /**
   * Gets the currency code of the country.
   *
   * @return the country's currency code
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * Sets the currency code of the country.
   *
   * @param currency the currency code to set for the country
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   * Gets the name of the country's currency.
   *
   * @return the name of the currency
   */
  public String getCurrencyName() {
    return currencyName;
  }

  /**
   * Sets the name of the country's currency.
   *
   * @param currencyName the name to set for the currency
   */
  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  /**
   * Gets the symbol of the country's currency.
   *
   * @return the currency symbol
   */
  public String getCurrencySymbol() {
    return currencySymbol;
  }

  /**
   * Sets the symbol of the country's currency.
   *
   * @param currencySymbol the symbol to set for the currency
   */
  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  /**
   * Gets the top-level domain (TLD) of the country.
   *
   * @return the country's TLD
   */
  public String getTld() {
    return tld;
  }

  /**
   * Sets the top-level domain (TLD) of the country.
   *
   * @param tld the TLD to set for the country
   */
  public void setTld(String tld) {
    this.tld = tld;
  }

  /**
   * Gets the native name of the country.
   *
   * @return the country's native name
   */
  public String getNativeName() {
    return nativeName;
  }

  /**
   * Sets the native name of the country.
   *
   * @param nativeName the native name to set for the country
   */
  public void setNativeName(String nativeName) {
    this.nativeName = nativeName;
  }

  /**
   * Gets the region where the country is located.
   *
   * @return the country's region
   */
  public String getRegion() {
    return region;
  }

  /**
   * Sets the region where the country is located.
   *
   * @param region the region to set for the country
   */
  public void setRegion(String region) {
    this.region = region;
  }

  /**
   * Gets the region ID where the country is located.
   *
   * @return the country's region ID
   */
  public String getRegionId() {
    return regionId;
  }

  /**
   * Sets the region ID where the country is located.
   *
   * @param regionId the region ID to set for the country
   */
  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  /**
   * Gets the subregion where the country is located.
   *
   * @return the country's subregion
   */
  public String getSubregion() {
    return subregion;
  }

  /**
   * Sets the subregion where the country is located.
   *
   * @param subregion the subregion to set for the country
   */
  public void setSubregion(String subregion) {
    this.subregion = subregion;
  }

  /**
   * Gets the subregion ID where the country is located.
   *
   * @return the country's subregion ID
   */
  public String getSubregionId() {
    return subregionId;
  }

  /**
   * Sets the subregion ID where the country is located.
   *
   * @param subregionId the subregion ID to set for the country
   */
  public void setSubregionId(String subregionId) {
    this.subregionId = subregionId;
  }

  /**
   * Gets the list of states within the country.
   *
   * @return a list of the country's states
   */
  public List<State> getStates() {
    return states;
  }

  /**
   * Sets the list of states within the country.
   *
   * @param states the list of states to set for the country
   */
  public void setStates(List<State> states) {
    this.states = states;
  }

  /**
   * Gets the nationality associated with the country.
   *
   * @return the country's nationality
   */
  public String getNationality() {
    return nationality;
  }

  /**
   * Sets the nationality associated with the country.
   *
   * @param nationality the nationality to set for the country
   */
  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  /**
   * Gets the list of time zones within the country.
   *
   * @return a list of the country's time zones
   */
  public List<TimeZone> getTimezones() {
    return timezones;
  }

  /**
   * Sets the list of time zones within the country.
   *
   * @param timezones the list of time zones to set for the country
   */
  public void setTimezones(List<TimeZone> timezones) {
    this.timezones = timezones;
  }

  /**
   * Gets the translations of the country's name.
   *
   * @return a map of translations for the country's name
   */
  public Map<String, String> getTranslations() {
    return translations;
  }

  /**
   * Sets the translations of the country's name.
   *
   * @param translations the map of translations to set for the country's name
   */
  public void setTranslations(Map<String, String> translations) {
    this.translations = translations;
  }

  /**
   * Gets the latitude of the country's geographic center.
   *
   * @return the country's latitude
   */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude of the country's geographic center.
   *
   * @param latitude the latitude to set for the country
   */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude of the country's geographic center.
   *
   * @return the country's longitude
   */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude of the country's geographic center.
   *
   * @param longitude the longitude to set for the country
   */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the emoji representing the country.
   *
   * @return the country's emoji
   */
  public String getEmoji() {
    return emoji;
  }

  /**
   * Sets the emoji representing the country.
   *
   * @param emoji the emoji to set for the country
   */
  public void setEmoji(String emoji) {
    this.emoji = emoji;
  }

  /**
   * Gets the Unicode representation of the country's emoji.
   *
   * @return the Unicode representation of the emoji
   */
  public String getEmojiU() {
    return emojiU;
  }

  /**
   * Sets the Unicode representation of the country's emoji.
   *
   * @param emojiU the Unicode representation to set for the emoji
   */
  public void setEmojiU(String emojiU) {
    this.emojiU = emojiU;
  }

  /**
   * Checks whether two country objects are equal based on their attributes.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
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

  /**
   * Computes the hash code for the country object based on its attributes.
   *
   * @return the hash code of the country
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getIso3(), getIso2(), getZoneName(), getPhoneCode(),
        getNumericCode(), getCapital(), getCurrency(), getCurrencyName(), getCurrencySymbol(),
        getTld(), getNativeName(), getRegion(), getRegionId(), getSubregion(), getSubregionId(),
        getStates(), getNationality(), getTimezones(), getTranslations(), getLatitude(),
        getLongitude(), getEmoji(), getEmojiU());
  }
}
