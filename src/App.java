import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private String[] text = {"<=", "CE", "C", "+/-", "(", "7", "8", "9", "/", ")", "4", "5", "6", "*", "sqrt", "1", "2", "3", "-", "x^n", "x^2", "0", ",", "+", "="};
    // working: <=, CE, C, +/-, numbers, /, *, x^2, +, =, sqrt, x^n
    // not working: (, ), ","
    public App() {
        super("Kalkulator");

        JPanel body = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout());
        JPanel main = new JPanel(new GridLayout(5, 4));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(350, 500);

        Input input = new Input();                      //creating input and add to top layout
        input.setSize(300, 100);
        top.add(input);

        List<Button> buttons = new ArrayList<Button>();      //button list
        for (int i = 0; i < text.length; i++) {
            buttons.add(new Button(text[i], input));    //creating a buttons and put into list
        }
        for (Button b : buttons) {
            main.add(b);                //add to layout
        }
        body.add(top, BorderLayout.NORTH);
        body.add(main, BorderLayout.CENTER);
        getContentPane().add(body);
    }
}

class Button extends JButton {
    private static List<Operations> operacje = new ArrayList<Operations>();
    private static int operationDegree = 0;

    public Operations getOperations() {
        if (operacje.size() < operationDegree + 1) operacje.add(new Operations());
        return operacje.get(operationDegree);
    }

    public void removeOperation() {
        if (operacje.size() < operationDegree + 1) operacje.add(new Operations());
        else operacje.remove(operationDegree);
    }

    public Button(String text, Input input) {
        super(text);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int action = -1;         //button
                double val = -1;     //current number value in input
                String value = "";    //current String value in input
                String cvalue = value;
                try {
                    action = Integer.parseInt(getText());
                } catch (NumberFormatException x) {
                    ;
                } finally {
                    value = input.getText();
                    cvalue = value;
                    try {
                        val = Double.parseDouble(input.getText());
                    } catch (NumberFormatException xx) {
                        ;
                    }
                }
                if (action == -1) {
                    switch (getText()) {
                        case "<=": {
                            if ((long) val == val) {
                                if (value.length() > 1 && value.indexOf('-') == -1)
                                    value = value.substring(0, value.length() - 1);
                                else if (value.length() > 2 && value.indexOf('-') != -1)
                                    value = value.substring(0, value.length() - 1);
                                else value = "0";
                            }
                        }
                        break;
                        case "CE": {
                            value = "0";
                            operacje = new ArrayList<Operations>();
                        }
                        break;
                        case "C": {
                            value = "0";
                        }
                        break;
                        case "+/-": {
                            val = -val;
                        }
                        break;
                        case "+": {
                            getOperations().addValue(val);
                            getOperations().setAction("+");
                            value = "0";
                        }
                        break;
                        case "-": {
                            getOperations().addValue(val);
                            getOperations().setAction("-");
                            value = "0";
                        }
                        break;
                        case "*": {
                            getOperations().addValue(val);
                            getOperations().setAction("*");
                            value = "0";
                        }
                        break;
                        case "/": {
                            getOperations().addValue(val);
                            getOperations().setAction("/");
                            value = "0";
                        }
                        break;
                        case "=": {
                            getOperations().addValue(val);
                            val = getOperations().calculate(val);
                            removeOperation();
                        }
                        break;
                        case "x^2":{
                            val=Operations.pow(val,2);
                        }break;
                        case "sqrt":{
                            getOperations().addValue(val);
                            getOperations().setAction("sqrt");
                            value = "0";
                        }
                        break;
                        case "x^n":{
                            getOperations().addValue(val);
                            getOperations().setAction("x^n");
                            value = "0";
                        }
                        break;
                    }
                    if (cvalue != value) {
                        val = Double.parseDouble(value);
                        input.setText(val);
                    }
                    input.setText(val);

                } else {
                    try {
                        val *= 10;
                        val += val >= 0 ? action : -action;
                        input.setText(val);
                    } catch (NumberFormatException x) {
                        ;
                    }
                }


            }
        });
    }
}

class Input extends JTextField {
    public Input() {
        super();
        setText("0");
    }

    public void setText(double a) {
        if ((long) a == a)
            setText(String.valueOf((long) a));
        else setText(String.valueOf(a));
    }
}
