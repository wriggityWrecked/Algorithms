
/**
 * Assignment 1.1.20
 * @author devin 
 */
public class Assignment1120 {

    /**
     * Compute the factorial of the input i
     *
     * @param i
     * @return i!
     */
    public static double factorial(double i) {
        if (i == 1) {
            return i;
        } else {
            return i * factorial(i - 1.0);
        }
    }

    /**
     * Using the product rule compute the natural log of N!
     *
     * ln(N!) = ln(N) + ln(N-1) + ln(N-2) + ... + ln(1)
     *
     *
     * @param i
     * @return ln(i!)
     */
    public static double naturalLogOfFactorial(double i) {
        if (i == 1) {
            return Math.log(i);
        } else {
            return Math.log(i) + naturalLogOfFactorial(i - 1.0);
        }
    }

    public static void main(String[] args) {
        double manual = Math.log(factorial(Integer.parseInt(args[0])));
        double function = naturalLogOfFactorial(Integer.parseInt(args[0]));
        assert manual == function; //use with -ea flags
    }
}
