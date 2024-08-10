package com.tomaytotomato.text.tokeniser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Default text tokeniser, splits text into pairs based on spacing.
 * <p>
 * This class implements the {@link TextTokeniser} interface to provide functionality for breaking a
 * string of text into smaller components, or "tokens." The tokenisation is done by splitting the
 * text into overlapping pairs of words, and then adding individual words. This is useful for search
 * and indexing purposes.
 * </p>
 * <p>
 * For example, given the input "San Francisco USA", the tokeniser will produce:
 * <ul>
 *   <li>"San Francisco"</li>
 *   <li>"Francisco USA"</li>
 *   <li>"San"</li>
 *   <li>"Francisco"</li>
 *   <li>"USA"</li>
 * </ul>
 */
public class DefaultTextTokeniser implements TextTokeniser {

  /**
   * Tokenises the input text into pairs of adjacent words, followed by individual words.
   * <p>
   * This method first removes any non-alphanumeric characters except spaces, hyphens, apostrophes,
   * and periods. Then, it splits the cleaned text by whitespace and forms overlapping pairs of
   * words. Finally, it adds each word individually to the list of tokens.
   * </p>
   * <p>
   * Example usage:
   * <pre>
   *   DefaultTextTokeniser tokeniser = new DefaultTextTokeniser();
   *   List&lt;String&gt; tokens = tokeniser.tokenise("San Francisco USA");
   *   // tokens: ["San Francisco", "Francisco USA", "San", "Francisco", "USA"]
   * </pre>
   *
   * @param text the input text to be tokenised
   * @return a list of tokens extracted from the input text
   */
  @Override
  public List<String> tokenise(String text) {

    if (Objects.isNull(text) || text.isEmpty()) {
      return new ArrayList<>();
    }

    List<String> tokens = new ArrayList<>();
    text = text.replaceAll("[^\\p{L}\\p{N}\\s\\-'.]", " ").trim();

    String[] parts = text.split("\\s+");

    for (int i = 0; i < parts.length - 1; i++) {
      tokens.add(parts[i] + " " + parts[i + 1]);
    }

    tokens.addAll(Arrays.asList(parts));
    return tokens;
  }
}
