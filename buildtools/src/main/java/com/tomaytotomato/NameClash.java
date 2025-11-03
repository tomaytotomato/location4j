package com.tomaytotomato;

import java.util.ArrayList;
import java.util.List;

public class NameClash {

  private String name;
  private List<String> countries = new ArrayList<>();
  private List<String> states = new ArrayList<>();
  private List<String> cities = new ArrayList<>();

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public List<String> getCountries() {
	return countries;
  }

  public void setCountries(List<String> countries) {
	this.countries = countries;
  }

  public List<String> getStates() {
	return states;
  }

  public void setStates(List<String> states) {
	this.states = states;
  }

  public List<String> getCities() {
	return cities;
  }

  public void setCities(List<String> cities) {
	this.cities = cities;
  }

  void addCountry(String country) {
	countries.add(country);
  }

  void addState(String state, String country) {
	states.add(state + " (in " + country + ")");
  }

  void addCity(String city, String state, String country) {
	cities.add(city + " (in " + state + ", " + country + ")");
  }

  public boolean hasClash() {
	int types = 0;
	if (!countries.isEmpty()) {
	  types++;
	}
	if (!states.isEmpty()) {
	  types++;
	}
	if (!cities.isEmpty()) {
	  types++;
	}
	return types > 1;
  }

  public String getSeverity() {
	if (!countries.isEmpty() && !states.isEmpty() && !cities.isEmpty()) {
	  return "CRITICAL (Country + State + City)";
	} else if (!countries.isEmpty() && !cities.isEmpty()) {
	  return "HIGH (Country + City)";
	} else if (!countries.isEmpty() && !states.isEmpty()) {
	  return "MEDIUM (Country + State)";
	} else if (!states.isEmpty() && !cities.isEmpty()) {
	  return "LOW (State + City)";
	}
	return "NONE";
  }

  @Override
  public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("\n  NAME: ").append(name);
	sb.append("\n  SEVERITY: ").append(getSeverity());
	if (!countries.isEmpty()) {
	  sb.append("\n  → Countries: ").append(countries);
	}
	if (!states.isEmpty()) {
	  sb.append("\n  → States: ").append(states);
	}
	if (!cities.isEmpty()) {
	  sb.append("\n  → Cities: ").append(cities);
	}
	return sb.toString();
  }
}
