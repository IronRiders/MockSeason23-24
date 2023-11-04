package org.ironriders.constants;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import swervelib.parser.PIDFConfig;

public class Auto {
    public static class Drive {
        public static final PathConstraints CONSTRAINTS = new PathConstraints(
                3.0,
                4.0,
                Units.degreesToRadians(540),
                Units.degreesToRadians(720)
        );

        public static final PIDController xPID = new PIDFConfig(0.7, 0, 0).createPIDController();
        public static final PIDController yPID = new PIDFConfig(0.7, 0, 0).createPIDController();
        public static final PIDController anglePID = new PIDFConfig(0.4, 0, 0.01).createPIDController();
    }
}
