package org.ironriders.lib;

public class Utils {
    public static double absoluteRotation(double input) {
        return (input % 360 + 360) % 360;
    }

    public static boolean isWithinTolerance(double input, double target, double tolerance) {
        return Math.abs(input - target) <= tolerance;
    }

    public static double deadband(double input, double deadband) {
        return isWithinTolerance(input, 0, deadband) ? 0 : input;
    }

    /**
     * Applies a control curve to the input value based on the specified exponent and deadband.
     * The control curve modifies the input such that values within the deadband are set to 0,
     * and the remaining values are transformed using an exponent function.
     *
     * @param input The input value to be modified by the control curve.
     * @param exponent The exponent to which the normalized input (after deadband removal) is raised.
     * @param deadband The range around 0 within which the output is set to 0.
     * @return The modified output value after applying the control curve.
     */
    public static double controlCurve(double input, double exponent, double deadband) {
        input = deadband(input, deadband);
        if (input == 0) {
            return 0;
        }

        return ((input > 0) ? 1 : -1) * Math.pow((Math.abs(input) - deadband) / (1 - deadband), exponent);
    }
}
