package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter.CalcPresenter;

public class CalculatorFragmentInitialize extends DialogFragment {

    protected View calc;
    protected CalcPresenter calcPresenter;

    protected TextInputEditText tvOutResult;
    protected EditText tvExpress;
    protected TextView tvResult;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calc = inflater.inflate(R.layout.fragment_calculator, container, false);
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

        tvExpress.clearFocus();

        tvExpress.setOnClickListener(v -> {
            System.out.println("click");
            hideKeyBoard();
        });
        tvExpress.setOnFocusChangeListener( (v, isFocused) -> {
            System.out.println("FOCUS");
            if (isFocused) {
                tvExpress.requestFocus();
                tvExpress.setShowSoftInputOnFocus(false);
                hideKeyBoard();
            }
        });
    }
    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view != null && imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        btnEqually.setOnClickListener( v -> {
            checkPreviousInput();
            solveExpression();
        } );
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
        if (calcPresenter.isActionButton(v)) {
            checkPreviousInput();
        }

        Button b = calc.findViewById(v.getId());
        workWithExpression(b.getTag().toString());
    }

    private void workWithExpression(@Nullable String tag) {
        if (tag == null) return;

        int insertIdx = tvExpress.getSelectionStart();

        calcPresenter.buildExpress(tag, insertIdx, tvExpress.hasFocus());

        calcPresenter.correctExpress();
        calcPresenter.setExpressionTextSize();
        calcPresenter.limitLenForExpress();

        tvExpress.setText(calcPresenter.getExpress());

        if (tvExpress.hasFocus()) {
            tvExpress.setSelection(insertIdx == 0 ? 0 : insertIdx + 1);
        }
    }

    private void deleteSymbols() {
        int tvSelectionIdx = tvExpress.getSelectionStart();
        calcPresenter.deleteSymbol(tvSelectionIdx);
        calcPresenter.setExpressionTextSize();
        tvExpress.setText(calcPresenter.getExpress());
        tvExpress.setSelection(calcPresenter.getPointer(tvSelectionIdx));
        if (tvExpress.getText().length() == 0) {
            System.out.println("clr");
            tvExpress.clearFocus();
        }
    }
    private void solveExpression() {
        String result = calcPresenter.getResult();
        if (result.length() > 16) {
            tvResult.setTextSize(24);
        }
        tvResult.setText(result);
        if (calcPresenter.isNumeric(result)) {
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
    public void setTextSize(int size) {
        tvExpress.setTextSize(size);
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

    /**
     * Получение значения, имеющего метку (например: #M1 500 )
     * Условия получения значения: после метки #M[целое число] должен стоять пробел,
     * далее считываемое число целое или дробное без букв или символов, далее пробел.
     * Если условия нарушены, метка не будет считана.
     * Разрешенные символы: "-", ".", ",", цифры.
     */
    private void checkPreviousInput() {
        calcPresenter.checkInputAndReplace();
        // нужна проверка, что нет вводимого ключа
        // добавить в паттерн - точку и запятую
    }

}
