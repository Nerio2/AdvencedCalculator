import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class Operations {
    private static final String POSITIVE_INFINITY = "∞";
    private static final String NEGATIVE_INFINITY = "-∞";

    private static int precision = 20;
    private static RoundingMode roundingMode = RoundingMode.HALF_DOWN;
    private static MathContext rounding;

    private List<String> valuesChceck = new ArrayList<>();      //to capture exceptions
    private List<BigDecimal> values = new ArrayList<>();        //List with values
    private List<String> actions = new ArrayList<>();           //List with actions

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
        valuesChceck.add(value);
        switch ( value ) {
            case "∞": {
                values.add(new BigDecimal(1));
            }
            break;
            case "π": {
                values.add(new BigDecimal(Math.PI, rounding));
            }
            break;
            case "e": {
                values.add(new BigDecimal(Math.E, rounding));
            }
            break;
            case "-∞": {
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
                return valuesChceck.get(0);

            else {
                String emergencyResult = "";
                BigDecimal result = new BigDecimal(0);
                while ( actions.size() >= 1 ) {
                    List<String> exceptions = new ArrayList<>();
                    String action = getAction();
                    result = values.get(actions.indexOf(action));
                    BigDecimal val1 = values.get(actions.indexOf(action));
                    BigDecimal val2 = new BigDecimal(0);
                    if (!action.equals("√")) {
                        try {
                            val2 = values.get(actions.indexOf(action) + 1);
                        } catch ( IndexOutOfBoundsException x ) {
                            return String.valueOf(result);
                        }
                    } else {
                        exceptions.add("√");
                        if (val1.doubleValue() < 0)
                            return "ERROR: You can't calculate a root from a negative number";
                    }
                    if (action.equals("/") && val2.equals(new BigDecimal(0))) {
                        return "ERROR: You can't devide by 0";
                    }
                    try {
                        if (!val1.equals(new BigDecimal(valuesChceck.get(actions.indexOf(action))))) {
                            return "ERROR";
                        } else if (!exceptions.contains("√") && !val2.equals(new BigDecimal(valuesChceck.get(actions.indexOf(action) + 1)))) {
                            return "ERROR";
                        }
                    } catch ( NumberFormatException x ) {
                        if (valuesChceck.get(actions.indexOf(action)).contains("∞")) {
                            exceptions.add("infinityValue");
                            try {
                                if ((action.equals("-") || action.equals("+")) && !valuesChceck.get(actions.indexOf(action) + 1).contains("∞"))
                                    emergencyResult = valuesChceck.get(actions.indexOf(action));
                                else if (action.equals("/") && valuesChceck.get(actions.indexOf(action) + 1).contains("∞"))
                                    return "Invalid value";
                            } catch ( IndexOutOfBoundsException z ) {
                                return valuesChceck.get(actions.indexOf(action));
                            }
                        } else if (valuesChceck.get(actions.indexOf(action) + 1).contains("∞")) {
                            exceptions.add("infinityValue");
                            try {
                                if (action.equals("/"))
                                    emergencyResult = "0";
                                else if ((action.equals("-") || action.equals("+")) && !valuesChceck.get(actions.indexOf(action)).contains("∞"))
                                    emergencyResult = action + valuesChceck.get(actions.indexOf(action) + 1);
                                else if (action.equals("/") && valuesChceck.get(actions.indexOf(action)).contains("∞"))
                                    return "Invalid value";
                            } catch ( IndexOutOfBoundsException z ) {
                                return valuesChceck.get(actions.indexOf(action));
                            }
                        }
                    }
                    try {
                        result = getResult(action, val1, val2);
                    } catch ( NumberFormatException x ) {
                        emergencyResult = getEmergencyResult(x);
                        if (emergencyResult.contains("∞"))
                            exceptions.add("infinityValue");
                    }
                    if (String.valueOf(result).length() > 2 && String.valueOf(result).charAt(0) == '0' && String.valueOf(result).charAt(1) == 'E')
                        result = new BigDecimal(0, rounding);
                    int numberOfValues = 2;     //to delete
                    for ( String exception : exceptions ) {
                        switch ( exception ) {
                            case "infinityValue": {
                                if (emergencyResult.equals("0")) {
                                    result = new BigDecimal(0);
                                    emergencyResult = "";
                                } else if (!emergencyResult.contains("∞")) {
                                    if (result.doubleValue() > 0) emergencyResult = POSITIVE_INFINITY;
                                    else if (result.doubleValue() < 0) emergencyResult = NEGATIVE_INFINITY;
                                    else return "Invalid value";
                                } else {
                                    if (emergencyResult.contains("-")) result = new BigDecimal(-1);
                                    else result = new BigDecimal(1);
                                }
                            }
                            break;
                            case "√": {
                                numberOfValues = 1;
                            }
                            break;
                        }
                    }
                    submit(action, result, numberOfValues, emergencyResult);
                }
                if (!emergencyResult.equals("")) {
                    if (result.doubleValue() < 0) return NEGATIVE_INFINITY;
                    else return POSITIVE_INFINITY;
                } else
                    return String.valueOf(result);
            }
        }
        return "";
    }

    //Actions
    private static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b, rounding);
    }

    private static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b, rounding);
    }

    private static BigDecimal mult(BigDecimal a, BigDecimal b) throws NumberFormatException {
        try {
            return a.multiply(b, rounding);
        } catch ( NumberFormatException x ) {
            if (a.doubleValue() < 0 && b.doubleValue() % 2 != 0) {
                throw new NumberFormatException(NEGATIVE_INFINITY);
            }
            throw new NumberFormatException(POSITIVE_INFINITY);
        }
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

    private static BigDecimal getResult(String action, BigDecimal val1, BigDecimal val2) throws NumberFormatException {
        switch ( action ) {
            case "*":
                return mult(val1, val2);
            case "/":
                return div(val1, val2);
            case "+":
                return add(val1, val2);
            case "-":
                return sub(val1, val2);
            case "√":
                return sqrt(val1, new BigDecimal(2));
            case "^":
                try {
                    return pow(val1, val2);
                } catch ( NumberFormatException x ) {
                    throw new NumberFormatException(getEmergencyResult(x));
                }
        }
        return val1;
    }

    private void submit(String action, BigDecimal result, int numberOfValues, String emergencyResult) {
        int index = actions.indexOf(action);
        try {
            System.out.print("action: " + action + " at " + valuesChceck.get(index) + " and " + valuesChceck.get(index + 1) + " with result: ");
        } catch ( IndexOutOfBoundsException x ) {
            System.out.print("action: " + action + " at " + valuesChceck.get(index) + " with result: ");
        }
        for ( int i = 0 ; i < numberOfValues ; i++ ) {
            values.remove(index);
            valuesChceck.remove(index);
        }
        values.add(index, result);
        if (emergencyResult.equals("")) {
            System.out.println(result);
            valuesChceck.add(index, String.valueOf(result));
        } else {
            System.out.println(emergencyResult);
            valuesChceck.add(index, emergencyResult);
        }
        actions.remove(action);
    }
}
