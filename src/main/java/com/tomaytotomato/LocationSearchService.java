package com.tomaytotomato;

import com.tomaytotomato.model.Location;
import com.tomaytotomato.usecase.Search;
import com.tomaytotomato.util.TextTokeniser;

import java.util.List;

public class LocationSearchService implements Search {

    private final LocationService locationService;
    private final TextTokeniser textTokeniser;

    public LocationSearchService(LocationService locationService, TextTokeniser textTokeniser) {
        this.locationService = locationService;
        this.textTokeniser = textTokeniser;
    }

    @Override
    public List<Location> search(String text) {
        return List.of();
    }
}
