package ru.reliableteam.noteorganizer.notes.model;

public class Span {
    public int start = -1;
    public int end = -1;
    public int style = -1;

    public Span(int start, int end, int style) {
        this.start = start;
        this.end = end;
        this.style = style;
    }
}
