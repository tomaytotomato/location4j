package com.tomaytotomato.usecase;

import com.tomaytotomato.model.State;
import java.util.List;
import java.util.Optional;

/**
 * Interface for finding states based on various criteria.
 */
public interface FindState {

  /**
   * Find a state by its unique identifier (ID).
   *
   * @param id the unique identifier of the state
   * @return an Optional containing the State if found, otherwise an empty Optional
   */
  Optional<State> findStateById(Integer id);

  /**
   * Retrieve a list of all states that match the specified state name.
   *
   * @param name the name of the state
   * @return a List of State objects that match the specified state name
   */
  List<State> findAllStatesByStateName(String name);

  /**
   * Retrieve a list of all states that match the specified state code.
   *
   * @param stateCode the code of the state
   * @return a List of State objects that match the specified state code
   */
  List<State> findAllStatesByStateCode(String stateCode);

}