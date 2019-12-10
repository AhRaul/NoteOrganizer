package ru.reliableteam.noteorganizer.sort;

import java.util.Comparator;

public class SortListComparator {

    private static NameComparator nameComparator;
    private static DateComparator dateComparator;


    public static Comparator<Note> getNameComparator() {
        if (nameComparator == null) {
            nameComparator = new NameComparator();
        }
        return nameComparator;
    }


    public static Comparator<Note> getDateComparator() {
        if (dateComparator == null) {
            dateComparator = new DateComparator();
        }
        return dateComparator;
    }

    private static class NameComparator implements Comparator<Note> {
        @Override
        public int compare(Note lhs, Note rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());

        }
    }

    private static class DateComparator implements Comparator<Note> {
        @Override
        public int compare(Note lhs, Note rhs) {
            return rhs.getDataTime().compareTo(lhs.getDataTime());
        }
    }

}
