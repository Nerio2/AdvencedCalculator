import java.awt.EventQueue;
import java.util.Scanner;

public class Test {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
