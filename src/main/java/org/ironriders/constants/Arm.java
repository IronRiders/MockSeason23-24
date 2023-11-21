package org.ironriders.constants;

public class Arm {
    public static final double SPEED = 0.5;
    public static final double TOLERANCE = 0.1;
    public static final double ENCODER_OFFSET = 0;
    public static final double GEARING = 1;
    public static final int CURRENT_LIMIT = 40;

    public static class Limit {
        public static final float REVERSE = 0;
        public static final float FORWARD = 90;
    }

    public static class PIDFF {
        public static final double P = 0.05;
        public static final double I = 0;
        public static final double D = 0;

        public static final double S = 0;
        public static final double G = 0;
        public static final double V = 0;
    }
}
