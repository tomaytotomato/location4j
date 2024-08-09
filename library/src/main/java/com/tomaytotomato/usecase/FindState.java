package com.tomaytotomato.usecase;

import com.tomaytotomato.model.State;
import java.util.List;
import java.util.Optional;

public interface FindState {

  Optional<State> findStateById(Integer id);

  List<State> findAllStatesByStateName(String name);

  List<State> findAllStatesByStateCode(String stateCode);

}
