
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.jar.JarInputStream;

public class Main extends JFrame {
    private String[] text = {"<-", "CE", "C", "+/-", "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ",", "+", "="};

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }

    public Main() {
        super("Kalkulator");

        JPanel body = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout());
        JPanel main = new JPanel(new GridLayout(5, 4));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(300, 500);

        Input input = new Input();
        input.setSize(300, 100);
        top.add(input);
        List<Button> buttons = new ArrayList<Button>();
        for (int i = 0; i < text.length; i++) {
            buttons.add(new Button(text[i], input));
        }
        for (Button b : buttons) {
            main.add(b);
        }
        body.add(top, BorderLayout.NORTH);
        body.add(main, BorderLayout.CENTER);
        getContentPane().add(body);
    }
}

class Button extends JButton {
    public Button(String text, Input input) {
        super(text);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int action = -1;
                double val = 0;
                try {
                    val = Double.parseDouble(input.getText());
                } catch (NumberFormatException x) {
                    ;
                }
                try {
                    action = Integer.parseInt(getText());
                    val *= 10;
                    val += action;
                } catch (NumberFormatException x) {
                    ;
                }
                if ( (long) val == val && (long) val > Math.pow(2,63))
                    input.setText(String.valueOf((long)val));
                else setText(String.valueOf(val));
            }
        });
    }
}

class Input extends JTextField {
    public Input() {
        super();
        setText("0");
    }
}