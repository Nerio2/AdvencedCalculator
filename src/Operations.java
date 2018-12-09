import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class Operations {
    private static final String POSITIVE_INFINITY = "+∞";
    private static final String NEGATIVE_INFINITY = "-∞";
    private static boolean infinityInside = false;

    private static int precision = 20;
    private static RoundingMode roundingMode = RoundingMode.HALF_DOWN;
    private static MathContext rounding;

    private List<BigDecimal> values = new ArrayList<>();
    private List<String> actions = new ArrayList<>();

    //TODO: public Operations(MathContext mathContext)

    private Operations(int precision, RoundingMode roundingMode) {
        Operations.precision = precision;
        Operations.roundingMode = roundingMode;
        try {
            rounding = new MathContext(precision, roundingMode);
        } catch ( IllegalArgumentException x ) {
            rounding = new MathContext(10, RoundingMode.HALF_DOWN);
        }
    }

    private Operations(int precision) {
        new Operations(precision, roundingMode);
    }

    Operations() {
        new Operations(precision);
    }

    void addValue(String value) {
        switch ( value ) {
            case "∞":{
                infinityInside=true;
                values.add(new BigDecimal(1));
            }
            case "π": {
                values.add(new BigDecimal(Math.PI, rounding));
            }
            break;
            case "e": {
                values.add(new BigDecimal(Math.E, rounding));
            }
            break;
            case "-∞":{
                infinityInside=true;
                values.add(new BigDecimal(-1));
            }
            break;
            case "-π": {
                values.add(new BigDecimal(-Math.PI, rounding));
            }
            break;
            case "-e": {
                values.add(new BigDecimal(-Math.E, rounding));
            }
            break;
            default: {
                values.add(new BigDecimal(value, rounding));
            }
            break;
        }

    }

    void addAction(String action) {
        actions.add(action);
    }

    private String getAction() {
        String action = "";
        if (actions.size() > 0) {
            int priority = 0;
            for ( String a : actions ) {
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

    String calculate() {
        if (values.size() >= 1) {
            if (actions.size() < 1)
                return String.valueOf(values.get(0));
            else {
                BigDecimal result = new BigDecimal(0);
                while ( actions.size() >= 1 ) {
                    String action = getAction();
                    result = values.get(actions.indexOf(action));
                    String emergencyResult = "";
                    BigDecimal val1 = values.get(actions.indexOf(action));
                    BigDecimal val2 = new BigDecimal(0);
                    if (!action.equals("√")) {
                        try {
                            val2 = values.get(actions.indexOf(action) + 1);
                        } catch ( IndexOutOfBoundsException x ) {
                            return String.valueOf(result);
                        }
                    } else {
                        if (val1.doubleValue() < 0)
                            return "ERROR: You can't calculate a root from a negative number";
                    }
                    if (action.equals("/") && val2.equals(new BigDecimal(0))) {
                        return "ERROR: You can't devide by 0";
                    }
                    String exception = "";
                    switch ( action ) {
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
                            try {
                                result = pow(val1, val2);
                            } catch ( NumberFormatException x ) {
                                exception = "infinityValue";
                                emergencyResult = getEmergencyResult(x);
                            }
                        }
                        break;
                    }
                    if (String.valueOf(result).length() > 2 && String.valueOf(result).charAt(0) == '0' && String.valueOf(result).charAt(1) == 'E')
                        result = new BigDecimal(0, rounding);

                    switch ( exception ) {
                        case "infinityValue": {
                            System.out.println("action: " + action + " at " + val1 + " and " + val2 + " with result: " + emergencyResult);
                            infinityInside = true;
                            String val = emergencyResult.substring(0, 1);

                            values.remove(actions.indexOf(action) + 1);
                            values.remove(actions.indexOf(action));
                            result = new BigDecimal(0);
                            try{
                                String nextAction = actions.get(actions.indexOf(action) + 1);
                                if (nextAction.equals("+") || nextAction.equals("-"))
                                    actions.remove(nextAction);
                                else {
                                    result = new BigDecimal(val + 1);
                                    values.add(actions.indexOf(action), result);
                                }
                            }catch ( IndexOutOfBoundsException x){
                                System.out.println("next action doesn't exists");
                            }
                            try{
                                String previousAction = actions.get(actions.indexOf(action) - 1);
                                if (previousAction.equals("+") || previousAction.equals("-"))
                                    actions.remove(previousAction);
                                else if(!result.equals(new BigDecimal(val+1))) {
                                    result = new BigDecimal(val + 1);
                                    values.add(actions.indexOf(action), result);
                                }
                            }catch ( IndexOutOfBoundsException x){
                                System.out.println("previous action doesn't exists");
                            }
                            actions.remove(action);
                            for ( BigDecimal a : values )
                                System.out.println(String.valueOf(a));
                            for ( String a : actions )
                                System.out.println(a);

                        }
                        break;
                        case "√": {
                            System.out.println("action: " + action + " at " + val1 + " with result: " + result);
                            values.remove(actions.indexOf(action));
                            values.add(actions.indexOf(action), result);
                            actions.remove(action);
                        }
                        break;
                        default: {
                            System.out.println("action: " + action + " at " + val1 + " and " + val2 + " with result: " + result);
                            values.remove(actions.indexOf(action) + 1);
                            values.remove(actions.indexOf(action));
                            values.add(actions.indexOf(action), result);
                            actions.remove(action);
                        }
                        break;
                    }

                }
                if (infinityInside) {
                    if(result.doubleValue()<0)return NEGATIVE_INFINITY;
                    else return POSITIVE_INFINITY;
                } else
                    return String.valueOf(result);
            }
        }
        return "";
    }

    private static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b, rounding);

    }

    private static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b, rounding);
    }

    private static BigDecimal mult(BigDecimal a, BigDecimal b) {
        return a.multiply(b, rounding);
    }

    private static BigDecimal div(BigDecimal a, BigDecimal b) {
        return a.divide(b, rounding);
    }

    private static BigDecimal pow(BigDecimal a, BigDecimal b) throws NumberFormatException {
        try {
            return new BigDecimal(Math.pow(a.doubleValue(), b.doubleValue())).setScale(precision, roundingMode);
        } catch ( NumberFormatException x ) {
            if (a.doubleValue() < 0 && b.doubleValue() % 2 != 0) {
                throw new NumberFormatException(NEGATIVE_INFINITY);
            }
            throw new NumberFormatException(POSITIVE_INFINITY);
        }
    }

    private static BigDecimal sqrt(BigDecimal a, BigDecimal b) {
        if (b.equals(new BigDecimal(0))) return new BigDecimal(1);
        return pow(a, div(new BigDecimal(1), b));
    }

    private static String getEmergencyResult(NumberFormatException x) {
        return x.toString().substring(33);
    }
}
