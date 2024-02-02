package org.ironriders.constants;

public class Drive {
    public static final String SWERVE_CONFIG_LOCATION = "swerve";
    public static final double MAX_SPEED = 4.5;

    public static class HeadingController {
        public static final double SPEED_CAP = 0.3;

        public static final double P = 0.001;
        public static final double I = 0;
        public static final double D = 0;
    }
}
