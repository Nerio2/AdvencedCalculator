import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CalculationLayer {
    private List<Operations> operacje = new ArrayList<>();
    private int operationDegree = 0;

    private static List<String> exceptions = new ArrayList<>(Arrays.asList("π", "e", "∞"));

    private Operations getOperations() {
        for ( int i = operacje.size() ; i < operationDegree + 1 ; i++ )
            operacje.add(i, new Operations());
        return getOperations(operationDegree);
    }

    private Operations getOperations(int degree) {
        return operacje.get(degree);
    }

    private void removeOperation(int degree) {
        operacje.remove(degree);
    }

    private void resetOperations() {
        operacje = new ArrayList<>();
        operationDegree = 0;
    }

    String calc(String inputVal) {      //main method to calculate
        BufferedReader reader;
        try {
            reader = new BufferedReader(new StringReader(inputVal));
            String readData;
            String currentVal = "";
            int read;
            while ( (read = reader.read()) != -1 ) {
                StringBuilder sb = new StringBuilder(currentVal);
                readData = String.valueOf((char) read);
                switch ( readData ) {
                    case ",":
                        readData = ".";
                    case ".": {
                        if (currentVal.equals(")")) {
                            currentVal = "";
                            getOperations().addAction("*");
                            break;
                        }
                        if (currentVal.contains("."))
                            break;
                    }
                    case "∞":
                    case "e":
                    case "π":
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9": {
                        if (currentVal.equals(")")) {
                            currentVal = "";
                            getOperations().addAction("*");
                        }
                        if ((!currentVal.equals("") && !currentVal.equals("-")) && (exceptions.contains(readData) || exceptions.contains(currentVal) || exceptions.contains("-" + currentVal))) {
                            getOperations().addValue(currentVal);
                            currentVal = "";
                            getOperations().addAction("*");
                            sb = new StringBuilder(currentVal);
                        }

                        sb.append(readData);
                        currentVal = sb.toString();
                    }
                    break;
                    case "-":
                        if (currentVal.equals("")) {
                            currentVal = "-";
                        }
                    case "+":
                    case "*":
                    case "/":
                    case "^": {
                        if (!currentVal.equals("") && !currentVal.equals("-")) {
                            if (!currentVal.equals(")"))
                                getOperations().addValue(currentVal);
                            currentVal = "";
                            getOperations().addAction(readData);
                        }
                    }
                    break;
                    case "√": {
                        if (!currentVal.equals("")) {
                            getOperations().addValue(currentVal);
                            currentVal = "";
                            getOperations().addAction("*");
                        }
                        getOperations().addAction("√");
                    }
                    break;
                    case "(": {
                        if (!currentVal.equals("")) {
                            getOperations().addValue(currentVal);
                            currentVal = "";
                            getOperations().addAction("*");
                        }
                        operationDegree++;
                    }
                    break;
                    case ")": {
                        if (!currentVal.equals("") && !currentVal.contains("ERROR")) {
                            getOperations().addValue(currentVal);
                        }
                        operationDegree--;
                        currentVal = getOperations(operationDegree + 1).calculate();
                        removeOperation(operationDegree + 1);
                        if (!currentVal.equals("") && !currentVal.contains("ERROR")) {
                            getOperations().addValue(currentVal);
                            currentVal = ")";
                        }
                    }
                    break;
                    case " ":
                        break;
                    default:
                        return "ERROR unknown value: \"" + readData + "\"";
                }
                if (currentVal.contains("ERROR"))
                    return currentVal;
            }
            if (!currentVal.equals("") && !currentVal.contains("ERROR") && !currentVal.equals(")")) {
                getOperations().addValue(currentVal);
                currentVal = "";
            }
            while ( operationDegree >= 0 ) {
                if (!currentVal.equals(""))
                    getOperations().addValue(currentVal);
                operationDegree--;
                currentVal = getOperations(operationDegree + 1).calculate();
                if (currentVal.contains("ERROR")) break;
                removeOperation(operationDegree + 1);
            }
            resetOperations();
            while ( currentVal.contains(".") && currentVal.length() > 1 && (currentVal.lastIndexOf('0') == currentVal.length() - 1 || currentVal.lastIndexOf('.') == currentVal.length() - 1) ) {
                currentVal = currentVal.substring(0, currentVal.length() - 1);
            }
            return currentVal;
        } catch ( IOException x ) {
            System.out.println("ERROR");
        }
        return "ERROR";
    }
}
