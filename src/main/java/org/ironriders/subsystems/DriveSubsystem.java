package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;

import static org.ironriders.constants.Robot.Dimensions;

public class DriveSubsystem extends SubsystemBase {
    private final VisionSubsystem vision = new VisionSubsystem();
    private final SwerveDrive swerveDrive;

    public DriveSubsystem() {
        try {
            swerveDrive = new SwerveParser(
                    new File(Filesystem.getDeployDirectory(), Drive.SWERVE_CONFIG_LOCATION)
            ).createSwerveDrive();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

        AutoBuilder.configureHolonomic(
                swerveDrive::getPose,
                swerveDrive::resetOdometry,
                swerveDrive::getRobotVelocity,
                swerveDrive::setChassisSpeeds,
                new HolonomicPathFollowerConfig(
                        4.5,
                        Dimensions.DRIVEBASE_RADIUS,
                        new ReplanningConfig()
                ),
                this
        );
    }

    @Override
    public void periodic() {
        getVision().getPoseEstimate().ifPresent(estimatedRobotPose -> swerveDrive.addVisionMeasurement(
                estimatedRobotPose.estimatedPose.toPose2d(),
                estimatedRobotPose.timestampSeconds,
                false,
                1
        ));
    }

    public VisionSubsystem getVision() {
        return vision;
    }

    public SwerveDrive getSwerveDrive() {
        return swerveDrive;
    }
}
