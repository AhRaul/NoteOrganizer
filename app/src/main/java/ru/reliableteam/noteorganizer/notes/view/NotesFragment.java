package ru.reliableteam.noteorganizer.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.presenter.NotesPresenter;


public class NotesFragment extends Fragment implements View.OnClickListener{

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton writeNewNote;
    private NotesPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notes, container, false);
        writeNewNote = root.findViewById(R.id.notes_write_fab);
        writeNewNote.setOnClickListener(this);
        presenter = new NotesPresenter(getContext(), this);

        initRecyclerView();

        return root;
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.notes_rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new MyAdapter(presenter));
    }

    public void notifyDataChanged() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notes_write_fab:
                Intent intent = new Intent(getActivity(), SingleNoteActivity.class);
                startActivity(intent);
//                NotesBottomDialogFragment addPhotoBottomDialogFragment = new NotesBottomDialogFragment();
//                addPhotoBottomDialogFragment.show(getFragmentManager(), "tag");
        }
    }

}
