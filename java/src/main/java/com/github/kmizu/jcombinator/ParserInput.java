package com.github.kmizu.jcombinator;

public class ParserInput {
    private final String input;
    private final int cursor;

    public ParserInput(String input, int cursor) {
        this.input = input;
        this.cursor = cursor;
    }
    public ParserInput(String input) {
        this(input, 0);
    }
    public String input() {
        return input;
    }
    public int cursor() {
        return cursor;
    }
    public ParserInput withCursor(int newCursor) {
        return new ParserInput(input, newCursor);
    }
    public ParserInput withInput(String newInput) {
        return new ParserInput(newInput, cursor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParserInput that = (ParserInput) o;

        if (cursor != that.cursor) return false;
        return input != null ? input.equals(that.input) : that.input == null;
    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + cursor;
        return result;
    }
}
