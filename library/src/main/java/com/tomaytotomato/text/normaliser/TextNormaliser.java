package com.tomaytotomato.text.normaliser;

/**
 * Normalises text input before being used in lookups or searches
 */
public interface TextNormaliser {

  String normalise(String text);

}
