package org.ironriders.constants;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

public class Arm {
    public static final double LENGTH_FROM_ORIGIN = Units.inchesToMeters(50);
    public static final double TOLERANCE = 0.3;
    public static final double FAILSAFE_DIFFERENCE = 7;
    public static final double GEARING = 500;
    public static final double PRIMARY_ENCODER_OFFSET = -124;
    public static final double SECONDARY_ENCODER_OFFSET = 44;
    public static final double TIMEOUT = 4;
    public static final int CURRENT_LIMIT = 15;

    public enum State {
        REST(0),
        SWITCH(40),
        EXCHANGE(0),
        EXCHANGE_RETURN(0),
        PORTAL(0),
        FULL(0);

        final int position;

        State(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class Limit {
        public static final float REVERSE = 0;
        public static final float FORWARD = 70;
    }

    public static class PID {

        public static final double P = 0.075;
        public static final double I = 0;
        public static final double D = 0;

        public static final TrapezoidProfile.Constraints PROFILE =
                new TrapezoidProfile.Constraints(25, 20);
    }
}
