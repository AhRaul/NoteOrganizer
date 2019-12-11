package ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.model;

import java.util.Stack;

public class CalculatorModel {

    private String result;

    public CalculatorModel() {
        result = "0";
    }

    public String getResult(){
        return result;
    }

    public String calcResult(String express){
        String[] strArray;
        Stack<String> stack = new Stack<>();
        int cntOpeBrackets = 0;
        while (express.length() > 0) {
            char currentChar = express.charAt(0);
            if ((currentChar == '-' || currentChar == '+') && (stack.peek().equals(null) || stack.peek().equals("("))){
                    String sign = express.substring(0,1);
                    express = express.substring(1);
                    strArray = nextDouble(express);
                    express = strArray[0];
                    stack.push(sign + strArray[1]);
            }
            else if (isNumeric(currentChar)) {
                strArray = nextDouble(express);
                express = strArray[0];
                stack.push(strArray[1]);
            } else {
                switch (currentChar) {
                    case 'x':
                        stack.push("x");
                        break;
                    case '/':
                        stack.push("/");
                        break;
                    case '+':
                        stack.push("+");
                        break;
                    case '-':
                        stack.push("-");
                        break;

                    case '(':
                        stack.push("(");
                        cntOpeBrackets++;
                        break;
                    case ')':
                        if (cntOpeBrackets > 0) {
                            Stack simpleExpress = new Stack();
                            while (!stack.peek().equals("(")) {
                                simpleExpress.push(stack.pop());
                            }
                            stack.pop();
                            cntOpeBrackets--;
                            String tempItem = calculateExpress(simpleExpress);
                            if (tempItem.equals("inf")) return "inf";
                            stack.push(tempItem);
                        } else {
                            result = "error no open bracket";
                            return result;
                        }
                        break;
                    default:
                        break;
                }
                express = express.substring(1);
            }
        }
        if (cntOpeBrackets != 0) {
            result = "error brackets";
            return result;
        }
        Stack mirrorExpress = new Stack();
        while (!stack.empty()) {
            mirrorExpress.push(stack.pop());
        }
        result = calculateExpress(mirrorExpress);
        return result;
    }

    private String calculateExpress(Stack<String> express){

        Stack<String> stack = new Stack<>();
        if (express.peek().equals("x") || express.peek().equals("/") || express.peek().equals("+") || express.peek().equals("-")){
            express.pop();
        }
        while (!express.empty()) {
            String item = express.pop();
            if (isNumeric(item)) {
                stack.push(item);
            } else {
                double arg1, arg2;
                switch (item.charAt(0)){
                    case 'x':
                        arg1 = Double.parseDouble(stack.pop());
                        arg2 = Double.parseDouble(express.pop());
                        stack.push(Double.toString(arg1*arg2));
                        break;
                    case '/':
                        arg1 = Double.parseDouble(stack.pop());
                        arg2 = Double.parseDouble(express.pop());
                        if (arg2 != 0.0) stack.push(Double.toString(arg1/arg2)); else return "inf";
                        break;
                    case '+':
                        stack.push("+");
                        break;
                    case '-':
                        stack.push("-");
                        break;
                    default:
                        break;
                }
            }
        }
        return calculateSimpleExpress(stack);
    }

    private String calculateSimpleExpress(Stack<String> express){
        double arg1;
        double arg2;
        String result = "0";
        String operator;
        if (!express.empty()){
            arg1 = Double.parseDouble(express.pop());
            if (express.empty()) return Double.toString(arg1);
            operator = express.pop();
            arg2 = Double.parseDouble(express.pop());
            switch (operator.charAt(0)){
                case '+':
                    express.push(Double.toString(arg1 + arg2));
                    result = calculateSimpleExpress(express);
                    break;
                case '-':
                    express.push(Double.toString(arg2 - arg1));
                    result = calculateSimpleExpress(express);
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private boolean isNumeric(char c){
        if (c >= '0' && c <= '9' || c == '.'){
            return true;
        }
        return false;
    }

    private boolean isNumeric(String s) throws NumberFormatException{
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(char c){
        if (c == 'x' || c == '/' || c == '+' || c == '-'){
            return true;
        }
        return false;
    }

    private String[] nextDouble(String str) {
        String[] result = new String[2];
        int idx = 0;
        boolean isFraction = false;
        StringBuilder stringBuilder = new StringBuilder();

        char currentChar;
        while (idx < str.length()){
            currentChar = str.charAt(idx);
            if (!(currentChar >=  '0' && currentChar <= '9' || (currentChar = str.charAt(idx)) == '.')) break;
            if (currentChar == '.'){
               if (!isFraction) {
                   if (idx == 0) stringBuilder.append("0."); else stringBuilder.append(currentChar);
                   isFraction = true;
               }
            } else {
                stringBuilder.append(currentChar);
            }
            idx++;
        }
        result[0] = str.substring(idx);
        result[1] = stringBuilder.toString();
        return result;
    }



//    private String[] nextOperator(String str){
//        String[] result = new String[2];
//        result[0] = str.substring(0,1);
//        result[1] = str.substring(1);
//        return result;
//    }

}
