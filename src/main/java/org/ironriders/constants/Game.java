package org.ironriders.constants;

import java.util.Arrays;

/**
 * Contains game specific constants.
 * <p>
 * M = Meters
 * CM = Centimeters
 * MM = Millimeters
 * <p>
 * Y = Yards
 * FT = Feet
 * IN = Inches
 */
public class Game {
    public static class Field {
        public enum AprilTagLocation {
            EXCHANGE(1, 2),
            PORTAL(3, 4, 5, 6),
            SWITCH(1, 7, 8, 9, 10, 11, 12, 13, 14);

            final int[] ids;

            AprilTagLocation(int... ids) {
                this.ids = ids;
            }

            public boolean has(int id) {
                return Arrays.stream(ids).anyMatch(i -> i == id);
            }
        }

        public static class Dimensions {
            /**
             * In FT.
             */
            public static final int FIELD_LENGTH = 54;
            /**
             * In FT.
             */
            public static final int FIELD_WIDTH = 27;
        }

        public static class Switch {
            /**
             * In IN.
             */
            public static final double SWITCH_WALL_HEIGHT = 18.75;
            /**
             * In FT.
             */
            public static final int SWITCH_PLATE_LENGTH = 3;
            /**
             * In FT.
             */
            public static final int SWITCH_PLATE_WIDTH = 4;
            /**
             * In IN.
             */
            public static final double SWITCH_FENCE_TO_PLATE_GAP = 1.5;
        }

        public static class Portal {
            /**
             * In IN, from the ground to the lowest point.
             */
            public static final int PORTAL_HEIGHT_OFF_GROUND = 20;
            /**
             * In IN.
             */
            public static final int PORTAL_HEIGHT = 14;
            /**
             * In IN.
             */
            public static final int PORTAL_WIDTH = 14;
        }

        public static class Exchange {
            /**
             * In IN.
             */
            public static final double EXCHANGE_HEIGHT = 16.25;
            /**
             * In IN.
             */
            public static final double EXCHANGE_WIDTH = 21;
            /**
             * In IN.
             */
            public static final double EXCHANGE_RAMP_HEIGHT = 1.75;
            /**
             * In IN.
             */
            public static final double EXCHANGE_RAMP_LENGTH = 6.5;
            /**
             * In IN.
             */
            public static final int EXCHANGE_RAMP_WIDTH = -1;
        }
    }

    public static class GamePieces {
        /**
         * In pounds.
         */
        public static final double POWER_CUBE_WEIGHT = 3.25;

        public static class Dimensions {
            /**
             * In IN.
             */
            public static final int POWER_CUBE_HEIGHT = 11;
            /**
             * In IN.
             */
            public static final int POWER_CUBE_LENGTH = 13;
            /**
             * In IN.
             */
            public static final int POWER_CUBE_WIDTH = 13;
        }
    }
}
