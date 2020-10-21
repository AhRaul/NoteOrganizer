package ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler;


import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.jetbrains.annotations.NotNull;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.todos_fragment.presenter.ITodoPresenter;
import ru.reliableteam.noteorganizer.utils.DateUtils;

/**
 * Base Adapter for recycler.
 *
 * Implements Base methods of adapter for recycler view.
 *
 * Methods to add:
 *  -   onClick in MyViewHolder to open Note
 *  -   onLongClick to select Note for deleting it
 */

public class TodosRecyclerAdapter extends RecyclerView.Adapter<TodosVH> {
        private final ITodoPresenter presenter;

        public TodosRecyclerAdapter(ITodoPresenter presenter) {
            this.presenter = presenter;
        }

        @NotNull
        @Override
        public TodosVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
            return new TodosVH(view, presenter);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        @Override
        public void onBindViewHolder(@NotNull TodosVH holder, int position) {
            presenter.bindView(holder);
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
}
