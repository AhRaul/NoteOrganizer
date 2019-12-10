package ru.reliableteam.noteorganizer.notes.notes_list_fragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 *  Note List Fragment
 */
public class NotesFragment extends Fragment implements View.OnClickListener {
    private final String CLASS_TAG = "NotesFragment";

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton writeNewNote;
    private MaterialButton sortNotes;
    private ImageButton closeBtn, deleteBtn, migrateBtn;
    private TextInputEditText searchNoteTv;

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
        writeNewNote = root.findViewById(R.id.notes_write_fab);
        writeNewNote.setOnClickListener(this);

        sortLayout = root.findViewById(R.id.sort_layout);
        sortNotes = root.findViewById(R.id.sort_notes_button);
        sortNotes.setOnClickListener(this);
        ChipGroup sortGroup = root.findViewById(R.id.sort_group);
        sortGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());

        searchNoteTv = root.findViewById(R.id.search_text_view);
        searchNoteTv.addTextChangedListener(getTextChangeListener());

        extraOptionsLayout = root.findViewById(R.id.extra_options_group);
        closeBtn = root.findViewById(R.id.close_button);
        deleteBtn = root.findViewById(R.id.delete_button);
        migrateBtn = root.findViewById(R.id.migrate_to_txt);

        closeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        migrateBtn.setOnClickListener(this);
    }


    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.notes_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new NotesRecyclerAdapter(presenter));
        recyclerView.addOnScrollListener(getRecyclerScrollListener());
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notes_write_fab:
                presenter.createNewNote();
                break;
            case R.id.sort_notes_button:
                presenter.enableSort();
                break;
            case R.id.close_button:
                presenter.disableMultiSelection();
                break;
            case R.id.delete_button:
                presenter.deleteNotes();
                break;
            case R.id.migrate_to_txt:
                presenter.migrateSelectedNotes();
                break;
        }
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
    public void setSelected(boolean isSelected, int position) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        MaterialCardView cardView = (MaterialCardView) manager.findViewByPosition(position);
        if (cardView != null) {
            cardView.setStrokeColor(getCardColor(isSelected));
            cardView.setSelected(isSelected);
        }
    }
    public void toDefaultStyle() {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        int count = manager.getItemCount();
        for (int position = 0; position < count; position++) {
            MaterialCardView cardView = (MaterialCardView) manager.findViewByPosition(position);
            if (cardView.isSelected()) {
                cardView.setStrokeColor(getCardColor(false));
                cardView.setSelected(false);
            }
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

    @Override
    public void onResume(){
        super.onResume();
        presenter.getNotes();
    }


    // todo потом перенесем в другой presenter (листенеры)
    /*
    ----------------------------------------------------------------------------------------------------
     */
    private RecyclerView.OnScrollListener getRecyclerScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fabVisibility = writeNewNote.getVisibility();
                if (dy != 0 && fabVisibility == View.VISIBLE)
                    writeNewNote.hide();

                Log.i(CLASS_TAG, "dy = " + dy);
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(CLASS_TAG, "new state = " + newState);
                int fabVisibility = writeNewNote.getVisibility();
                if (newState == 0 && fabVisibility != View.VISIBLE) // seemed to stop
                    writeNewNote.show();
            }
        };
    }

    private TextWatcher getTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchNotes(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private ChipGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sort_by_date:
                        // todo sort by date
                        System.out.println("SORT BY DATE");
                        break;
                    case  R.id.sort_by_title:
                        // todo sort by title
                        System.out.println("SORT BY TITLE");
                        break;
                    default:
                        // todo sort by date
                        System.out.println("DEFAULT: SORT BY DATE");
                }

            }
        };
    }
}
