package org.ironriders.constants;

public class Ports {
    /**
     * Driver station ports for the controllers.
     */
    public static class Controllers {
        public static final int JOYSTICK = 0;
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

    /**
     * Ports for each swerve module on the drive base.
     * <p>
     * F = Front
     * B = Back
     * R = Right
     * L = Left
     */
    public static class Drive {
        public static class FR {
            public static final int DIRECTION_CONTROLLER = 0;
            public static final int SPEED_CONTROLLER = 0;
        }
        public static class FL {
            public static final int DIRECTION_CONTROLLER = 0;
            public static final int SPEED_CONTROLLER = 0;
        }
        public static class BR {
            public static final int DIRECTION_CONTROLLER = 0;
            public static final int SPEED_CONTROLLER = 0;
        }
        public static class BL {
            public static final int DIRECTION_CONTROLLER = 0;
            public static final int SPEED_CONTROLLER = 0;
        }
    }
}
