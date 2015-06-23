package de.fabianparzefall.jraytracer.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility methods for strings.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public final class Strings {
    /**
     * Removes the given set of characters from the string.
     *
     * @param input      The string from which the characters should be removed.
     * @param characters The list of characters to remove.
     * @return The string without the characters.
     */
    public static String remove(final String input, final char... characters) {
        assert input != null;
        assert characters != null;

        final List<Character> characterList = new ArrayList<>();
        for (final char character : characters) {
            characterList.add(character);
        }

        final StringBuilder builder = new StringBuilder();
        for (final char character : input.toCharArray()) {
            if (!characterList.contains(character)) {
                builder.append(character);
            }
        }

        return builder.toString();
    }

    /**
     * Splits a string into an array after every space.
     *
     * @param input     The string to split.
     * @param delimiter The delimiter, after which a string should be splitted.
     * @return An array with the string parts.
     */
    public static String[] split(final String input, final char delimiter) {
        assert input != null;

        final ArrayList<String> items = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        for (final char character : input.toCharArray()) {
            if (character == ' ') {
                // Empty strings should be ignored.
                if (builder.length() != 0) {
                    items.add(builder.toString());
                    builder = new StringBuilder();
                }
            } else {
                builder.append(character);
            }
        }

        if (builder.length() != 0) {
            items.add(builder.toString());
        }

        return items.toArray(new String[items.size()]);
    }
}
