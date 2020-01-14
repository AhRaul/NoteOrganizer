package ru.reliableteam.noteorganizer.notes.notes_list_fragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter.INotesPresenter;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter.NotesPresenter;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler.NotesRecyclerAdapter;
import ru.reliableteam.noteorganizer.notes.single_note_activity.view.SingleNoteActivity;
import ru.reliableteam.noteorganizer.utils.ScreenUtil;

/**
 *  Note List Fragment
 */
public class NotesFragment extends Fragment {
    private final String CLASS_TAG = "NotesFragment";

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton writeNewNote;
    private MaterialButton sortNotes;
    private TextInputEditText searchNoteTv;
    MaterialButton clearSearch;

    private INotesPresenter presenter;

    private LinearLayoutCompat sortLayout;
    private ConstraintLayout extraOptionsLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        System.out.println("ON CREATE");
        root = inflater.inflate(R.layout.fragment_notes, container, false);
        presenter = new NotesPresenter(this);

        initUI();
        initRecyclerView();

        return root;
    }

    private void initUI() {
        initWriteNoteFab();
        initSortLayoutAndGroup();
        initSearchNoteLayout();
        initExtraOptionsLayout();
    }
    private void initWriteNoteFab() {
        writeNewNote = root.findViewById(R.id.notes_write_fab);
        writeNewNote.setOnClickListener( v -> presenter.createNewNote() );
    }
    private void initSortLayoutAndGroup() {
        sortLayout = root.findViewById(R.id.sort_layout);
        sortNotes = root.findViewById(R.id.sort_notes_button);
        sortNotes.setOnClickListener(
                v -> {
                    if (sortNotes.isChecked())
                        presenter.enableSort();
                    else
                        presenter.disableSort();
                }
        );
        ChipGroup sortGroup = root.findViewById(R.id.sort_group);
        sortGroup.setOnCheckedChangeListener(presenter.getOnCheckedChangeListener());
    }
    private void initSearchNoteLayout() {
        clearSearch = root.findViewById(R.id.clear_search);
        clearSearch.setOnClickListener( v -> clearSearch() );
        clearSearch.setVisibility(View.GONE);

        searchNoteTv = root.findViewById(R.id.search_text_view);
        searchNoteTv.addTextChangedListener(presenter.getTextChangeListener(clearSearch));
    }
    private void initExtraOptionsLayout() {
        extraOptionsLayout = root.findViewById(R.id.extra_options_group);
        ImageButton closeBtn = root.findViewById(R.id.close_button);
        ImageButton deleteBtn = root.findViewById(R.id.delete_button);
        ImageButton migrateBtn = root.findViewById(R.id.migrate_to_txt);
        closeBtn.setOnClickListener(v -> presenter.disableMultiSelection() );
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder db = new AlertDialog.Builder(getContext());
            db.setTitle(R.string.delete_hint);
            db.setPositiveButton(R.string.positive, (dialog, which) -> {
                presenter.deleteNotes();
            });
            db.setNegativeButton(R.string.negative, (dialog, which) -> dialog.dismiss());
            db.show();
        });
        migrateBtn.setOnClickListener(v -> presenter.migrateSelectedNotes() );
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.notes_rv);
        int spanCount = ScreenUtil.getDisplayColumns(getActivity());
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new NotesRecyclerAdapter(presenter));
        recyclerView.addOnScrollListener(getRecyclerScrollListener());

        presenter.getNotes();
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

    public String getSearchText() {
        if (searchNoteTv == null)
            return "";
        return searchNoteTv.getText().toString();
    }

    public void showToast(int messageId) {
        String s = getResources().getString(messageId);
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private RecyclerView.OnScrollListener getRecyclerScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fabVisibility = writeNewNote.getVisibility();
                if (dy != 0 && fabVisibility == View.VISIBLE)
                    writeNewNote.hide();
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int STATE_STOP = 0;
                int fabVisibility = writeNewNote.getVisibility();
                if (newState == STATE_STOP && fabVisibility != View.VISIBLE)
                    writeNewNote.show();
            }
        };
    }

    private void clearSearch() {
        searchNoteTv.setText("");
    }
}
