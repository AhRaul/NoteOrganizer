package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter.CalcPresenter;


public class CalculatorFragment extends CalculatorFragmentInitialize {

    public void setTvOutResult(TextInputEditText tvOutResult) {
        this.tvOutResult = tvOutResult;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        calcPresenter = new CalcPresenter(this);
        calcPresenter.setSavedArguments(getArguments().getStringArrayList("page_m_values"));

        return calc;
    }



    public void setExpression(String text) {
        tvExpress.setText(text);
    }

    public void showError() {
        tvResult.setText("ERROR");
    }
    public void setTvResult(String res) {
        tvResult.setText(res);
    }
}
