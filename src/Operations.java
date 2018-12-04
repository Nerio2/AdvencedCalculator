import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Operations {


    private List<Double> values = new ArrayList<>();
    private List<String> actions = new ArrayList<>();

    public void addValue(String value) {
        switch (value) {
            case "π": {
                values.add(Math.PI);
            }
            break;
            case "e": {
                values.add(Math.E);
            }
            break;
            default: {
                values.add(Double.parseDouble(value));
            }
            break;
        }

    }

    public void addAction(String action) {
        actions.add(action);
    }

    private String getAction() {
        String action = "";
        if (actions.size() > 0) {
            int priority = 0;
            for (String a : actions) {
                if ((a.equals("√") || a.equals("^")) && priority <= 2) {
                    priority = 3;
                    action = a;
                } else if ((a.equals("*") || a.equals("/")) && priority <= 1) {
                    priority = 2;
                    action = a;
                } else if (priority == 0) {
                    priority = 1;
                    action = a;
                }
            }
        }
        return action;
    }

    public String calculate() {
        if (values.size() >= 1) {
            double result = 0;
            while (actions.size() >= 1) {
                String action = getAction();
                result = values.get(actions.indexOf(action));
                double val1 = values.get(actions.indexOf(action));
                double val2 = 0;
                if (!action.equals("√")) {
                    try {
                        val2 = values.get(actions.indexOf(action) + 1);
                    } catch (IndexOutOfBoundsException x) {
                        return String.valueOf(result);
                    }
                }
                else{
                    if(val1<0)
                        return "ERROR: You can't calculate a root from a negative number";
                }
                if(action.equals("/") && val2==0){
                    return "ERROR: You can't devide by 0";
                }
                String exception = "";
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
                    case "√": {
                        result = sqrt(val1, 2);
                        exception = "√";
                    }
                    break;
                    case "^": {
                        result = pow(val1, val2);
                    }
                    break;
                }
                switch (exception) {
                    case "√": {
                        System.out.println("action: " + action + " at " + val1 + " with result: " + result);
                        values.remove(actions.indexOf(action));
                        values.add(actions.indexOf(action), result);
                        actions.remove(actions.indexOf(action));
                    }
                    break;
                    default: {
                        System.out.println("action: " + action + " at " + val1 + " and " + val2 + " with result: " + result);
                        values.remove(actions.indexOf(action) + 1);
                        values.remove(actions.indexOf(action));
                        values.add(actions.indexOf(action), result);
                        actions.remove(actions.indexOf(action));
                    }
                    break;
                }

            }
            return String.valueOf(result);
        }
        return "";
    }

    public static double add(double a, double b) {
        return a+b;

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
