package org.ironriders.constants;

/**
 * Robot specific constants.
 * <p>
 * M = Meters
 * CM = Centimeters
 * MM = Millimeters
 * <p>
 * Y = Yards
 * FT = Feet
 * IN = Inches
 */
public class Robot {
    public static final double nominalVoltage = 11.0;

    /**
     * Robot dimensions in IN (including bumpers).
     */
    public static class Dimensions {
        public static final int LENGTH = 31;
        public static final int WIDTH = 31;
        public static final int HEIGHT = -1;
    }
}
