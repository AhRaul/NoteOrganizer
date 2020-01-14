package ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler;


import android.util.Log;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter.INotesPresenter;
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

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder> {
        private INotesPresenter presenter;

        public NotesRecyclerAdapter(INotesPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
            return new NotesViewHolder(view, presenter);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        @Override
        public void onBindViewHolder(NotesViewHolder holder, int position) {
            presenter.bindView(holder);
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements IViewHolder {
        private View itemView;
        private INotesPresenter presenter;

        private String CLASS_TAG = "MyViewHolder";

        private TextView title;
        private TextView subtitle;
        private TextView date;

        public NotesViewHolder(View view, INotesPresenter presenter) {
            super(view);
            this.itemView = view;
            this.presenter = presenter;

            init();
        }
        private void init() {
            initItemViewFields();
            initItemViewClickListeners();
        }
        private void initItemViewFields() {
            title = itemView.findViewById(R.id.note_item_title);
            subtitle = itemView.findViewById(R.id.note_item_subtitle);
            date = itemView.findViewById(R.id.note_item_data);
        }
        private void initItemViewClickListeners() {
            itemView.setOnClickListener( v -> presenter.clicked(getPos()) );
            itemView.setOnLongClickListener( v -> {
                presenter.longClicked(getPos(), itemView);
                return true;
            });
        }
        @Override
        public void setNote(Note note) {
            if (note.title.equals(""))
                title.setVisibility(View.GONE);
            else {
                title.setVisibility(View.VISIBLE);
                title.setText(note.title);
            }
            subtitle.setText(note.body);
            date.setText(DateUtils.dateToString(note.dataTime));
        }
        @Override
        public int getPos() { return getLayoutPosition(); }
    }
}
