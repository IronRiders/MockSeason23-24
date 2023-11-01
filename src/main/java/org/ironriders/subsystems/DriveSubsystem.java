package org.ironriders.subsystems;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;

public class DriveSubsystem extends SubsystemBase {
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
    }

    public SwerveDrive getSwerveDrive() {
        return swerveDrive;
    }
}
