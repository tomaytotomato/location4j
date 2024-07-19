package com.tomaytotomato.usecase;

import com.tomaytotomato.model.Location;

import java.util.List;

public interface Search {

    /**
     * Searches for locations based off a text input
     * e.g. - "Canada, Alberta"
     *      - "Santa Clara, CA, USA"
     *      - "Glasgow, GB"
     *      - "San Francisco"
     * @param text free text input
     * @return list of locations that match text input
     */
    List<Location> search(String text);

}
