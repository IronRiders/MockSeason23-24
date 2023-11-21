package org.ironriders.constants;

public class Ports {
    /**
     * Driver station ports for the MOTORs.
     */
    public static class Controllers {
        public static final int CONTROLLER = 0;
        public static final int KEYPAD = 1;
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

    public static class Manipulator {
        public static final int RIGHT_MOTOR = 1; // TODO: put actual port number
        public static final int LEFT_MOTOR = 2; // TODO: put actual port number
    }

    /**
     * Ports for each swerve module on the drive base.
     * <p>
     * F = Front
     * B = Back
     * R = Right
     * L = Left
     */
    public static class Drive {
        public static final int GYRO = 0;

        public static class FR {
            public static final int SPEED_MOTOR = 0;
            public static final int DIRECTION_MOTOR = 0;
            public static final int DIRECTION_ENCODER = 0;
            public static final double DIRECTION_ENCODER_OFFSET = 0;
        }

        public static class FL {
            public static final int SPEED_MOTOR = 0;
            public static final int DIRECTION_MOTOR = 0;
            public static final int DIRECTION_ENCODER = 0;
            public static final double DIRECTION_ENCODER_OFFSET = 0;
        }

        public static class BR {
            public static final int SPEED_MOTOR = 0;
            public static final int DIRECTION_MOTOR = 0;
            public static final int DIRECTION_ENCODER = 0;
            public static final double DIRECTION_ENCODER_OFFSET = 0;
        }

        public static class BL {
            public static final int SPEED_MOTOR = 0;
            public static final int DIRECTION_MOTOR = 0;
            public static final int DIRECTION_ENCODER = 0;
            public static final double DIRECTION_ENCODER_OFFSET = 0;
        }
    }
}
