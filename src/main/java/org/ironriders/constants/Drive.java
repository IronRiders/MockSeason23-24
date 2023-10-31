package org.ironriders.constants;

public class Drive {
    public static final String SWERVE_CONFIG_LOCATION = "swerve";

    /*
     * https://www.swervedrivespecialties.com/products/mk4i-swerve-module
     */
    public static class Encoders {
        public static final double DISTANCE_GEARING = 6.75;
        public static final double DIRECTION_GEARING = (double) 150 / 7;
    }
}
