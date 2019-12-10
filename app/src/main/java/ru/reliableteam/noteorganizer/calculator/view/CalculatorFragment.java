package ru.reliableteam.noteorganizer.calculator.view;

import android.graphics.drawable.Drawable;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.calculator.presenter.CalcPresenter;


public class CalculatorFragment extends DialogFragment implements View.OnClickListener {

    private FragmentManager fm;

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

//    public CalculatorFragment(FragmentManager fm) {
//        this.fm = fm;
//        this.calcPresenter = new CalcPresenter();
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calc = inflater.inflate(R.layout.fragment_calculator, container, false);
//        Drawable back  = getActivity().getWindow().getDecorView().getBackground();
//        calc.setBackground(back);
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
        tvExpress.setOnClickListener(this);
        tvResult = calc.findViewById(R.id.tv_result);
        tvResult.setOnClickListener(this);

        cancelBtn = calc.findViewById(R.id.btn_cancel_calc);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_calc:
                getDialog().dismiss();
//                close();
                break;
            case R.id.btn_clear:
                calcPresenter.setExpress("");
                tvExpress.setText(calcPresenter.getExpress());
                break;
            case R.id.btn_del_num:
                calcPresenter.deleteSimbol();
                tvExpress.setText(calcPresenter.getExpress());
                break;

            case R.id.btn_equally:
                String result = calcPresenter.getResult();
                tvResult.setText(result);
                calcPresenter.setExpress(result);
                break;
            default:
                Button b = calc.findViewById(v.getId());
                calcPresenter.buildExpress(b.getText().toString());
                calcPresenter.correctExpress();
                tvExpress.setText(calcPresenter.getExpress());
                break;
        }
    }

    public void open(){
        fm.beginTransaction()
        .add(R.id.fragment_calc,this)
                .commit();
    }

    private void close(){
        fm.beginTransaction().remove(this).commit();
    }

}
