package ru.reliableteam.noteorganizer.ui.notes;


import android.graphics.Point;
import android.util.Log;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.reliableteam.noteorganizer.R;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private NotesPresenter presenter;

        public MyAdapter(NotesPresenter presenter) {
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private NotesPresenter presenter;

        private String CLASS_TAG = "MyViewHolder";

//        private ImageView image = itemView.findViewById(R.id.image);
//        private var width =  screenSize.x / if (isPortrait) 2 else 3 //for horizontal
//        private var height: Int = screenSize.y / 3

        private TextView title;
        private TextView subtitle;
        private ImageView image;

        public MyViewHolder(View view, NotesPresenter presenter) {
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
//        Picasso.get().load(sourceUrl).placeholder(R.drawable.placeholder).into(image)
        }

        public int getPos() { return getPosition(); }

        @Override
        public void onClick(View v) {
            Log.i(CLASS_TAG, "clicked " + getPos());
//            presenter.clicked(getPos());
        }
    }
}
