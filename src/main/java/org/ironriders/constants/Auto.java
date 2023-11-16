package org.ironriders.constants;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;

public class Auto {
    public static class Drive {
        public static final PathConstraints CONSTRAINTS = new PathConstraints(
                3.0,
                4.0,
                Units.degreesToRadians(540),
                Units.degreesToRadians(720)
        );
    }
}
