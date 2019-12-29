package ru.reliableteam.noteorganizer.todos.view.recycler;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
//        private TextView subtitle;
//        private ImageView image;

        public TodosViewHolder(View view, ITodoPresenter presenter) {
            super(view);
            this.itemView = view;
            this.presenter = presenter;

            init();
//            itemView.setOnClickListener(shortClickListener);
//            itemView.setOnLongClickListener(longClickListener);
        }

        private void init() {
            title = itemView.findViewById(R.id.todo_item_title);
            dateEnd = itemView.findViewById(R.id.todo_date_end);
//            subtitle = itemView.findViewById(R.id.note_item_subtitle);
//            image = itemView.findViewById(R.id.note_item_image);
        }
        @Override
        public void setTodo(Todo todo) {
            title.setText(todo.title);
            if (todo.endDate != 0) {
                dateEnd.setVisibility(View.VISIBLE);
                dateEnd.setText(DateUtils.dateToString(todo.endDate));
            }
//            subtitle.setText(note.body);
//            if (note.cardImageUri != 0)
//                image.setImageResource(note.cardImageUri);
//            else
//                subtitle.setMaxLines(15);
        }
        @Override
        public int getPos() { return getLayoutPosition(); }

//        private final View.OnLongClickListener longClickListener = v -> {
//            System.out.println(getPos());
//            presenter.longClicked(getPos(), itemView);
//            return true;
//        };
//        private final View.OnClickListener shortClickListener = v -> {
//            Log.i(CLASS_TAG, "clicked " + getPos());
//            presenter.clicked(getPos());
//        };
    }
}
