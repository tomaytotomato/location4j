package com.tomaytotomato.location4j.text.tokeniser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A text tokeniser that is aware of prefixes, grouping words with recognized prefixes into tokens.
 * <p>
 * This class implements the {@link TextTokeniser} interface and provides functionality for breaking
 * a string of text into meaningful tokens, taking into account common prefixes.
 * </p>
 * <p>
 * @apiNote <strong>EXPERIMENTAL:</strong> This class is experimental and its behavior may
 * change in future releases without prior notice. Backward compatibility is not guaranteed.
 * </p>
 * <p>
 * @implNote Current implementation has known limitations and might produce unexpected results
 * in some edge cases, particularly with ambiguous prefixes. Users should thoroughly test in
 * their specific use cases.
 * </p>
 * <p>
 * For example, given the input "San Francisco New York", the tokeniser will produce:
 * <ul>
 *   <li>"San Francisco"</li>
 *   <li>"New York"</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 *   PrefixAwareTextTokeniser tokeniser = new PrefixAwareTextTokeniser();
 *   List&lt;String&gt; tokens = tokeniser.tokenise("San Francisco USA");
 *   // tokens: ["San Francisco", "USA"]
 * </pre>
 */
public class PrefixAwareTextTokeniser implements TextTokeniser {

  // A set of prefixes used to identify parts of names that should be tokenized together
  private static final Set<String> PREFIXES = new LinkedHashSet<>(Arrays.asList(
      "san", "los", "la", "el", "las", "st.", "st", "saint", "new", "district", "banileu",
      "fort", "port", "villa", "santa", "santo", "são", "ste.", "de", "del", "rio",
      "north", "south", "east", "west", "mount", "upper", "lower", "great", "little", "united"
  ));

  /**
   * Tokenises the input text into meaningful components, considering common prefixes.
   * <p>
   * This method first removes any non-alphanumeric characters except spaces, hyphens, apostrophes,
   * and periods. It then splits the text by whitespace and examines each part to determine if it
   * should be grouped with preceding parts based on recognized prefixes.
   * </p>
   * <p><strong>Note:</strong> This method is experimental and may not always produce the expected
   * results. Users should test it thoroughly in their specific contexts.</p>
   * <p>
   * Example usage:
   * <pre>
   *   PrefixAwareTextTokeniser tokeniser = new PrefixAwareTextTokeniser();
   *   List&lt;String&gt; tokens = tokeniser.tokenise("San Francisco New York");
   *   // tokens: ["San Francisco", "New York"]
   * </pre>
   *
   * @param text the input text to be tokenised
   * @return a list of tokens extracted from the input text, grouped by prefixes where applicable
   */
  @Override
  public List<String> tokenise(String text) {
    if (text == null || text.isEmpty()) {
      return new ArrayList<>();
    }

    text = text.replaceAll("[^\\p{L}\\p{N}\\s\\-'.]", " ").trim();
    String[] parts = text.split("\\s+");

    var tokenBuilder = new StringBuilder();

    List<String> tokenParts = Stream.of(parts)
        .flatMap(part -> {
          if (isPrefixWord(part) && !tokenBuilder.isEmpty()) {
            tokenBuilder.append(" ").append(part);
            return Stream.empty();
          } else {
            if (!tokenBuilder.isEmpty()) {
              String token = tokenBuilder.append(" ").append(part).toString();
              tokenBuilder.setLength(0);
              return Stream.of(token);
            } else if (isPrefixWord(part)) {
              tokenBuilder.append(part);
              return Stream.empty();
            } else {
              return Stream.of(part);
            }
          }
        })
        .collect(Collectors.toList());

    if (!tokenBuilder.isEmpty()) {
      tokenParts.add(tokenBuilder.toString());
    }

    return new ArrayList<>(tokenParts);
  }

  /**
   * Checks if a given word is a recognized prefix.
   *
   * @param word the word to check
   * @return true if the word is a prefix, false otherwise
   */
  private boolean isPrefixWord(String word) {
    return PREFIXES.contains(word.toLowerCase());
  }
}
