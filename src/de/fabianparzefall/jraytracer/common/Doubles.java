package de.fabianparzefall.jraytracer.common;

/**
 * Compares to doubles.
 *
 * @author Fabian Parzefall
 * @version 15-05-11
 */
public final class Doubles {
    /**
     * The tolerance for the double comparison.
     */
    private static final double EPSILON = 1E-12;

    /**
     * Compares two doubles for equality.
     *
     * @param double1 The first double.
     * @param double2 The second double.
     * @return True, if they are equal.
     */
    public static boolean equals(final double double1, final double double2) {
        return Math.abs(double1 - double2) <= EPSILON;
    }
}
