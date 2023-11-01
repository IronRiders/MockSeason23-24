package org.ironriders.subsystems;

import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.telemetry.SwerveDriveTelemetry;

public class DriveSubsystem extends SwerveDrive {
    public DriveSubsystem(SwerveDriveConfiguration config, SwerveControllerConfiguration controllerConfig) {
        super(config, controllerConfig);

        SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
    }
}
