package com.tomaytotomato.util;


import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultTextTokeniserTest {

    private TextTokeniser textTokeniser = new DefaultTextTokeniser();

    @Description("Tokenise, when text only has one word, then return one word")
    @Test
    void tokenise_WhenTextOnlyHasOneWord_ThenReturnOneToken() {

        // When
        var result = textTokeniser.tokenise("Glasgow");

        // Then
        assertThat(result).isNotEmpty().hasSize(1).contains("Glasgow");
    }

    @Description("Tokenise, when text only has one word but lots of commas, then return one word")
    @Test
    void tokenise_WhenTextOnlyHasOneWordButLotsOfCommas_ThenReturnOneToken() {

        // When
        var result = textTokeniser.tokenise(",Aberdeen,,,,");

        // Then
        assertThat(result).isNotEmpty().hasSize(1).contains("Aberdeen");
    }

    @Description("Tokenise, when text only has one word but lots of other random character, then return one word")
    @Test
    void tokenise_WhenTextOnlyHasOneWordButLotsOfRandomCharacters_ThenReturnOneToken() {

        // When
        var result = textTokeniser.tokenise(",!!!Dallas%&^^");

        // Then
        assertThat(result).isNotEmpty().hasSize(1).contains("Dallas");
    }

    @Description("Tokenise, when text has one or more words, then return appropriate amount of tokens")
    @ParameterizedTest
    @CsvSource({
            "Aberdeen, Aberdeen",
            "Dallas, Dallas",
            "London, London",
            "Paris, Paris",
            "Tokyo, Tokyo",
            "Guinea-Bissau, Guinea-Bissau",
            "Saint-Pierre and Miquelon, Saint-Pierre|and|Miquelon",
            "Bosnia-Herzegovina, Bosnia-Herzegovina",
            "St John's, St John's",
            "N'Djamena, N'Djamena",
            "O'Fallon, O'Fallon",
            "D'Iberville, D'Iberville",
            "St. Louis, St. Louis",
            "St. John's, St. John's",
            "Washington D.C., Washington|D.C.",
            "San Juan Capistrano, San Juan|Capistrano",
            "New York, New York",
            "Los Angeles, Los Angeles",
            "Rio de Janeiro, Rio de Janeiro",
            "Buenos Aires, Buenos|Aires",
            "St. John's-Newfoundland, St. John's-Newfoundland",
            "San Antonio, San Antonio",
            "Baden-Baden, Baden-Baden",
            "Aix-en-Provence, Aix-en-Provence",
            "La Coruña, La Coruña",
            "District 9, District 9",
            "Sector 7G, Sector|7G",
            "Area 51, Area|51",
            "\"&^%St John's!!!\", St John's",
            "Guinea-Bissau@@, Guinea-Bissau",
            "\"***New York&&&\", New York",
            "\"Los**Angeles^^^\", Los Angeles",
    })
    void tokenise_WhenTextHasValidWordsWithPrefixes_ThenReturnCorrectTokens(String text, String expectedTokens) {

        // When
        var result = textTokeniser.tokenise(text);

        // Then
        var expectedNames = expectedTokens.split("\\|");
        assertThat(result).isNotEmpty().hasSize(expectedNames.length).contains(expectedNames);
    }

    @Description("Tokenise, when text only has one word with valid characters, then return one word")
    @Test
    void tokenise_WhenTextHasMultipleWordsWithPrefixes_ThenReturnCorrectTokens() {

        // When Then
        assertThat(textTokeniser.tokenise("Santa Clara CA, United States")).containsAll(Stream.of("Santa Clara", "CA", "United States").toList());
        assertThat(textTokeniser.tokenise("Santa Clara California USA")).containsAll(Stream.of("Santa Clara", "California", "USA").toList());
        assertThat(textTokeniser.tokenise("Santa Clara, California, United States")).containsAll(Stream.of("Santa Clara", "California", "United States").toList());
        assertThat(textTokeniser.tokenise("US CA Santa Clara")).containsAll(Stream.of("Santa Clara", "CA", "US").toList());
        assertThat(textTokeniser.tokenise("Tel Aviv Israel")).containsAll(Stream.of("Aviv", "Tel", "Israel").toList());
    }

}