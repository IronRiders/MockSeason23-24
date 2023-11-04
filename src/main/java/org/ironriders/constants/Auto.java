package org.ironriders.constants;

import edu.wpi.first.math.controller.PIDController;
import swervelib.parser.PIDFConfig;

public class Auto {
    public static class Drive {
        public static final PIDController xPID = new PIDFConfig(0.7, 0, 0).createPIDController();
        public static final PIDController yPID = new PIDFConfig(0.7, 0, 0).createPIDController();
        public static final PIDController anglePID = new PIDFConfig(0.4, 0, 0.01).createPIDController();
    }
}
