package com.example.library.model;

import com.google.common.collect.ImmutableSet;

public final class DataInitializer {

    private DataInitializer() {
    }

    public static final ImmutableSet<String> AUTHOR_NAMES = ImmutableSet.of(
            "Ursula K. Le Guin",
            "Andrzej Sapkowski"
    );

    public static final ImmutableSet<String> BOOK_NAMES = ImmutableSet.of(
            "A Wizard of Earthsea",
            "The Left Hand of Darkness",
            "The Last Wish"
    );
}

