package ru.reliableteam.noteorganizer.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.presenter.INotesPresenter;
import ru.reliableteam.noteorganizer.notes.presenter.NotesPresenter;

/**
 *  Note List Fragment
 */
public class NotesFragment extends Fragment implements View.OnClickListener {
    private final String CLASS_TAG = "NotesFragment";

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton writeNewNote;
    private MaterialButton sortNotes;
    private INotesPresenter presenter;
    private LinearLayoutCompat sortLayout;
    TextInputEditText searchNoteTv;

    private final int NEW_NOTE = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notes, container, false);
        presenter = new NotesPresenter(this);

        initUI();
        initRecyclerView();

        return root;
    }

    private void initUI() {
        writeNewNote = root.findViewById(R.id.notes_write_fab);

        sortLayout = root.findViewById(R.id.sort_layout);
        sortNotes = root.findViewById(R.id.sort_notes_button);
        sortNotes.setOnClickListener(this);

        ChipGroup sortGroup = root.findViewById(R.id.sort_group);
        sortGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());

        searchNoteTv = root.findViewById(R.id.search_text_view);
        searchNoteTv.addTextChangedListener(getTextChangeListener());

        writeNewNote.setOnClickListener(this);
    }


    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.notes_rv);
//        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new MyAdapter(presenter));
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
                presenter.clicked(NEW_NOTE);
                break;
            case R.id.sort_notes_button:
                setSortLayoutVisibility();
        }
    }

    private void setSortLayoutVisibility() {
        sortLayout.setVisibility(sortNotes.isChecked() ? View.VISIBLE : View.GONE);
    }

    public void viewNote() {
//        presenter.saveToDir();
        Intent intent = new Intent(getActivity(), SingleNoteActivity.class);
        startActivity(intent);
    }

    public String getSearchText() {
        if (searchNoteTv == null)
            return "";
        return searchNoteTv.getText().toString();
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.getNotes();
        System.out.println("ON RESUME");
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
