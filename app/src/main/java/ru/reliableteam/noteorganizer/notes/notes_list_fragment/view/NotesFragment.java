package ru.reliableteam.noteorganizer.notes.notes_list_fragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.view.SingleNoteActivity;

/**
 *  Note List Fragment
 */
public class NotesFragment extends NotesFragmentInitialize {
    private final String CLASS_TAG = "NotesFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initPresenter(this);
        super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void setSortLayoutVisibility(boolean isVisible) {
        sortNotes.setChecked(isVisible);
        sortLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setExtraOptionsLayoutVisibility(boolean isVisible) {
        extraOptionsLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void viewNote() {
        Intent intent = new Intent(getActivity(), SingleNoteActivity.class);
        startActivity(intent);
    }

    // card styles
    private void setCardStyle(boolean isSelected, int position, RecyclerView.LayoutManager manager) {
        MaterialCardView cardView = (MaterialCardView) manager.findViewByPosition(position);
        if (cardView != null) {
            cardView.setStrokeWidth(isSelected ? 8 : 4);
            cardView.setStrokeColor(getCardColor(isSelected));
            cardView.setSelected(isSelected);
        }
    }
    public void setCardSelected(boolean isSelected, int position) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;

        setCardStyle(isSelected, position, manager);
    }
    public void setCardsToDefaultStyle() {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;

        convertListItemsToDefaultStyle(manager);
    }
    private void convertListItemsToDefaultStyle(RecyclerView.LayoutManager manager) {
        int count = manager.getItemCount();
        for (int i = 0; i < count; i++) {
            setCardSelected(false, i);
        }
    }
    private int getCardColor(boolean isSelected) {
        return isSelected ? getResources().getColor(R.color.cardStrokeSelected) : getResources().getColor(R.color.cardStrokeDefault);
    }

    public void clearSearch() {
        searchNoteTv.setText("");
    }

    public String getSearchText() {
        if (searchNoteTv == null)
            return "";
        return searchNoteTv.getText().toString();
    }

    public void showToast(int messageId) {
        String s = getResources().getString(messageId);
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("RESUME");
        presenter.getNotes();
    }
}
