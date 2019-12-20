package ru.reliableteam.noteorganizer.utils;

import java.util.Comparator;

import ru.reliableteam.noteorganizer.notes.model.Note;

public class SortListComparator {

    private static final Object obj = new Object();
    private static NameComparator nameComparator;
    private static DateComparator dateComparator;
    private static NumberComparator numberComparator;


    public static synchronized Comparator<Note> getNameComparator() {
        synchronized (obj) {
            if (nameComparator == null) {
                nameComparator = new NameComparator();
            }
            return nameComparator;
        }
    }


    public static synchronized Comparator<Note> getDateComparator() {
        if (dateComparator == null) {
            dateComparator = new DateComparator();
        }
        return dateComparator;
    }

    public static synchronized Comparator<Note> getNumberComparator() {
        if (numberComparator == null) {
            numberComparator = new NumberComparator();
        }
        return numberComparator;
    }

    private static class NameComparator implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.title.compareTo(o2.title);

        }
    }

    private static class DateComparator implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.dataTime > o2.dataTime ? 1 : 0;
        }
    }

    private static class NumberComparator implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.id > o2.id ? 1 : 0;
        }
    }

}
