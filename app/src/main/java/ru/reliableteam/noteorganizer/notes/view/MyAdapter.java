package ru.reliableteam.noteorganizer.notes.view;


import android.util.Log;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.presenter.INotesPresenter;
import ru.reliableteam.noteorganizer.notes.presenter.NotesPresenter;

/**
 * Base Adapter for recycler.
 *
 * Implements Base methods of adapter for recycler view.
 *
 * Methods to add:
 *  -   onClick in MyViewHolder to open Note
 *  -   onLongClick to select Note for deleting it
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private INotesPresenter presenter;

        public MyAdapter(INotesPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
            return new MyViewHolder(view, presenter);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            presenter.bindView(holder);
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private INotesPresenter presenter;

        private String CLASS_TAG = "MyViewHolder";

        private TextView title;
        private TextView subtitle;
        private ImageView image;

        public MyViewHolder(View view, INotesPresenter presenter) {
            super(view);
            this.itemView = view;
            this.presenter = presenter;

            init();
            itemView.setOnClickListener(this);
        }

        private void init() {
            title = itemView.findViewById(R.id.note_item_title);
            subtitle = itemView.findViewById(R.id.note_item_subtitle);
            image = itemView.findViewById(R.id.note_item_image);
        }

        public void setNote(Note note) {
            title.setText(note.title);
            subtitle.setText(note.body);
            if (note.cardImageUri != 0)
                image.setImageResource(note.cardImageUri);
            else
                subtitle.setMaxLines(15);
        }

        public int getPos() { return getPosition(); }

        @Override
        public void onClick(View v) {
            Log.i(CLASS_TAG, "clicked " + getPos());
            // todo add click interaction to open clicked item in SingleNoteActivity
             presenter.clicked(getPos());
            // in presenter:
            //      noteList.getNote(position).getId()
            //      send this id to activity through extras or save in SharedPreferences and get
            //      that id from SP in activity
        }
    }
}
