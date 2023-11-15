package org.ironriders.constants;

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
 * LB = Pounds
 */
public class Game {
    public static class Field {
        public static final int FIELD_LENGTH = 54; // FT
        public static final int FIELD_WIDTH = 27;  // FT
        public static final double SWITCH_WALL_HEIGHT = 18.75; // IN
        public static final int SWITCH_PLATE_LENGTH = 3; // FT
        public static final int SWITCH_PLATE_WIDTH = 4; // FT
        public static final double SWITCH_FENCE_TO_PLATE_GAP = 1.5; // IN
        public static final int PORTAL_HEIGHT_OFF_GROUND = 20; // IN (From the ground to the lowest point)
        public static final int PORTAL_HEIGHT = 14; // IN (It's a square you figure out the other side)
        public static final double EXCHANGE_HEIGHT = 16.25; // IN
        public static final double EXCHANGE_WIDTH = 21; // IN (Ramp has same enough width for practical use)
        public static final double EXCHANGE_RAMP_HEIGHT  = 1.75; // IN
        public static final double EXCHANGE_RAMP_LENGTH  = 6.5; // IN
        public static final int EXCHANGE_RAMP_WIDTH  = -1; // IN (NOT ASSIGNED DO NOT USE UNTIL SOMEONE FINDS IT)


    }

    public static class GamePieces {
        public static final int POWERCUBE_HEIGHT = 11; // IN
        public static final int POWERCUBE_LENGTH = 13; // IN
        public static final int POWERCUBE_WIDTH = 13; // IN (OMG IT'S A SQUARE!)
        public static final double POWERCUBE_WEIGHT = 3.25; // LB
    }
}
