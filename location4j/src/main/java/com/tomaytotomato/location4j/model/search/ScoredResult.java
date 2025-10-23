package com.tomaytotomato.location4j.model.search;

/**
 * A search result paired with its relevance score.
 */
public record ScoredResult(SearchLocationResult result, int score) {
}