package com.tomaytotomato.text.tokeniser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixAwareTextTokeniser implements TextTokeniser {

    private static final Set<String> PREFIXES = new LinkedHashSet<>(Arrays.asList(
            "san", "los", "la", "el", "las", "st.", "st", "saint", "new", "district", "banileu",
            "fort", "port", "villa", "santa", "santo", "são", "ste.", "de", "del", "rio",
            "north", "south", "east", "west", "mount", "upper", "lower", "great", "little", "united"
    ));

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

    private boolean isPrefixWord(String word) {
        return PREFIXES.contains(word.toLowerCase());
    }
}
