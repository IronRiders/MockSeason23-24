package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.lib.Path;
import org.ironriders.subsystems.DriveSubsystem;
import swervelib.SwerveDrive;

import static org.ironriders.constants.Auto.Drive.CONSTRAINTS;

public class DriveCommands {
    private final SwerveDrive swerveDrive;

    public DriveCommands(DriveSubsystem drive) {
        this.swerveDrive = drive.getSwerveDrive();
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
        resetOdometry(resetOdometry);

        return AutoBuilder.pathfindToPose(
                pathPlannerPath.getStartingDifferentialPose(),
                CONSTRAINTS
        ).andThen(
                AutoBuilder.followPathWithEvents(pathPlannerPath)
        );
    }

    private void resetOdometry(boolean reset) {
        if (reset) {
            swerveDrive.resetOdometry(new Pose2d());
        }
    }
}
