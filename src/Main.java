
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static void main(String[] args) {

        EventQueue.invokeLater(App::new);
        /*
        //Testing~
        double in = Double.POSITIVE_INFINITY;
        double nin = Double.NEGATIVE_INFINITY;
        System.out.println(Math.pow(in, nin));  //=0
        System.out.println(in / in);            //=NaN
        System.out.println(in * nin);           //=-Infinity
        System.out.println(in - nin);           //=Infinity
        System.out.println(nin - in);           //=-Infinity
        System.out.println(in + nin);           //=NaN
        System.out.println(nin + in);           //=NaN
        */
    }
}