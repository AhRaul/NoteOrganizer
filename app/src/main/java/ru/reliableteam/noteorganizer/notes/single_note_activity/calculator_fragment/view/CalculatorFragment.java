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


public class CalculatorFragment extends DialogFragment {

    private View calc;
    private CalcPresenter calcPresenter;

    private TextInputEditText tvOutResult;
    private TextView tvExpress;
    private TextView tvResult;
    private Spinner spinner;

    public void setTvOutResult(TextInputEditText tvOutResult) {
        this.tvOutResult = tvOutResult;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calc = inflater.inflate(R.layout.fragment_calculator, container, false);
        calcPresenter = new CalcPresenter(this);

        calcPresenter.setSavedArguments(getArguments().getStringArrayList("page_m_values"));

        initUI();

        return calc;
    }

    // view initialization
    private void initUI() {
        initBrackets();
        initButtonsNumeric();
        initButtonsArithmetic();
        initButtonClear();
        initButtonCancel();
        initButtonBackspace();
        initButtonEquality();
        initButtonSetResultAndQuit();
        initExpressionAndResultTv();
        initButtonCalcMemory();
    }
    private void initExpressionAndResultTv() {
        tvExpress = calc.findViewById(R.id.tv_express);
        tvResult = calc.findViewById(R.id.tv_result);
    }
    private void initButtonsArithmetic() {
        Button btnMultiplication = calc.findViewById(R.id.btn_multiplication);
        btnMultiplication.setOnClickListener(this::addToExpression);
        Button btnDivison = calc.findViewById(R.id.btn_division);
        btnDivison.setOnClickListener(this::addToExpression);
        Button btnPlus = calc.findViewById(R.id.btn_plus);
        btnPlus.setOnClickListener(this::addToExpression);
        Button btnMinus = calc.findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(this::addToExpression);
    }
    private void initButtonsNumeric() {
        Button btnZero = calc.findViewById(R.id.btn_zero);
        btnZero.setOnClickListener(this::addToExpression);
        Button btnOne = calc.findViewById(R.id.btn_one);
        btnOne.setOnClickListener(this::addToExpression);
        Button btnTwo = calc.findViewById(R.id.btn_two);
        btnTwo.setOnClickListener(this::addToExpression);
        Button btnThree = calc.findViewById(R.id.btn_three);
        btnThree.setOnClickListener(this::addToExpression);
        Button btnFour = calc.findViewById(R.id.btn_four);
        btnFour.setOnClickListener(this::addToExpression);
        Button btnFive = calc.findViewById(R.id.btn_five);
        btnFive.setOnClickListener(this::addToExpression);
        Button btnSix = calc.findViewById(R.id.btn_six);
        btnSix.setOnClickListener(this::addToExpression);
        Button btnSeven = calc.findViewById(R.id.btn_seven);
        btnSeven.setOnClickListener(this::addToExpression);
        Button btnEight = calc.findViewById(R.id.btn_eight);
        btnEight.setOnClickListener(this::addToExpression);
        Button btnNine = calc.findViewById(R.id.btn_nine);
        btnNine.setOnClickListener(this::addToExpression);
        Button btnSeparator = calc.findViewById(R.id.btn_separator);
        btnSeparator.setOnClickListener(this::addToExpression);
    }
    private void initBrackets() {
        Button btnBracketLeft = calc.findViewById(R.id.btn_left_bracket);
        btnBracketLeft.setOnClickListener(this::addToExpression);
        Button btnBracketRight = calc.findViewById(R.id.btn_right_bracket);
        btnBracketRight.setOnClickListener(this::addToExpression);
    }
    private void initButtonClear() {
        Button btnClear = calc.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> {
            calcPresenter.setExpress("");
            tvExpress.setText(calcPresenter.getExpress());
        });
    }
    private void initButtonCancel() {
        ImageButton cancelBtn = calc.findViewById(R.id.btn_cancel_calc);
        cancelBtn.setOnClickListener(v -> dismiss() );
    }
    private void initButtonBackspace() {
        Button btnBackSpace = calc.findViewById(R.id.btn_del_num);
        btnBackSpace.setOnClickListener( v -> deleteSymbols());
    }
    private void initButtonEquality() {
        Button btnEqually = calc.findViewById(R.id.btn_equally);
        btnEqually.setOnClickListener( v -> solveExpression() );
    }
    private void initButtonSetResultAndQuit() {
        Button btnSetResultAndQuit = calc.findViewById(R.id.btn_set_result);
        btnSetResultAndQuit.setOnClickListener( v -> getResultAndQuit() );
    }

    private void initButtonCalcMemory() {
        Button btnCalcMemory = calc.findViewById(R.id.btn_calc_memory);
        btnCalcMemory.setOnClickListener(this::addToExpression);
    }

    private void addToExpression(View v) {
        if (v.getId() == R.id.btn_division ||
                v.getId() == R.id.btn_minus ||
                v.getId() == R.id.btn_plus ||
                v.getId() == R.id.btn_multiplication)
            checkPreviousInput();

        Button b = calc.findViewById(v.getId());
        calcPresenter.buildExpress(b.getText().toString());
        calcPresenter.correctExpress();
        setExpressionTextSize();
        calcPresenter.limitLenForExpress();
        tvExpress.setText(calcPresenter.getExpress());
    }
    private void setExpressionTextSize() {
        String result = calcPresenter.getExpress();
        if (result.length() > 20){
            tvExpress.setTextSize(16);
        } else {
            tvExpress.setTextSize(24);
        }
    }
    private boolean isNumeric(String s) throws NumberFormatException{
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void deleteSymbols() {
        calcPresenter.deleteSymbol();
        setExpressionTextSize();
        tvExpress.setText(calcPresenter.getExpress());
    }
    private void solveExpression() {
        String result = calcPresenter.getResult();
        if (result.length() > 16) {
            tvResult.setTextSize(24);
        }
        tvResult.setText(result);
        if (isNumeric(result)) {
            calcPresenter.setExpress(result);
        }
    }
    private void getResultAndQuit() {
        int endSelectionIdx = tvOutResult.getSelectionEnd();
        String result = appendResultWithNoteText(endSelectionIdx);
        tvOutResult.setText(result);
        tvOutResult.setSelection(endSelectionIdx + tvResult.getText().toString().length());
        dismiss();
    }

    /**
     * Получение значения, имеющего метку (например: #M1 500 )
     * Условия получения значения: после метки #M[целое число] должен стоять пробел,
     * далее считываемое число целое или дробное без букв или символов, далее пробел.
     * Если условия нарушены, метка не будет считана.
     * Разрешенные символы: "-", ".", ",", цифры.
     */
    private void getMemoryPoint() {

    }

    private String appendResultWithNoteText(int endSelectionIdx) {
        StringBuilder resultStringBuilder = new StringBuilder();

        String text = tvOutResult.getText().toString();

        if (text.isEmpty())
            resultStringBuilder.append(tvResult.getText().toString());
        else {
            String textBeforeIndex = text.substring(0, endSelectionIdx);
            String textAfterIndex = text.substring(tvOutResult.getSelectionEnd());
            resultStringBuilder
                    .append(textBeforeIndex)
                    .append(tvResult.getText().toString())
                    .append(textAfterIndex);
        }

        return resultStringBuilder.toString();
    }

    public void setExpression(String text) {
        tvExpress.setText(text);
    }

    private void checkPreviousInput() {
        System.out.println("CHECK");
        calcPresenter.checkInputAndReplace(tvExpress.getText().toString());
        // нужна проверка, что нет вводимого ключа
        // добавить в паттерн - точку и запятую
    }
}
