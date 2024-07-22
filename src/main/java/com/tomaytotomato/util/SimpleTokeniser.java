package com.tomaytotomato.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SimpleTokeniser implements TextTokeniser {

    @Override
    public List<String> tokenise(String text) {

        if (Objects.isNull(text) || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tokens = new ArrayList<>();
        text = text.replaceAll("[^\\p{L}\\p{N}\\s\\-'.]", " ").trim();

        String[] parts = text.split("\\s+");

        // Generate combinations with preceding words
        for (int i = 0; i < parts.length - 1; i++) {
            tokens.add(parts[i] + " " + parts[i + 1]);
        }

        // Add all individual words
        tokens.addAll(Arrays.asList(parts));

        return tokens;
    }
}
