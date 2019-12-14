package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter.CalcPresenter;


// todo вылетает при клике на TextView
public class CalculatorFragment extends DialogFragment implements View.OnClickListener {

    private View calc;
    private Button btnBracketLeft;
    private Button btnBracketRight;
    private Button btnClear;
    private Button btnbackSpace;
    private Button btnZero;
    private  Button btnOne;
    private  Button btnTwo;
    private  Button btnThree;
    private  Button btnFour;
    private  Button btnFive;
    private  Button btnSix;
    private  Button btnSeven;
    private  Button btnEight;
    private  Button btnNine;
    private  Button btnMultyplication;
    private  Button btnDivison;
    private   Button btnPlus;
    private  Button btnMinus;
    private  Button btnSeparator;
    private  Button btnEqually;
    private  TextView tvExpress;
    private  TextView tvResult;

    private ImageButton cancelBtn;

    private CalcPresenter calcPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calc = inflater.inflate(R.layout.fragment_calculator, container, false);
        calcPresenter = new CalcPresenter();

        initUI();

        return calc;
    }

    private void initUI() {
        btnBracketLeft = calc.findViewById(R.id.btn_left_bracket);
        btnBracketLeft.setOnClickListener(this);
        btnBracketRight = calc.findViewById(R.id.btn_right_bracket);
        btnBracketRight.setOnClickListener(this);
        btnClear = calc.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnbackSpace = calc.findViewById(R.id.btn_del_num);
        btnbackSpace.setOnClickListener(this);
        btnZero = calc.findViewById(R.id.btn_zero);
        btnZero.setOnClickListener(this);
        btnOne = calc.findViewById(R.id.btn_one);
        btnOne.setOnClickListener(this);
        btnTwo = calc.findViewById(R.id.btn_two);
        btnTwo.setOnClickListener(this);
        btnThree = calc.findViewById(R.id.btn_three);
        btnThree.setOnClickListener(this);
        btnFour = calc.findViewById(R.id.btn_four);
        btnFour.setOnClickListener(this);
        btnFive = calc.findViewById(R.id.btn_five);
        btnFive.setOnClickListener(this);
        btnSix = calc.findViewById(R.id.btn_six);
        btnSix.setOnClickListener(this);
        btnSeven = calc.findViewById(R.id.btn_seven);
        btnSeven.setOnClickListener(this);
        btnEight = calc.findViewById(R.id.btn_eight);
        btnEight.setOnClickListener(this);
        btnNine = calc.findViewById(R.id.btn_nine);
        btnNine.setOnClickListener(this);
        btnMultyplication = calc.findViewById(R.id.btn_multiplication);
        btnMultyplication.setOnClickListener(this);
        btnDivison = calc.findViewById(R.id.btn_division);
        btnDivison.setOnClickListener(this);
        btnPlus = calc.findViewById(R.id.btn_plus);
        btnPlus.setOnClickListener(this);
        btnMinus = calc.findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(this);
        btnSeparator = calc.findViewById(R.id.btn_separator);
        btnSeparator.setOnClickListener(this);
        btnEqually = calc.findViewById(R.id.btn_equally);
        btnEqually.setOnClickListener(this);
        tvExpress = calc.findViewById(R.id.tv_express);
        tvResult = calc.findViewById(R.id.tv_result);

        cancelBtn = calc.findViewById(R.id.btn_cancel_calc);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String result;
        switch (v.getId()) {
            case R.id.btn_cancel_calc:
                getDialog().dismiss();
                break;
            case R.id.btn_clear:
                calcPresenter.setExpress("");
                tvExpress.setText(calcPresenter.getExpress());
                break;
            case R.id.btn_del_num:
                calcPresenter.deleteSimbol();
                if (calcPresenter.getExpress().length() > 20){
                    tvExpress.setTextSize(16);
                } else {
                    tvExpress.setTextSize(24);
                }
                tvExpress.setText(calcPresenter.getExpress());
                break;
            case R.id.btn_equally:
                result = calcPresenter.getResult();
                if (result.length() > 16){
                    tvResult.setTextSize(24);
                }
                tvResult.setText(result);
                if (isNumeric(result)){
                    calcPresenter.setExpress(result);
                }
                break;
            default:
                Button b = calc.findViewById(v.getId());
                calcPresenter.buildExpress(b.getText().toString());
                calcPresenter.correctExpress();
                result = calcPresenter.getExpress();
                if (result.length() > 20){
                    tvExpress.setTextSize(16);
                } else {
                    tvExpress.setTextSize(24);
                }
                calcPresenter.limitLenForExpress();
                tvExpress.setText(calcPresenter.getExpress());
                break;
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


}
