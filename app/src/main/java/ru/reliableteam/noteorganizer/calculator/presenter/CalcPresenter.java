package ru.reliableteam.noteorganizer.calculator.presenter;

import ru.reliableteam.noteorganizer.calculator.model.CalculatorModel;

public class CalcPresenter {

    CalculatorModel calculator;
    StringBuilder express;

    public CalcPresenter() {
        express = new StringBuilder();
        calculator = new CalculatorModel();
    }

    public String getResult(){
        calculator.calcResult(express.toString());
        return calculator.getResult();
    }

    public void correctExpress(){
        setExpress(getCorrectExpress(express.toString()));
    }

    private String getCorrectExpress(String inExpress){
        int leng = inExpress.length();
        if (leng == 0) return "";
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

    public StringBuilder buildExpress(String s){
        return express.append(s);
    }

    public StringBuilder deleteSimbol(){
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



}
