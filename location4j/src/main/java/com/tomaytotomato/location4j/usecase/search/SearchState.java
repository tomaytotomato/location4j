package com.tomaytotomato.location4j.usecase.search;

import com.tomaytotomato.location4j.model.search.StateResult;

import java.util.List;

public interface SearchState {

    List<StateResult> searchStates(String searchText);
}
