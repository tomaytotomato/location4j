package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.model.search.SearchLocationResult;
import java.util.List;

/**
 * Interface for searching locations based on a free-text input.
 */
public interface SearchLocation {

  /**
   * Searches for locations based on a free-text input.
   * <p>
   * This method accepts input strings that can be formatted in various ways, such as:
   * <ul>
   *   <li>"Canada, Alberta"</li>
   *   <li>"Santa Clara, CA, USA"</li>
   *   <li>"Glasgow, GB"</li>
   *   <li>"Glasgow, Scotland"</li>
   *   <li>"San Francisco"</li>
   * </ul>
   * The input can be formatted or unformatted, for example:
   * <ul>
   *   <li>"uk, glasgow"</li>
   *   <li>"glasgow UNITED KINGDOM"</li>
   *   <li>"USA san Francisco"</li>
   *   <li>"san Francisco United States"</li>
   * </ul>
   * The method normalizes and tokenizes the input to find matching locations.
   *
   * @param text the free-text input used to search for locations
   * @return a List of {@link SearchLocationResult} objects that match the input text
   */
  List<SearchLocationResult> search(String text);

}
