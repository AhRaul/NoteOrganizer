package ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.chip.ChipGroup;

import ru.reliableteam.noteorganizer.R;

public class NotesListenersProvider {
    private INotesSortingPresenter presenter;

    public NotesListenersProvider(INotesSortingPresenter iNotesPresenter) {
        this.presenter = iNotesPresenter;
    }

    public TextWatcher getTextChangeListener(View buttonClear) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearButtonVisibility(s);
                presenter.searchNotes(s.toString());
            }
            private void setClearButtonVisibility(CharSequence s) {
                if (s.length() == 0) {
                    buttonClear.setVisibility(View.GONE);
                } else {
                    if (buttonClear.getVisibility() != View.VISIBLE)
                        buttonClear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public ChipGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (group, checkedId) -> {
            switch (checkedId) {
                case R.id.sort_by_date:
                    presenter.sortByDate();
                    break;
                case  R.id.sort_by_title:
                    presenter.sortByTitle();
                    break;
                default:
                    presenter.sortByDefault();
                    break;
            }
        };
    }
}
