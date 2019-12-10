package ru.reliableteam.noteorganizer.sort;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Note> listDoc;

    private static Comparator<Note> comparator = SortListComparator
            .getDateComparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_sort_name: {
            item.setChecked(true);
            comparator = SortListComparator.getNameComparator();
            sort();
            return true;
            }

            case R.id.menu_sort_date: {
                item.setChecked(true);
                comparator = SortListComparator.getDateComparator();
                sort();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sort() {
        Collections.sort(listDoc, comparator);
        updateIndexes();

        sortAdapter = new SortAdapter(this, listDoc);
        listView.setAdapter(sortAdapter);

        //sortAdapter.getFilter().filter(txtSearch.getText());
    }

    private void updateIndexes() {
        int i = 0;
        for (Note note : list ) {
            note.setNumber(i++);
        }
    }


}
