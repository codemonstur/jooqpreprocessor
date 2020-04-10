package jooqpreprocessor.util;

public enum StringParsing {;

    public static int findNextSeparator(final String input, final int offset) {
        int i = offset;
        while (i != -1 && i < input.length()) {
            if (input.charAt(i) == ',') return i;

            if (input.charAt(i) == '(')
                i = unescapedIndexOf(input, ')', i+1) + 1;
            else if (input.charAt(i) == '`')
                i = unescapedIndexOf(input, '`', i+1) + 1;
            else if (input.charAt(i) == '\'')
                i = unescapedIndexOf(input, '\'',i+1) + 1;
            else
                i = i + 1;
        }
        return i;
    }

    public static int unescapedIndexOf(final String input, final char c, final int offset) {
        int i = offset;
        while (i != -1 && i < input.length()) {
            if (input.charAt(i) == c) return i;
            i = input.charAt(i) == '\\' ? i+2 : i+1;
        }
        return i;
    }

}
