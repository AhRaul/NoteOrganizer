package ru.reliableteam.noteorganizer.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowId;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.reliableteam.noteorganizer.R;

public class NotesFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private NotesPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notes, container, false);
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

}
