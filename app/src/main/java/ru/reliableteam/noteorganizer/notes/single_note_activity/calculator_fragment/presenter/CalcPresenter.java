package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter;

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

        if (express.length() == LIMIT_LEN){
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

    public StringBuilder buildExpress(String s){
        return express.append(s);
    }

    public StringBuilder deleteSymbol(){
        if (express.length() == 0) return express;
        return express.deleteCharAt(express.length() - 1);
    }

    public String getExpress(){
        return express.toString();
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
