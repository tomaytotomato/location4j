package com.tomaytotomato.text.normaliser;

import java.util.Objects;

/**
 * Default text normaliser, lowercases text, removes unnecessary punctuation characters and spaces
 */
public class DefaultTextNormaliser implements TextNormaliser {

  @Override
  public String normalise(String text) {
    if (Objects.isNull(text) || text.isEmpty()) {
      throw new IllegalArgumentException("Text cannot be null or empty");
    }
    return text.trim()
        .toLowerCase()
        .replaceAll("\\p{Punct}", "")
        .replaceAll("\\s+", " ");
  }
}
