package com.tomaytotomato.location4j.model;

import com.tomaytotomato.location4j.model.lookup.City;
import com.tomaytotomato.location4j.model.lookup.Country;
import com.tomaytotomato.location4j.model.lookup.State;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class containing pre-built data structures for location4j.
 * This class is serialized to the binary file to avoid runtime data structure building.
 */
public class Location4JData implements Serializable {

  private static final long serialVersionUID = 1L;

  // Raw data
  private List<Country> countries;

  // LocationService data structures
  private Map<String, Country> countryNameToCountryMap;
  private Map<Integer, Country> countryIdToCountryMap;
  private Map<String, Country> localisedNameToCountryMap;
  private Map<String, Country> iso2CodeToCountryMap;
  private Map<String, Country> iso3CodeToCountryMap;
  private Map<Integer, State> stateIdToStateMap;
  private Map<Integer, City> cityIdToCityMap;
  private Map<String, List<State>> stateNameToStatesMap;
  private Map<String, List<State>> stateCodeToStatesMap;
  private Map<String, List<City>> cityNameToCitiesMap;

  // SearchLocationService additional data structures
  private Map<String, List<City>> searchCityNameToCitiesMap;

  public Location4JData() {
  }

  // Getters and setters
  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  public Map<String, Country> getCountryNameToCountryMap() {
    return countryNameToCountryMap;
  }

  public void setCountryNameToCountryMap(Map<String, Country> countryNameToCountryMap) {
    this.countryNameToCountryMap = countryNameToCountryMap;
  }

  public Map<Integer, Country> getCountryIdToCountryMap() {
    return countryIdToCountryMap;
  }

  public void setCountryIdToCountryMap(Map<Integer, Country> countryIdToCountryMap) {
    this.countryIdToCountryMap = countryIdToCountryMap;
  }

  public Map<String, Country> getLocalisedNameToCountryMap() {
    return localisedNameToCountryMap;
  }

  public void setLocalisedNameToCountryMap(Map<String, Country> localisedNameToCountryMap) {
    this.localisedNameToCountryMap = localisedNameToCountryMap;
  }

  public Map<String, Country> getIso2CodeToCountryMap() {
    return iso2CodeToCountryMap;
  }

  public void setIso2CodeToCountryMap(Map<String, Country> iso2CodeToCountryMap) {
    this.iso2CodeToCountryMap = iso2CodeToCountryMap;
  }

  public Map<String, Country> getIso3CodeToCountryMap() {
    return iso3CodeToCountryMap;
  }

  public void setIso3CodeToCountryMap(Map<String, Country> iso3CodeToCountryMap) {
    this.iso3CodeToCountryMap = iso3CodeToCountryMap;
  }

  public Map<Integer, State> getStateIdToStateMap() {
    return stateIdToStateMap;
  }

  public void setStateIdToStateMap(Map<Integer, State> stateIdToStateMap) {
    this.stateIdToStateMap = stateIdToStateMap;
  }

  public Map<Integer, City> getCityIdToCityMap() {
    return cityIdToCityMap;
  }

  public void setCityIdToCityMap(Map<Integer, City> cityIdToCityMap) {
    this.cityIdToCityMap = cityIdToCityMap;
  }

  public Map<String, List<State>> getStateNameToStatesMap() {
    return stateNameToStatesMap;
  }

  public void setStateNameToStatesMap(Map<String, List<State>> stateNameToStatesMap) {
    this.stateNameToStatesMap = stateNameToStatesMap;
  }

  public Map<String, List<State>> getStateCodeToStatesMap() {
    return stateCodeToStatesMap;
  }

  public void setStateCodeToStatesMap(Map<String, List<State>> stateCodeToStatesMap) {
    this.stateCodeToStatesMap = stateCodeToStatesMap;
  }

  public Map<String, List<City>> getCityNameToCitiesMap() {
    return cityNameToCitiesMap;
  }

  public void setCityNameToCitiesMap(Map<String, List<City>> cityNameToCitiesMap) {
    this.cityNameToCitiesMap = cityNameToCitiesMap;
  }

  public Map<String, List<City>> getSearchCityNameToCitiesMap() {
    return searchCityNameToCitiesMap;
  }

  public void setSearchCityNameToCitiesMap(Map<String, List<City>> searchCityNameToCitiesMap) {
    this.searchCityNameToCitiesMap = searchCityNameToCitiesMap;
  }
}

