package ru.reliableteam.noteorganizer.todos.view.recycler;


import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter.INotesPresenter;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.presenter.ITodoPresenter;
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

public class TodosRecyclerAdapter extends RecyclerView.Adapter<TodosRecyclerAdapter.TodosViewHolder> {
        private ITodoPresenter presenter;

        public TodosRecyclerAdapter(ITodoPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public TodosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
            return new TodosViewHolder(view, presenter);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        @Override
        public void onBindViewHolder(TodosViewHolder holder, int position) {
            presenter.bindView(holder);
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }


    public class TodosViewHolder extends RecyclerView.ViewHolder implements IViewHolder {
        private View itemView;
        private ITodoPresenter presenter;

        private String CLASS_TAG = "MyViewHolder";

        private TextView title, dateEnd;
        private MaterialCheckBox checkBoxDone;

        public TodosViewHolder(View view, ITodoPresenter presenter) {
            super(view);
            this.itemView = view;
            this.presenter = presenter;

            init();
            itemView.setOnClickListener(v -> presenter.clicked(getPos()));
            itemView.setOnLongClickListener( v -> {
                presenter.longClicked(getPos());
                return true;
            });
            checkBoxDone.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> presenter.makeTodoDone(getPos(), checkBoxDone.isChecked())
            );
        }

        private void init() {
            title = itemView.findViewById(R.id.todo_item_title);
            dateEnd = itemView.findViewById(R.id.todo_date_end);
            checkBoxDone = itemView.findViewById(R.id.todo_item_checkbox);
        }
        @Override
        public void setTodo(Todo todo) {
            setDate(todo.endDate);
            checkBoxDone.setChecked(todo.isDone);
            setTitle(todo.title, todo.isDone);
        }
        @Override
        public int getPos() { return getLayoutPosition(); }

        private void setDate(Long endDate) {
            if (DateUtils.isDateConfigured(endDate)) {
                dateEnd.setVisibility(View.VISIBLE);
                dateEnd.setText(DateUtils.dateToString(endDate));
            } else {
                dateEnd.setVisibility(View.GONE);
            }
        }
        private void setTitle(String title_, boolean isDone) {
            Editable e = new SpannableStringBuilder(title_);
            if (isDone) {
                e.setSpan(new StrikethroughSpan(), 0, title_.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                e.removeSpan(new StrikethroughSpan());
            }

            title.setText(e);
        }
    }
}
