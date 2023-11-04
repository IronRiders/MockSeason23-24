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
        public static final double LENGTH = 31;
        public static final double WIDTH = 31;
        public static final double HEIGHT = -1;

        public static final double DRIVEBASE_RADIUS = 0.3048;
    }
}
