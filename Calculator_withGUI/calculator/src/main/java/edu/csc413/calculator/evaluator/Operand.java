package edu.csc413.calculator.evaluator;

/**
 * Operand class used to represent an operand
 * in a valid mathmetical expression.
 */
public class Operand {
    int value;

    /**
     * construct operand from string token.
     */
    public Operand(String token) {
        this.value = Integer.parseInt(token);
    }

    /**
     * construct operand from integer
     */
    public Operand(int value) {
        this.value = value;
    }

    /**
     * return value of opernad
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Check to see if given token is a valid
     * operand.
     */
    public static boolean check(String token) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
