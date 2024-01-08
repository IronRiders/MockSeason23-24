package org.ironriders.constants;

public class Ports {
    /**
     * Driver station ports for the MOTORs.
     */
    public static class Controllers {
        public static final int PRIMARY_CONTROLLER = 0;
        public static final int SECONDARY_CONTROLLER = 1;
    }

    /**
     * Ports for each addressable LED strip.
     * <p>
     * S = Strip
     */
    public static class Lights {
        public static class S1 {
            public static final int RSL = 0;
        }
    }

    public static class Arm {
        public static final int RIGHT_MOTOR = 10;
        public static final int LEFT_MOTOR = 11;

        public static final int PRIMARY_ENCODER = 0;
        public static final int SECONDARY_ENCODER = 1;
    }

    public static class Manipulator {
        public static final int RIGHT_MOTOR = 12;
        public static final int LEFT_MOTOR = 13;

    }
}
