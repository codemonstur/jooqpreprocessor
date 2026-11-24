package jooqpreprocessor.db;

/**
 * Rewrites one kind of SQL statement into the form jOOQ can parse.
 */
public interface StatementParser {

    /**
     * Tells whether this parser handles the given statement.
     *
     * @param statement the trimmed statement to inspect
     * @return {@code true} when {@link #convert(String)} knows how to rewrite it
     */
    boolean matches(String statement);

    /**
     * Rewrites the given statement into jOOQ-parsable SQL.
     *
     * @param statement the statement {@link #matches(String)} accepted
     * @return the rewritten statement, or an empty string when jOOQ should not see it at all
     */
    String convert(String statement);
}
