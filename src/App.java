import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/*
 * 1. State: In testing right now.
 * 2. efficiency: should work.
 * 3. What's next?:
 * - File input support;
 * - Extends for functions and calculating value for "x";
 */

/*
 * Testing results:
 * - problems with infinity (no char for that and problems with for ex. infinity*-3)
 */

public class App extends JFrame implements KeyListener {

    private Input input;
    private List<Operations> operacje = new ArrayList<>();
    private int operationDegree = 0;

    public App() {
        super("Calculator");
        String[] text = {"<=", "AC", "π", "e", "(", "7", "8", "9", "/", ")", "4", "5", "6", "*", "√", "1", "2", "3", "-", "x^n", "x^2", "0", ",", "+", "="};
        addKeyListener(this);
        setFocusable(true);
        pack();
        JPanel body = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout());
        JPanel main = new JPanel(new GridLayout(5, 4));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(350, 500);

        input = new Input(this);                      //creating input and add to top layout
        input.setSize(300, 100);
        top.add(input);

        List<Button> buttons = new ArrayList<>();      //button list

        for (String txt : text) {
            buttons.add(new Button(txt, input, this));    //creating a buttons and put into list
        }
        for (Button b : buttons) {
            main.add(b);                //add to layout
        }
        body.add(top, BorderLayout.NORTH);
        body.add(main, BorderLayout.CENTER);
        getContentPane().add(body);
    }

    private Operations getOperations() {
        if (operacje.size() < operationDegree + 1) operacje.add(operationDegree, new Operations());
        return getOperations(operationDegree);
    }

    private Operations getOperations(int degree) {
        return operacje.get(degree);
    }

    private void removeOperation(int degree) {
        operacje.remove(degree);
    }

    public void resetOperations() {
        operacje = new ArrayList<>();
        operationDegree = 0;
    }

    public String calc(String inputVal) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new StringReader(inputVal));
            String readData;
            String currentVal = "";
            int read;
            while ((read = reader.read()) != -1) {
                readData = String.valueOf((char) read);
                switch (readData) {
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
                        if ((readData.equals("e") || readData.equals("π")) && !currentVal.equals("") || (currentVal.equals("e") || currentVal.equals("π"))) {
                            getOperations().addValue(currentVal);
                            currentVal = "";
                            getOperations().addAction("*");
                        }
                        currentVal += readData;
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
                        if (!currentVal.equals("") && !currentVal.contains("ERROR") ) {
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
                if(currentVal.contains("ERROR"))
                    return currentVal;
            }
            if (!currentVal.equals("") && !currentVal.contains("ERROR") && !currentVal.equals(")")) {
                getOperations().addValue(currentVal);
                currentVal = "";
            }
            while (operationDegree >= 0) {
                operationDegree--;
                currentVal = getOperations(operationDegree + 1).calculate();
                if(currentVal.contains("ERROR"))break;
                removeOperation(operationDegree + 1);
            }
            resetOperations();
            return currentVal;
        } catch (IOException x) {
            System.out.println("ERROR");
        }
        return "ERROR";
    }

    public static boolean shiftPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        String value = input.getText();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT: {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                }
            }
            break;
            case KeyEvent.VK_BACK_SPACE: {

                if (value.length() > 1 && value.indexOf('-') == -1)
                    value = value.substring(0, value.length() - 1);
                else if (value.length() > 2 && value.indexOf('-') != -1)
                    value = value.substring(0, value.length() - 1);
                else value = "0";

            }
            break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_EQUALS: {
                if (!shiftPressed) {
                    value = calc(value);
                    input.setText(value);
                    break;
                }
            }
        }
        input.setText(value);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String value = input.getText();
        switch (String.valueOf(e.getKeyChar())) {
            case "e":
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case ",":
            case ".":
            case "+":
            case "-":
            case "*":
            case "/":
            case "^":
            case "(":
            case ")": {
                if (!value.equals("0"))
                    value += String.valueOf(e.getKeyChar());
                else
                    value = String.valueOf(e.getKeyChar());
            }
            break;

        }
        input.setText(value);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }
}

class Button extends JButton {

    public Button(String text, Input input, App app) {
        super(text);
        addKeyListener(app);
        addActionListener(e -> {
            String value = input.getText();
            switch (text) {
                case "<=": {
                    if (value.length() > 1 && value.indexOf('-') == -1)
                        value = value.substring(0, value.length() - 1);
                    else if (value.length() > 2 && value.indexOf('-') != -1)
                        value = value.substring(0, value.length() - 1);
                    else value = "0";
                }
                break;

                case "AC": {
                    value = "0";
                    app.resetOperations();
                }
                break;
                case "=": {
                    value = app.calc(value);
                }
                break;
                case "x^2": {
                    value += "^2";
                }
                break;
                case "x^n": {
                    value += "^";
                }
                break;
                default: {
                    if (value.equals("0"))
                        value = text;
                    else
                        value += text;
                }
                break;
            }
            input.setText(value);
        });
    }
}

class Input extends JTextField implements KeyListener {
    private App app;

    public Input(App app) {
        super();
        this.app = app;
        addKeyListener(this);
        setText("0");
        setEnabled(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            App.shiftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_EQUALS && !App.shiftPressed) {
            setText(app.calc(getText()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            App.shiftPressed = false;
        }
    }
}
