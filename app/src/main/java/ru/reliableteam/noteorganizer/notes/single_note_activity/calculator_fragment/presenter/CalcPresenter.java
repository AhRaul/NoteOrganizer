package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.model.CalculatorModel;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;

public class CalcPresenter {

    private CalculatorModel calculator;
    private StringBuilder express;
    private final int LIMIT_LEN  = 25; //ограничение числа вводимых символов;
    private Map<String, String> mValues = new HashMap<>();
    private CalculatorFragment view;

    public CalcPresenter(CalculatorFragment calculatorFragment) {
        express = new StringBuilder();
        calculator = new CalculatorModel();
        this.view = calculatorFragment;
    }

    public String getResult(){
        char currentChar;
        String exp = express.toString();
        if (!exp.equals("")) {
            currentChar = exp.charAt(exp.length() - 1);
            if (currentChar == '-' || currentChar == '+' || currentChar == 'x' || currentChar == '/') {
                setExpress(exp.substring(0, exp.length() - 1));
            }
        }
        calculator.calcResult(express.toString());
        return calculator.getResult();
    }

    public void correctExpress(){
        setExpress(getCorrectExpress(express.toString()));
    }

    public void limitLenForExpress(){

        if (express.length() == LIMIT_LEN){
            setExpress(express.substring(0, LIMIT_LEN - 1));
        }
    }

    //добавить возможность добавлять M
    private String getCorrectExpress(String inExpress){
        int leng = inExpress.length();
        if (leng == 0) return "";
        if (!isCorrectNumeric(inExpress)) return inExpress.substring(0,leng-1);
        if (leng == 1) {
            char currentChar = inExpress.charAt(0);
            if (currentChar == 'x' || currentChar == '/' || currentChar == ')'){
                return "";
            }
            if (currentChar == '.'){
                return "0.";
            }
            if (currentChar == '+' || currentChar == '-'){
                return "0" + currentChar;
            }
        }
        if (leng >= 2) {
            int idxEnd = leng - 1;
            int idxBegin = leng - 2;
            char charBegin = inExpress.charAt(idxBegin);
            char charEnd   = inExpress.charAt(idxEnd);
            if (charBegin >= '0' && charBegin <= '9' && charEnd == '('){
                return inExpress.substring(0,leng-1);
            }
            if (charBegin == ')'){
                if (charEnd >= '0' && charEnd <= '9' || charEnd == '(' || charEnd == '.' ){
                    return inExpress.substring(0,leng-1);
                }

            }
            if (charBegin == '('){
                if (charEnd == 'x' || charEnd == '/' || charEnd == ')'|| charEnd == '.') {
                    return inExpress.substring(0, leng - 1);
                }
                if (charEnd == '+' || charEnd == '-') {
                    inExpress = inExpress.substring(0, leng - 1);
                    inExpress = inExpress + "0" + charEnd;
                    return inExpress;

                }
            }
            if (charBegin == 'x' || charBegin == '/' || charBegin == '+' || charBegin == '-'){
                if (charEnd == '+' ||charEnd == '-' ||charEnd == 'x' || charEnd == '/' || charEnd == ')') {
                    inExpress = inExpress.substring(0,leng-2);
                    inExpress = inExpress + charEnd;
                }
                if (charEnd == '.'){
                    inExpress = inExpress.substring(0,leng-2);
                    inExpress = inExpress + "0.";
                }
            }
        }
        return inExpress;
    }

    private boolean isCorrectNumeric(String express){
        char currentChar = express.charAt(express.length() - 1);
        if (currentChar == '.') {
            express = express.substring(0,express.length()-1);
            while (!express.isEmpty() && currentChar >= '0' && currentChar <= '9' || currentChar == '.') {
                currentChar = express.charAt(express.length()-1);
                if (currentChar == '.'){
                    return false;
                }
                express = express.substring(0,express.length()-1);
            }
        }
        return true;
    }

    public StringBuilder buildExpress(String s){
        return express.append(s);
    }

    public StringBuilder deleteSymbol(){
        if (express.length() == 0) return express;
        return express.deleteCharAt(express.length()-1);
    }

    public String getExpress(){
        return express.toString();
    }

    public void setExpress(String s){
        express.setLength(0);
        express.append(s);
    }

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
