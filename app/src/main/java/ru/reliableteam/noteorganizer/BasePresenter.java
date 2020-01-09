package ru.reliableteam.noteorganizer;

public interface BasePresenter {
    default void notifyDatasetChanged(int messageId) { };
    default void notifyItemChanged(int position) { }
}
