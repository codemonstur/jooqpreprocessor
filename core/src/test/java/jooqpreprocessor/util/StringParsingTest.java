package jooqpreprocessor.util;

import org.junit.jupiter.api.Test;

import static jooqpreprocessor.util.StringParsing.findNextSeparator;
import static jooqpreprocessor.util.StringParsing.unescapedIndexOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringParsingTest {

    @Test
    public void unescapedIndexOfReturnsIndexWhenFound() {
        assertEquals(2, unescapedIndexOf("ab`c", '`', 0));
    }

    @Test
    public void unescapedIndexOfReturnsMinusOneWhenMissing() {
        assertEquals(-1, unescapedIndexOf("abc", '`', 0));
    }

    @Test
    public void unescapedIndexOfSkipsEscapedCharacters() {
        // "a\'bc'": the backslash escapes the quote at index 2, so the real quote is at index 5
        assertEquals(5, unescapedIndexOf("a\\'bc'", '\'', 0));
    }

    @Test
    public void findNextSeparatorFindsTopLevelComma() {
        // "a (b,c), d": the comma at index 4 is inside brackets, the top-level one is at index 7
        assertEquals(7, findNextSeparator("a (b,c), d", 0));
    }

    @Test
    public void findNextSeparatorSkipsCommasInsideBrackets() {
        // "(a,b) , c": the comma at index 2 is inside brackets, the top-level one is at index 6
        assertEquals(6, findNextSeparator("(a,b) , c", 0));
    }

    @Test
    public void findNextSeparatorReturnsMinusOneWhenUnterminatedBracket() {
        // Previously this returned length+1, causing substring to throw
        assertEquals(-1, findNextSeparator("a (b, c", 0));
    }

    @Test
    public void findNextSeparatorReturnsLengthWhenNoMoreSeparators() {
        assertEquals(7, findNextSeparator("a (b) c", 0));
    }
}
