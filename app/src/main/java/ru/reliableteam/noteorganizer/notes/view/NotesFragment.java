package ru.reliableteam.noteorganizer.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private INotesPresenter presenter;

    private final int NEW_NOTE = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notes, container, false);
        writeNewNote = root.findViewById(R.id.notes_write_fab);

        writeNewNote.setOnClickListener(this);
        presenter = new NotesPresenter(this);

        initRecyclerView();

        return root;
    }


    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.notes_rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new MyAdapter(presenter));

        recyclerView.addOnScrollListener(getRecyclerScrollListener());
    }

    public void notifyDataChanged() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notes_write_fab:
                presenter.clicked(NEW_NOTE);
                break;
        }
    }

    public void viewNote() {
        Intent intent = new Intent(getActivity(), SingleNoteActivity.class);
        startActivity(intent);
//                NotesBottomDialogFragment addPhotoBottomDialogFragment = new NotesBottomDialogFragment();
//                addPhotoBottomDialogFragment.show(getFragmentManager(), "tag");
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.getNotes();
        System.out.println("ON RESUME");
    }

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
}
