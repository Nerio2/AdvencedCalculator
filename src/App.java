import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private String[] text = {"<=", "CE", "C", "+/-", "(", "7", "8", "9", "/", ")", "4", "5", "6", "*", "sqrt", "1", "2", "3", "-", "x^n", "x^2", "0", ",", "+", "="};
    // working: <=, CE, C, +/-, numbers, /, *, x^2, +, = , sqrt, x^n, (, )
    // TODO: , doesnt work
    // DONE: <= for nukbers less than 0 (with .)
    // DONE: = without closed brackets
    // TODO: better look for user like (2+3-6)

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

        List<Button> buttons = new ArrayList<>();      //button list
        for (String txt : text) {
            buttons.add(new Button(txt, input));    //creating a buttons and put into list
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
    private static List<Operations> operacje = new ArrayList<>();
    private static int operationDegree = 0;

    private Operations getOperations() {
        if (operacje.size() < operationDegree + 1) operacje.add(operationDegree, new Operations());
        return operacje.get(operationDegree);
    }
    private Operations getOperations(int degree) {
        return operacje.get(degree);
    }

    private void removeOperation(int degree) {
        operacje.remove(degree);
    }
    private void resetOperations(){
        operacje = new ArrayList<>();
        operationDegree=0;
    }

    public Button(String text, Input input) {
        super(text);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int action = -1;         //button
                double val = -1;     //current number value in input
                String value = "";    //current String value in input
                String cvalue = value;  //to check if something has changed
                try {
                    action = Integer.parseInt(getText());
                } catch (NumberFormatException x) {
                    System.out.println(x);
                } finally {
                    value = input.getText();
                    cvalue = value;
                    try {
                        val = Double.parseDouble(input.getText());
                    } catch (NumberFormatException xx) {
                        System.out.println(xx);
                    }
                }
                if (action == -1) {
                    switch (getText()) {
                        case "<=": {
                            if (value.length() > 1 && value.indexOf('-') == -1)
                                value = value.substring(0, value.length() - 1);
                            else if (value.length() > 2 && value.indexOf('-') != -1)
                                value = value.substring(0, value.length() - 1);
                            else value = "0";
                        }
                        break;
                        case "CE": {
                            value = "0";
                            resetOperations();
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
                            while(operationDegree>=0) {
                                getOperations().addValue(val);
                                operationDegree--;
                                val=getOperations(operationDegree+1).calculate(val);
                                removeOperation(operationDegree+1);
                            }
                            resetOperations();
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
                        case "(":{
                            operationDegree++;
                        }break;
                        case ")":{
                            getOperations().addValue(val);
                            operationDegree--;
                            val=getOperations(operationDegree+1).calculate(val);
                            removeOperation(operationDegree+1);
                        }break;
                    }
                    if (!cvalue.equals(value)) {
                        val = Double.parseDouble(value);
                    }
                    input.setText(val);

                } else {
                    try {
                        val *= 10;
                        val += val >= 0 ? action : -action;
                        input.setText(val);
                    } catch (NumberFormatException x) {
                        input.setText("ERROR");
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
