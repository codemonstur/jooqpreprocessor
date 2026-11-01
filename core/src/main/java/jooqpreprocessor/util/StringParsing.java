package jooqpreprocessor.util;

/**
 * String scanning helpers used by the statement parsers.
 * <p>
 * Every search is escape-aware: a backslash escapes the character that follows it, and
 * brackets are matched so that separators nested inside strings, identifiers or
 * sub-expressions are not mistaken for top-level ones.
 */
public enum StringParsing {;

    /**
     * Returns the index of the first separator (comma) at nesting level zero at or after
     * {@code offset}, skipping over anything inside brackets, backticks or quotes.
     *
     * @param input the text to search
     * @param offset the index to start searching from
     * @return the index of the separator, or the index one past the end of the input when
     *         there is none, or {@code -1} when a bracket is never closed
     */
    public static int findNextSeparator(final String input, final int offset) {
        int i = offset;
        while (i != -1 && i < input.length()) {
            if (input.charAt(i) == ',') return i;

            if (input.charAt(i) == '(')
                i = skipPast(input, ')', i);
            else if (input.charAt(i) == '`')
                i = skipPast(input, '`', i);
            else if (input.charAt(i) == '\'')
                i = skipPast(input, '\'', i);
            else
                i = i + 1;
        }
        return i;
    }

    // Skips past the closing bracket that matches the opening one at position openIndex.
    // Returns -1 when the bracket is unterminated (so no further separator can be found).
    private static int skipPast(final String input, final char closing, final int openIndex) {
        final int closingIndex = unescapedIndexOf(input, closing, openIndex + 1);
        return closingIndex == -1 ? -1 : closingIndex + 1;
    }

    /**
     * Returns the index of the first occurrence of {@code c} at or after {@code offset},
     * treating a backslash-escaped character as ordinary text.
     *
     * @param input the text to search
     * @param c the character to look for
     * @param offset the index to start searching from
     * @return the index of the character, or {@code -1} when it does not occur
     */
    public static int unescapedIndexOf(final String input, final char c, final int offset) {
        int i = offset;
        while (i != -1 && i < input.length()) {
            if (input.charAt(i) == c) return i;
            i = input.charAt(i) == '\\' ? i+2 : i+1;
        }
        return -1;
    }

}
