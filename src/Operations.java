import java.util.ArrayList;
import java.util.List;

public class Operations {
    private List<Double> values = new ArrayList<Double>();
    private List<String> actions = new ArrayList<String>();

    public void addValue(double value) { values.add(value); }

    public double getValue(int index) {
        return values.get(index);
    }

    public void setAction(String action) {
        actions.add(action);
    }

    private String getAction() {
        String action = "";
        if (actions.size() > 0) {
            int priority = 0;
            for (String a : actions) {
                if (a.equals("sqrt") || a.equals("x^n") && priority <= 2) {
                    priority+=2;
                    action = a;
                } else if (a.equals("*") || a.equals("/") && priority <= 1) {
                    priority++;
                    action = a;
                } else if (priority == 0) {
                    priority++;
                    action = a;
                }
            }
        }
        return action;
    }

    public double calculate(double exception) {
        if (values.size() >= 2) {
            double result = 0;
            while (values.size() >= 2) {
                String action = getAction();
                result = values.get(actions.indexOf(action));
                double val1 = values.get(actions.indexOf(action));
                double val2 = values.get(actions.indexOf(action) + 1);
                switch (action) {
                    case "*": {
                        result = mult(val1, val2);
                    }
                    break;
                    case "/": {
                        result = div(val1, val2);
                    }
                    break;
                    case "+": {
                        result = add(val1, val2);
                    }
                    break;
                    case "-": {
                        result = sub(val1, val2);
                    }
                    break;
                    case "sqrt": {
                        result = sqrt(val1, val2);
                    }
                    break;
                    case "x^n": {
                        result = pow(val1, val2);
                    }
                    break;
                }
                values.remove(actions.indexOf(action) + 1);
                values.remove(actions.indexOf(action));
                values.add(actions.indexOf(action), result);
                actions.remove(actions.indexOf(action));
            }
            return result;
        }
        return exception;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static double sub(double a, double b) {
        return a - b;
    }

    public static double mult(double a, double b) {
        return a * b;
    }

    public static double div(double a, double b) {
        if (b == 0) return a;
        else return a / b;
    }

    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    public static double sqrt(double a, double b) {
        if (b == 0) return a;
        else return pow(a, 1 / b);
    }
}
