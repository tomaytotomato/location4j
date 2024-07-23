package com.tomaytotomato.text.normaliser;

import java.util.Objects;

public class DefaultTextNormaliser implements TextNormaliser {

    @Override
    public String normalise(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        return text.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
