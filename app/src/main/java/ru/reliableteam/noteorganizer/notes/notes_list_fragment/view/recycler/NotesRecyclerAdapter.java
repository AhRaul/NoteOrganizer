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
        private ImageView image;

        public NotesViewHolder(View view, INotesPresenter presenter) {
            super(view);
            this.itemView = view;
            this.presenter = presenter;

            init();
            itemView.setOnClickListener(shortClickListener);
            itemView.setOnLongClickListener(longClickListener);
        }

        private void init() {
            title = itemView.findViewById(R.id.note_item_title);
            subtitle = itemView.findViewById(R.id.note_item_subtitle);
            image = itemView.findViewById(R.id.note_item_image);
        }
        @Override
        public void setNote(Note note) {
            title.setText(note.title);
            subtitle.setText(note.body);
            if (note.cardImageUri != 0)
                image.setImageResource(note.cardImageUri);
            else
                subtitle.setMaxLines(15);
        }
        @Override
        public int getPos() { return getLayoutPosition(); }

        private final View.OnLongClickListener longClickListener = v -> {
            System.out.println(getPos());
            presenter.longClicked(getPos(), itemView);
            return true;
        };
        private final View.OnClickListener shortClickListener = v -> {
            Log.i(CLASS_TAG, "clicked " + getPos());
            presenter.clicked(getPos());
        };
    }
}
