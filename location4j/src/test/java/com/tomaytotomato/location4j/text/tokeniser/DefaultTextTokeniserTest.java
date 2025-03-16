package com.tomaytotomato.location4j.text.tokeniser;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultTextTokeniserTest {

  private TextTokeniser textTokeniser = new DefaultTextTokeniser();

  @DisplayName("Tokenise, when text only has one word, then return one word")
  @Test
  void tokenise_WhenTextOnlyHasOneWord_ThenReturnOneToken() {

    // When
    var result = textTokeniser.tokenise("Glasgow");

    // Then
    assertThat(result).isNotEmpty().hasSize(1).contains("Glasgow");
  }

  @DisplayName("Tokenise, when text only has one word but lots of commas, then return one word")
  @Test
  void tokenise_WhenTextOnlyHasOneWordButLotsOfCommas_ThenReturnOneToken() {

    // When
    var result = textTokeniser.tokenise(",Aberdeen,,,,");

    // Then
    assertThat(result).isNotEmpty().hasSize(1).contains("Aberdeen");
  }

  @DisplayName("Tokenise, when text only has one word but lots of other random character, then return one word")
  @Test
  void tokenise_WhenTextOnlyHasOneWordButLotsOfRandomCharacters_ThenReturnOneToken() {

    // When
    var result = textTokeniser.tokenise(",!!!Dallas%&^^");

    // Then
    assertThat(result).isNotEmpty().hasSize(1).contains("Dallas");
  }


  @DisplayName("Tokenise, when text only has one word with valid characters, then return one word")
  @Test
  void tokenise_WhenTextHasMultipleWordsWithPrefixes_ThenReturnCorrectTokens() {

    // When Then
    assertThat(textTokeniser.tokenise("Santa Clara CA, United States")).containsExactly(
        "Santa Clara", "Clara CA", "CA United", "United States", "Santa", "Clara", "CA", "United",
        "States");
    assertThat(textTokeniser.tokenise("Santa Clara California USA")).containsExactly("Santa Clara",
        "Clara California", "California USA", "Santa", "Clara", "California", "USA");
    assertThat(textTokeniser.tokenise("Santa Clara, California, United States")).containsExactly(
        "Santa Clara", "Clara California", "California United", "United States", "Santa", "Clara",
        "California", "United", "States");
    assertThat(textTokeniser.tokenise("US CA Santa Clara")).containsExactly("US CA", "CA Santa",
        "Santa Clara", "US", "CA", "Santa", "Clara");
    assertThat(textTokeniser.tokenise("Tel Aviv Israel")).containsExactly("Tel Aviv", "Aviv Israel",
        "Tel", "Aviv", "Israel");
  }
}