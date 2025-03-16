package com.tomaytotomato.location4j.text.tokeniser;

import java.util.List;

/**
 * Tokenises text for use in location searching
 */
public interface TextTokeniser {

  List<String> tokenise(String text);

}
