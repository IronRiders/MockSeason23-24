package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj2.command.Subsystem;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;

public class DriveSubsystem extends SwerveDrive {
    public DriveSubsystem(SwerveDriveConfiguration config, SwerveControllerConfiguration controllerConfig) {
        super(config, controllerConfig);

        AutoBuilder.configureRamsete(
                this::getPose,
                this::resetOdometry,
                this::getRobotVelocity,
                this::setChassisSpeeds,
                new ReplanningConfig(),
                (Subsystem) this
        );
    }
}
