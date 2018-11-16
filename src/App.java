import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private String[] text = {"<=", "CE", "C", "+/-", "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ",", "+", "="};
    private  List<Double> values= new ArrayList<Double>() ;
    private static  List<String> actions = new ArrayList<String>();

    public App() {
        super("Kalkulator");

        JPanel body = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout());
        JPanel main = new JPanel(new GridLayout(5, 4));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(300, 500);

        Input input = new Input();                      //creating input and add to top layout
        input.setSize(300, 100);
        top.add(input);

        List<Button> buttons = new ArrayList<Button>();      //button list
        for (int i = 0; i < text.length; i++) {
            buttons.add(new Button(text[i], input, this));    //creating a buttons and put into list
        }
        for (Button b : buttons) {
            main.add(b);                //add to layout
        }
        body.add(top, BorderLayout.NORTH);
        body.add(main, BorderLayout.CENTER);
        getContentPane().add(body);
    }

    public void addValue(double value) {
        values.add(value);
    }

    public double getValue(int index) {
        return values.get(index);
    }

    public static void setAction(String action) {
        actions.add(action);
    }

    private static String getAction() {
        int priority = 0;
        String action = "";
        for (String a : actions) {
            if (a.equals("*") || a.equals("/") && priority <= 1) {
                priority++;
                action = a;
            } else if (priority == 0) {
                priority++;
                action = a;
            }
        }
        if (action.equals("")) return "";
        else return action;
    }

    public double calculate() {                                 //zrobic obsluge bledow
        String action = App.getAction();
        double result = values.get(actions.indexOf(action));
        double val1 = values.get(actions.indexOf(action));
        double val2 = values.get(actions.indexOf(action) + 1);
        switch (action) {
            case "*": {
                result = val1 * val2;
            }
            break;
            case "+": {
                result = val1 + val2;
            }
            break;
        }
        values.remove(actions.indexOf(action) + 1);
        values.remove(actions.indexOf(action));
        values.add(actions.indexOf(action), result);
        actions.remove(actions.indexOf(action));
        return result;
    }
}

class Button extends JButton {
    public Button(String text, Input input, App app) {
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
                            if (value.length() > 1 && value.indexOf('-') == -1)
                                value = value.substring(0, value.length() - 1);
                            else if (value.length() > 2 && value.indexOf('-') != -1)
                                value = value.substring(0, value.length() - 1);
                            else value = "0";
                        }
                        break;
                        case "CE": {
                            value = "0";
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
                            app.addValue(val);
                            App.setAction("+");
                            value = "0";
                        }
                        break;
                        case "*": {
                            app.addValue(val);
                            App.setAction("*");
                            value = "0";
                        }
                        break;
                        case "=": {
                            app.addValue(val);
                            val = app.calculate();
                        }
                        break;
                    }
                    if (cvalue != value) {
                        val = Double.parseDouble(value);
                        input.setText(val);
                    }input.setText(val);

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
