import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutLayer extends JFrame implements KeyListener {
    private Input input;
    private CalculationLayer calculationLayer = new CalculationLayer();
    static List<String> acceptedChars;

    LayoutLayer(){
        new LayoutLayer("standard");
    }

    private LayoutLayer(String type) {
        super("Calculator");
        addKeyListener(this);
        setFocusable(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        switch(type){
            case "standard":{
                acceptedChars=new ArrayList<>(Arrays.asList("e", "!", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", ".", "+", "-", "*", "/", "^", "(", ")"));
                String[] text = {"<=", "AC", "π", "e", "(", "7", "8", "9", "/", ")", "4", "5", "6", "*", "√", "1", "2", "3", "-", "x^n", "x^2", "0", ",", "+", "=", "∞", "!"};
                JPanel body = new JPanel(new BorderLayout());
                JPanel top = new JPanel(new GridLayout());
                JPanel main = new JPanel(new GridLayout(5, 4));
                setSize(350, 500);
                input = new Input(this);                      //creating input and add to top layout
                input.setSize(300, 100);
                top.add(input);
                List<Button> buttons = new ArrayList<>();      //button list

                for ( String txt : text ) {
                    buttons.add(new Button(txt, input, this));    //creating a buttons and put into list
                }
                for ( Button b : buttons ) {
                    main.add(b);                //add to layout
                }
                body.add(top, BorderLayout.NORTH);
                body.add(main, BorderLayout.CENTER);
                getContentPane().add(body);
            }
            break;
        }
    }

    String calc(String value) {
        return calculationLayer.calc(value);
    }

    static boolean shiftPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        String value = input.getText();
        switch ( e.getKeyCode() ) {
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
        if(acceptedChars.contains(String.valueOf(e.getKeyChar()))) {
            if (!value.equals("0"))
                value += String.valueOf(e.getKeyChar());
            else
                value = String.valueOf(e.getKeyChar());
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

    Button(String text, Input input, LayoutLayer app) {
        super(text);
        addKeyListener(app);
        addActionListener(e -> {
            String value = input.getText();
            switch ( text ) {
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
    private LayoutLayer app;

    Input(LayoutLayer app) {
        super();
        this.app = app;
        addKeyListener(this);
        setText("0");
        setEnabled(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!LayoutLayer.acceptedChars.contains(String.valueOf(e.getKeyChar())))
            e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            LayoutLayer.shiftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_EQUALS && !LayoutLayer.shiftPressed) {
            setText(app.calc(getText()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            LayoutLayer.shiftPressed = false;
        }
    }
}