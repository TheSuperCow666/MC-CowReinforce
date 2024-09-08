package cow.cowReinforce;

import java.util.ArrayList;
import java.util.LinkedList;

public final class CalculationUtils {
    public static double getResult(String formula) {
        double returnValue = 0.0D;
        try {
            returnValue = doAnalysis(formula);
        } catch (NumberFormatException nfe) {
            return 0.0D;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isRightFormat)
            return 0.0D;
        return returnValue;
    }

    private static double doAnalysis(String formula) {
        double returnValue = 0.0D;
        LinkedList<Integer> stack = new LinkedList<>();
        isRightFormat = true;
        while (isRightFormat && (formula.indexOf('(') >= 0 || formula.indexOf(')') >= 0)) {
            int curPos = 0;
            byte b;
            int i;
            char[] arrayOfChar;
            for (i = (arrayOfChar = formula.toCharArray()).length, b = 0; b < i; ) {
                char s = arrayOfChar[b];
                if (s == '(') {
                    stack.add(Integer.valueOf(curPos));
                } else if (s == ')') {
                    if (stack.size() > 0) {
                        String beforePart = formula.substring(0, ((Integer)stack.getLast()).intValue());
                        String afterPart = formula.substring(curPos + 1);
                        String calculator = formula.substring(((Integer)stack.getLast()).intValue() + 1, curPos);
                        formula = String.valueOf(beforePart) + doCalculation(calculator) + afterPart;
                        stack.clear();
                        break;
                    }
                    isRightFormat = false;
                }
                curPos++;
                b++;
            }
            if (stack.size() > 0)
                break;
        }
        if (isRightFormat)
            returnValue = doCalculation(formula);
        return returnValue;
    }

    private static double doCalculation(String formula) {
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        int curPos = 0;
        int prePos = 0;
        int minus = 0;
        byte b;
        int i;
        char[] arrayOfChar;
        for (i = (arrayOfChar = formula.toCharArray()).length, b = 0; b < i; ) {
            char s = arrayOfChar[b];
            boolean is4 = ((s == '+' || s == '-' || s == '*' || s == '/') && minus != 0 && minus != 2);
            if (is4) {
                values.add(Double.valueOf(Double.parseDouble(formula.substring(prePos, curPos).trim())));
                operators.add(s + "");
                prePos = curPos + 1;
                minus++;
            } else {
                minus = 1;
            }
            curPos++;
            b++;
        }
        values.add(Double.valueOf(Double.parseDouble(formula.substring(prePos).trim())));
        for (curPos = 0; curPos <= operators.size() - 1; curPos++) {
            char op = ((String)operators.get(curPos)).charAt(0);
            switch (op) {
                case '*':
                    values.add(curPos, Double.valueOf(((Double)values.get(curPos)).doubleValue() * ((Double)values.get(curPos + 1)).doubleValue()));
                    values.remove(curPos + 1);
                    values.remove(curPos + 1);
                    operators.remove(curPos);
                    curPos = -1;
                    break;
                case '/':
                    values.add(curPos, Double.valueOf(((Double)values.get(curPos)).doubleValue() / ((Double)values.get(curPos + 1)).doubleValue()));
                    values.remove(curPos + 1);
                    values.remove(curPos + 1);
                    operators.remove(curPos);
                    curPos = -1;
                    break;
            }
        }
        for (curPos = 0; curPos <= operators.size() - 1; curPos++) {
            char op = ((String)operators.get(curPos)).charAt(0);
            switch (op) {
                case '+':
                    values.add(curPos, Double.valueOf(((Double)values.get(curPos)).doubleValue() + ((Double)values.get(curPos + 1)).doubleValue()));
                    values.remove(curPos + 1);
                    values.remove(curPos + 1);
                    operators.remove(curPos);
                    curPos = -1;
                    break;
                case '-':
                    values.add(curPos, Double.valueOf(((Double)values.get(curPos)).doubleValue() - ((Double)values.get(curPos + 1)).doubleValue()));
                    values.remove(curPos + 1);
                    values.remove(curPos + 1);
                    operators.remove(curPos);
                    curPos = -1;
                    break;
            }
        }
        return ((Double)values.get(0)).doubleValue();
    }

    private static boolean isRightFormat = true;
}
