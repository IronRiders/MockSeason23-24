package org.ironriders.lib;

/**
 * Utility class containing various helper methods for mathematical and control operations.
 */
public class Utils {
    /**
     * Calculates the absolute error between the target and current values, considering rotational values.
     *
     * @param target  The target value.
     * @param current The current value.
     * @return The absolute error between the target and current values.
     */
    public static double rotationalError(double target, double current) {
        double error = Utils.absoluteRotation(target) - current;
        if (error > 180) {
            error -= 360;
        } else if (error < -180) {
            error += 360;
        }
        return error;
    }

    /**
     * Normalizes a rotational input value to the range [0, 360) degrees.
     *
     * @param input The input rotational value.
     * @return The normalized rotational value within the range [0, 360) degrees.
     */
    public static double absoluteRotation(double input) {
        return (input % 360 + 360) % 360;
    }

    /**
     * Checks if the input value is within a specified tolerance of the target value.
     *
     * @param input     The input value.
     * @param target    The target value.
     * @param tolerance The allowable tolerance range.
     * @return {@code true} if the input value is within the specified tolerance of the target value, {@code false}
     * otherwise.
     */
    public static boolean isWithinTolerance(double input, double target, double tolerance) {
        return Math.abs(input - target) <= tolerance;
    }

    /**
     * Applies deadband to the input value. If the input falls within the specified deadband range, it is set to 0.
     *
     * @param input    The input value to be processed.
     * @param deadband The range around 0 within which the output is set to 0.
     * @return The processed output value after applying deadband.
     */
    public static double deadband(double input, double deadband) {
        return isWithinTolerance(input, 0, deadband) ? 0 : input;
    }

    /**
     * Applies a control curve to the input value based on the specified exponent and deadband.
     * The control curve modifies the input such that values within the deadband are set to 0,
     * and the remaining values are transformed using an exponent function.
     *
     * @param input    The input value to be modified by the control curve.
     * @param exponent The exponent to which the normalized input (after deadband removal) is raised.
     * @param deadband The range around 0 within which the output is set to 0.
     * @return The modified output value after applying the control curve.
     */
    public static double controlCurve(double input, double exponent, double deadband) {
        input = deadband(input, deadband);
        if (input == 0) {
            return 0;
        }

        return sign(input) * Math.pow((Math.abs(input) - deadband) / (1 - deadband), exponent);
    }

    /**
     * Returns the sign of the input value.
     *
     * @param input The input value to determine the sign.
     * @return 1 if the input is positive, -1 if negative, 0 if zero.
     */
    public static double sign(double input) {
        return input > 0 ? 1 : -1;
    }
}
