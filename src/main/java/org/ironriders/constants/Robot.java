package org.ironriders.constants;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;

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
    public static final double NOMINAL_VOLTAGE = 11.0;
    public static final Transform3d LIMELIGHT_POSITION = new Transform3d(0, 0, 0, new Rotation3d());

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
