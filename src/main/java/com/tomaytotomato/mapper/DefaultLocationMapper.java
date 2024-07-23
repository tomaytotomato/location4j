package com.tomaytotomato.mapper;

import com.tomaytotomato.model.City;
import com.tomaytotomato.model.Country;
import com.tomaytotomato.model.Location;
import com.tomaytotomato.model.State;

public class DefaultLocationMapper implements LocationMapper {

    @Override
    public Location toLocation(Country country) {
        return Location.builder()
                .countryName(country.getName())
                .countryId(country.getId())
                .countryIso2Code(country.getIso2())
                .countryIso3Code(country.getIso3())
                .latitude(country.getLatitude())
                .longitude(country.getLongitude())
                .build();
    }

    @Override
    public Location toLocation(State state) {
        return Location.builder()
                .countryName(state.getCountryName())
                .countryId(state.getCountryId())
                .countryIso2Code(state.getCountryIso2Code())
                .countryIso3Code(state.getCountryIso3Code())
                .stateId(state.getId())
                .state(state.getName())
                .stateCode(state.getStateCode())
                .cityId(state.getId())
                .latitude(state.getLatitude())
                .longitude(state.getLongitude())
                .build();
    }

    @Override
    public Location toLocation(City city) {
        return Location.builder()
                .countryName(city.getCountryName())
                .countryId(city.getCountryId())
                .countryIso2Code(city.getCountryIso2Code())
                .countryIso3Code(city.getCountryIso3Code())
                .stateId(city.getStateId())
                .state(city.getStateName())
                .stateCode(city.getStateCode())
                .cityId(city.getId())
                .city(city.getName())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .build();
    }
}
