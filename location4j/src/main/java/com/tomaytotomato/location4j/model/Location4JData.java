package com.tomaytotomato.location4j.model;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Holds all location data which is de/serialized from the location4j.bin file
 * Note: this class is mutable and is not thread-safe, data structures should be
 * copied and not accessed directly.
 */
public class Location4JData implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private List<Country> countries;

  private Map<String, Country> countryNameToCountryMap;
  private Map<String, Country> countryNativeNameToCountryMap;
  private Map<Integer, Country> countryIdToCountryMap;
  private Map<String, Country> localisedNameToCountryMap;
  private Map<String, Country> iso2CodeToCountryMap;
  private Map<String, Country> iso3CodeToCountryMap;

  private Map<Integer, State> stateIdToStateMap;
  private Map<String, List<State>> stateNameToStatesMap;
  private Map<String, List<State>> stateNativeNameToStateMap;
  private Map<String, List<State>> stateIso2CodeToStateMap;
  private Map<String, State> stateIso361662ToStateMap;

  private Map<Integer, City> cityIdToCityMap;
  private Map<String, List<City>> cityNameToCitiesMap;

  private Map<String, List<City>> searchCityNameToCitiesMap;

  public Location4JData() {
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  public Map<String, Country> getCountryNameToCountryMap() {
    return countryNameToCountryMap;
  }

  public void setCountryNameToCountryMap(
      Map<String, Country> countryNameToCountryMap) {
    this.countryNameToCountryMap = countryNameToCountryMap;
  }

  public Map<String, Country> getCountryNativeNameToCountryMap() {
    return countryNativeNameToCountryMap;
  }

  public void setCountryNativeNameToCountryMap(
      Map<String, Country> countryNativeNameToCountryMap) {
    this.countryNativeNameToCountryMap = countryNativeNameToCountryMap;
  }

  public Map<Integer, Country> getCountryIdToCountryMap() {
    return countryIdToCountryMap;
  }

  public void setCountryIdToCountryMap(
      Map<Integer, Country> countryIdToCountryMap) {
    this.countryIdToCountryMap = countryIdToCountryMap;
  }

  public Map<String, Country> getLocalisedNameToCountryMap() {
    return localisedNameToCountryMap;
  }

  public void setLocalisedNameToCountryMap(
      Map<String, Country> localisedNameToCountryMap) {
    this.localisedNameToCountryMap = localisedNameToCountryMap;
  }

  public Map<String, Country> getIso2CodeToCountryMap() {
    return iso2CodeToCountryMap;
  }

  public void setIso2CodeToCountryMap(
      Map<String, Country> iso2CodeToCountryMap) {
    this.iso2CodeToCountryMap = iso2CodeToCountryMap;
  }

  public Map<String, Country> getIso3CodeToCountryMap() {
    return iso3CodeToCountryMap;
  }

  public void setIso3CodeToCountryMap(
      Map<String, Country> iso3CodeToCountryMap) {
    this.iso3CodeToCountryMap = iso3CodeToCountryMap;
  }

  public Map<Integer, State> getStateIdToStateMap() {
    return stateIdToStateMap;
  }

  public void setStateIdToStateMap(
      Map<Integer, State> stateIdToStateMap) {
    this.stateIdToStateMap = stateIdToStateMap;
  }

  public Map<String, List<State>> getStateNameToStatesMap() {
    return stateNameToStatesMap;
  }

  public void setStateNameToStatesMap(
      Map<String, List<State>> stateNameToStatesMap) {
    this.stateNameToStatesMap = stateNameToStatesMap;
  }

  public Map<String, List<State>> getStateNativeNameToStateMap() {
    return stateNativeNameToStateMap;
  }

  public void setStateNativeNameToStateMap(
      Map<String, List<State>> stateNativeNameToStateMap) {
    this.stateNativeNameToStateMap = stateNativeNameToStateMap;
  }

  public Map<String, List<State>> getStateIso2CodeToStateMap() {
    return stateIso2CodeToStateMap;
  }

  public void setStateIso2CodeToStatesMap(
      Map<String, List<State>> stateIso2CodeToStateMap) {
    this.stateIso2CodeToStateMap = stateIso2CodeToStateMap;
  }

  public Map<String, State> getStateIso361662ToStateMap() {
    return stateIso361662ToStateMap;
  }

  public void setStateIso361662ToStateMap(
      Map<String, State> stateIso361662ToStateMap) {
    this.stateIso361662ToStateMap = stateIso361662ToStateMap;
  }

  public Map<Integer, City> getCityIdToCityMap() {
    return cityIdToCityMap;
  }

  public void setCityIdToCityMap(
      Map<Integer, City> cityIdToCityMap) {
    this.cityIdToCityMap = cityIdToCityMap;
  }

  public Map<String, List<City>> getCityNameToCitiesMap() {
    return cityNameToCitiesMap;
  }

  public void setCityNameToCitiesMap(
      Map<String, List<City>> cityNameToCitiesMap) {
    this.cityNameToCitiesMap = cityNameToCitiesMap;
  }

  public void setSearchCityNameToCitiesMap(
      Map<String, List<City>> searchCityNameToCitiesMap) {
    this.searchCityNameToCitiesMap = searchCityNameToCitiesMap;
  }
}

