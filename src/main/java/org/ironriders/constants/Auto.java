package org.ironriders.constants;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;

import static org.ironriders.constants.Auto.AutoLocation.*;

public class Auto {
    public enum AutoOption {
        NONE(null, null),
        A_B(A, B),
        B_B(B, B),
        B_C(B, C),
        C_C(C, C),
        A_C(A, C);

        private final AutoLocation start;
        private final AutoLocation end;

        AutoOption(AutoLocation start, AutoLocation end) {
            this.start = start;
            this.end = end;
        }

        public static AutoOption getDefault(int i) {
            return switch (i) {
                case 0 -> AutoOption.A_B;
                case 1 -> AutoOption.B_C;
                case 2, 3 -> AutoOption.C_C;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }

        public boolean isCompatible(AutoOption other) {
            if (this.equals(NONE) || other.equals(NONE)) return true;
            return end.equals(other.getStart());
        }

        public AutoLocation getStart() {
            return start;
        }
    }

    public enum AutoLocation {
        A,
        B,
        C
    }

    public enum PathfindingConstraintProfile {
        SLOW(new PathConstraints(
                1,
                0.5,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        )),
        MEDIUM(new PathConstraints(
                2,
                1.5,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        )),
        FAST(new PathConstraints(
                2.5,
                2,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        ));

        private final PathConstraints constraints;

        PathfindingConstraintProfile(PathConstraints constraints) {
            this.constraints = constraints;
        }

        public static PathfindingConstraintProfile getDefault() {
            return SLOW;
        }

        public PathConstraints getConstraints() {
            return constraints;
        }
    }
}
