package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Path;
import org.ironriders.subsystems.DriveSubsystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;

import java.util.function.DoubleSupplier;

import static org.ironriders.constants.Auto.Drive.CONSTRAINTS;

public class DriveCommands {
    private final DriveSubsystem drive;
    private final SwerveDrive swerveDrive;

    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;
        this.swerveDrive = drive.getSwerveDrive();
    }

    public Command teleopCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier rotation) {
        SwerveControllerConfiguration config = swerveDrive.swerveController.config;

        return Commands.run(
                () -> drive.getSwerveDrive().drive(
                        new Translation2d(x.getAsDouble() * config.maxSpeed, y.getAsDouble() * config.maxSpeed),
                        rotation.getAsDouble() * config.maxAngularVelocity,
                        true,
                        false,
                        true
                ),
                drive
        );
    }

    public Command pathFindTo(Pose2d target) {
        return AutoBuilder.pathfindToPose(
                target,
                CONSTRAINTS
        );
    }

    public Command followPath(Path path) {
        return followPath(path, false);
    }

    public Command followPath(Path path, boolean resetOdometry) {
        PathPlannerPath pathPlannerPath = PathPlannerPath.fromPathFile(path.name());

        if (resetOdometry) {
            swerveDrive.resetOdometry(pathPlannerPath.getStartingDifferentialPose());
            return AutoBuilder.followPathWithEvents(pathPlannerPath);
        }

        return pathFindTo(pathPlannerPath.getStartingDifferentialPose()).andThen(
                AutoBuilder.followPathWithEvents(pathPlannerPath)
        );
    }
}
