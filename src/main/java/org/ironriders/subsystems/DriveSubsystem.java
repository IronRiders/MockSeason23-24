package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.ironriders.constants.Drive;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;

import static org.ironriders.constants.Robot.Dimensions;

public class DriveSubsystem extends SwerveDrive implements Subsystem {
    public DriveSubsystem(SwerveDriveConfiguration config, SwerveControllerConfiguration controllerConfig) {
        super(config, controllerConfig);
        SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

        AutoBuilder.configureHolonomic(
                this::getPose,
                this::resetOdometry,
                this::getRobotVelocity,
                this::setChassisSpeeds,
                new HolonomicPathFollowerConfig(
                        4.5,
                        Dimensions.DRIVEBASE_RADIUS,
                        new ReplanningConfig()
                ),
                this
        );
    }
}
