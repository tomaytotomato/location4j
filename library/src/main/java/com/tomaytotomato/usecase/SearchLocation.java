package com.tomaytotomato.usecase;

import com.tomaytotomato.model.Location;
import java.util.List;

public interface SearchLocation {

  /**
   * Searches for locations based off a text input e.g. - "Canada, Alberta" - "Santa Clara, CA, USA"
   * - "Glasgow, GB" - "Glasgow, Scotland" - "San Francisco"
   *
   * @param text free text input
   * @return list of locations that match text input
   */
  List<Location> search(String text);

}
