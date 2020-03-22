package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter;

import android.view.View;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;

public class CalcPresenter {

    private StringBuilder express;
    private final int LIMIT_LEN  = 25; //ограничение числа вводимых символов;
    private Map<String, String> mValues = new HashMap<>();
    private CalculatorFragment view;

    public CalcPresenter(CalculatorFragment calculatorFragment) {
        express = new StringBuilder();
        this.view = calculatorFragment;
    }

    public String getResult(){
        Expression exp = new Expression(getExpress());
        correctExpress();
        return exp.calculate() + "";
    }

    public void correctExpress() {
        String expression = express.toString();
        if (checkExpress(expression))
            setExpress(expression);
    }

    public void limitLenForExpress(){
        int len = getExpressLen();
        if (len == LIMIT_LEN){
            setExpress(express.substring(0, LIMIT_LEN - 1));
        }
    }

    private boolean checkExpress(String inExpress){
        if (!isCorrect(inExpress)) {
            view.showError();
            return false;
        } else {
            view.setTvResult("");
            return true;
        }
    }

    private boolean isCorrect(String express) {
        Expression e = new Expression(express);
        return e.checkSyntax();
    }
    public boolean isNumeric(String s) throws NumberFormatException{
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean isActionButton(View v) {
        String id = v.getTag().toString();
        System.out.println(id);
        return isOperator(id);//id == R.id.btn_division || id == R.id.btn_minus || id == R.id.btn_plus ||
                //id == R.id.btn_multiplication || id == R.id.btn_equally;
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*") || s.equals("=");
    }

    public void setExpressionTextSize() {
        int len = getExpressLen();
        view.setTextSize((len > 20) ? 16 : 24);
    }


    public StringBuilder buildExpress(String s, int insertIdx, boolean hasFocus){
        int len = getExpressLen();
        if (!hasFocus || len == 0)
            return express.append(s);
        //else if (hasFocus)
            return express.insert(insertIdx, s.charAt(0));

    }

    public StringBuilder deleteSymbol(int startIdx){
        System.out.println(startIdx);
        int len = getExpressLen();
        if (len == 0) return express;
        if (startIdx != 0)
            return express.deleteCharAt(startIdx - 1);

        return express.deleteCharAt(len - 1);
    }
    public int getPointer(int startIdx) {
        int len = getExpressLen();
        if (len == 0)
            return  0;
        if (startIdx > 0)
            return startIdx - 1;
        else
            return 0;

    }

    public String getExpress(){
        return express.toString();
    }
    private int getExpressLen() {
        return express.length();
    }

    public void setExpress(String s){
        express.setLength(0);
        express.append(s);
    }


    /**
     * SAVED ARGUMENTS REPLACEMENT
     * @param savedArguments - list of saved arguments like ["M1:200", "M2:300"]
     */
    public void setSavedArguments(List<String> savedArguments) {
        for(String s: savedArguments) {
            String[] toMapValue = s.split(":");
            if (toMapValue.length == 2) {
                mValues.put(toMapValue[0].toUpperCase(), toMapValue[1]);
                System.out.println(toMapValue[0].toUpperCase() + " --- " + toMapValue[1]);
            }
        }

    }
    public void checkInputAndReplace() {
        String expression = express.toString();
        if (!expression.contains("M"))
            return;

        Pattern pattern = Pattern.compile("M\\d+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String key = expression.substring(matcher.start(), matcher.end());
            String value = mValues.get(key);
            if (value != null)
                expression = expression.replace(key, value);
        }

        view.setExpression(expression);
        express = new StringBuilder(expression);
    }

}
