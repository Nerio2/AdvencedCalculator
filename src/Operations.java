import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Operations {


    private List<BigDecimal> values = new ArrayList<>();
    private List<String> actions = new ArrayList<>();

    public void addValue(String value) {
        switch (value) {
            case "π": {
                values.add(new BigDecimal(Math.PI));
            }
            break;
            case "e": {
                values.add(new BigDecimal(Math.E));
            }
            break;
            default: {
                values.add(new BigDecimal(value));
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
            BigDecimal result = new BigDecimal(0);
            while (actions.size() >= 1) {
                String action = getAction();
                result = values.get(actions.indexOf(action));
                BigDecimal val1 = values.get(actions.indexOf(action));
                BigDecimal val2 = new BigDecimal(0);
                if (!action.equals("√")) {
                    try {
                        val2 = values.get(actions.indexOf(action) + 1);
                    } catch (IndexOutOfBoundsException x) {
                        return String.valueOf(result);
                    }
                }
                else{
                    if(val1.doubleValue()<0)
                        return "ERROR: You can't calculate a root from a negative number";
                }
                if(action.equals("/") && val2.equals(new BigDecimal(0))){
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
                        result = sqrt(val1, new BigDecimal(2));
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

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);

    }

    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public static BigDecimal mult(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public static BigDecimal div(BigDecimal a, BigDecimal b) {
        return a.divide(b);
    }

    public static BigDecimal pow(BigDecimal a, BigDecimal b) {
        return new BigDecimal(Math.pow(a.doubleValue(),b.doubleValue()));
    }

    public static BigDecimal sqrt(BigDecimal a, BigDecimal b) {
        if(b.equals(new BigDecimal(0)))return new BigDecimal(1);
        return pow(a, div(new BigDecimal(1) , b));
    }
}
