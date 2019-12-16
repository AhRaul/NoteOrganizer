package ru.reliableteam.noteorganizer.todos.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.presenter.TodoPresenter;
import ru.reliableteam.noteorganizer.todos.view.recycler.TodosRecyclerAdapter;

public class TodosFragment extends Fragment {

    private TodoPresenter presenter;

    private View root;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todos, container, false);

        presenter = new TodoPresenter(this);
        initRecyclerView();
        presenter.getTodos();

        return root;
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.todos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodosRecyclerAdapter(presenter));
//        recyclerView.addOnScrollListener(getRecyclerScrollListener());
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
