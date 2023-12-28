package org.ironriders.constants;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;

public class Auto {
    public enum AutoOption {
        TEST,
        DOUBLE_C;

        public static AutoOption getDefault() {
            return TEST;
        }
    }

    public static class Drive {
        public static final PathConstraints CONSTRAINTS = new PathConstraints(
                2.5,
                2,
                Units.degreesToRadians(360),
                Units.degreesToRadians(180)
        );
    }
}
