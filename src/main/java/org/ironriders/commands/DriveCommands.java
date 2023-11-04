package org.ironriders.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.DriveSubsystem;
import swervelib.SwerveDrive;

import static org.ironriders.constants.Auto.Drive.*;

public class DriveCommands {
    private final DriveSubsystem drive;

    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;
    }

    public Command followTrajectory(PathPlannerTrajectory trajectory, boolean resetOdometry) {
        SwerveDrive swerveDrive = drive.getSwerveDrive();

        if (resetOdometry) {
            swerveDrive.resetOdometry(trajectory.getInitialHolonomicPose());
        }

        return new PPSwerveControllerCommand(
                trajectory,
                swerveDrive::getPose,
                xPID,
                yPID,
                anglePID,
                swerveDrive::setChassisSpeeds,
                drive
        );
    }
}
