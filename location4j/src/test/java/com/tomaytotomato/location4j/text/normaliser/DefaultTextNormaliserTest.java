package com.tomaytotomato.location4j.text.normaliser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultTextNormaliserTest {

  private final TextNormaliser textNormaliser = new DefaultTextNormaliser();

  @DisplayName("Normalise, when text is null, then throw IllegalArgumentException")
  @Test
  void normalise_WhenTextIsNull_ThenThrowIllegalArgumentException() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> textNormaliser.normalise(null));
  }

  @DisplayName("Normalise, when text is empty, then throw IllegalArgumentException")
  @Test
  void normalise_WhenTextIsEmpty_ThenThrowIllegalArgumentException() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> textNormaliser.normalise(""));
  }

  @DisplayName("Normalise, when text has leading and trailing spaces, then return trimmed and lowercased text")
  @Test
  void normalise_WhenTextHasLeadingAndTrailingSpaces_ThenReturnTrimmedAndLowercasedText() {
    // When
    var result = textNormaliser.normalise("  Hello World  ");

    // Then
    assertThat(result).isEqualTo("hello world");
  }

  @DisplayName("Normalise, when text is already lowercased and trimmed, then return the same text")
  @Test
  void normalise_WhenTextIsAlreadyLowercasedAndTrimmed_ThenReturnTheSameText() {
    // When
    var result = textNormaliser.normalise("hello world");

    // Then
    assertThat(result).isEqualTo("hello world");
  }

  @DisplayName("Normalise, when text is mixed case, then return lowercased text")
  @Test
  void normalise_WhenTextIsMixedCase_ThenReturnLowercasedText() {
    // When
    var result = textNormaliser.normalise("HeLLo WoRLD");

    // Then
    assertThat(result).isEqualTo("hello world");
  }

  @DisplayName("Normalise, when text has no leading or trailing spaces, then return lowercased text")
  @Test
  void normalise_WhenTextHasNoLeadingOrTrailingSpaces_ThenReturnLowercasedText() {
    // When
    var result = textNormaliser.normalise("HelloWorld");

    // Then
    assertThat(result).isEqualTo("helloworld");
  }

  @DisplayName("Normalise, when text has multiple spaces between words, then return lowercased text with spaces minimised to one")
  @Test
  void normalise_WhenTextHasMultipleSpacesBetweenWords_ThenReturnLowercasedTextWithOneSpace() {
    // When
    var result = textNormaliser.normalise("Hello    World");

    // Then
    assertThat(result).isEqualTo("hello world");
  }

  @DisplayName("Normalise, when text contains punctuation, then return lowercased text without punctuation")
  @Test
  void normalise_WhenTextContainsPunctuation_ThenReturnLowercasedTextWithPunctuationIntact() {
    // When
    var result = textNormaliser.normalise("San Francisco, CA, USA");

    // Then
    assertThat(result).isEqualTo("san francisco ca usa");
  }
}
