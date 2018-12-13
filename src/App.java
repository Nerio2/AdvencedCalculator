/*
 * 1. State: Testing
 * 2. efficiency: should work.
 * 3. What's next?:
 * - File input support;
 * - Extends for functions and calculating value for "x";
 * - Calculations in other Thread?;
 */

/*
 * Testing results: all problems solved now
 */

//TODO: where is checking of undefined values, like letters and if not on calculation layer move it there
//TODO: split layoutLayer to functions and normal calculator
//TODO: menu where you can chose type of calc (normal, functions, binary?)
//TODO: function chose: linear, quadratic and polynomial
//TODO: linear function: 0=AX+BY+C or y=ax+b
//TODO: quadratic function: y=ax^2+bx+c or y=a(x-p)^2+q
//TODO: polynomial: at first derive the general function equation like: y=ax^4+bx^3+cx+d
//TODO: polynomial: hash map of <Integer degree , Big Decimal coefficients>

import java.awt.*;

class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LayoutLayer();
            }
        });
    }
}
