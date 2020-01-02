package ru.reliableteam.noteorganizer;

public interface BasePresenter {
    void notifyDatasetChanged(int messageId);
    default void notifyItemChanged(int position) { }
}
