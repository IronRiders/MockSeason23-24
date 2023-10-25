package org.ironriders.constants;

public class Drive {
    public static final int NEO_CURRENT_LIMIT = 40;
    public static final double DISTANCE_KP = 0.1;
    // Direction is the rotation of the wheels on each module. Rotation is the rotation of the robot.
    public static final double DIRECTION_KP = 0.1;
    public static final double ROTATION_KP = 0.1;

    /*
     * https://www.swervedrivespecialties.com/products/mk4i-swerve-module
     */
    public static class Encoders {
        public static final double DISTANCE_GEARING = 6.75;
        public static final double DIRECTION_GEARING = (double) 150 / 7;
    }
}
