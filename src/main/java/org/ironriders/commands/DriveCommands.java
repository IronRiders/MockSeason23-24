package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Path;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.VisionSubsystem;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import static org.ironriders.constants.Auto.Drive.CONSTRAINTS;

public class DriveCommands {
    private final DriveSubsystem drive;
    private final VisionSubsystem vision;
    private final SwerveDrive swerveDrive;

    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;
        this.vision = drive.getVision();
        this.swerveDrive = drive.getSwerveDrive();
    }

    public Command teleopCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier rotation) {
        SwerveControllerConfiguration config = swerveDrive.swerveController.config;

        return Commands.runOnce(
                () -> drive.getSwerveDrive().drive(
                        new Translation2d(x.getAsDouble() * config.maxSpeed, y.getAsDouble() * config.maxSpeed),
                        rotation.getAsDouble() * config.maxAngularVelocity,
                        false,
                        false,
                        false
                ),
                drive
        );
    }

    public Command pathFindTo(Pose2d target) {
        return pathFindTo(target, false);
    }

    public Command pathFindTo(Pose2d target, boolean preserveEndVelocity) {
        return AutoBuilder.pathfindToPose(
                target,
                CONSTRAINTS,
                preserveEndVelocity ? CONSTRAINTS.getMaxVelocityMps() : 0
        );
    }

    /**
     * Returns an empty command if the tag does not exist.
     */
    public Command pathFindToTag(int id, Transform2d offset) {
        Optional<Pose3d> optionalPose = vision.getTagLayout().getTagPose(id);
        if (optionalPose.isEmpty()) {
            return Commands.none();
        }

        return useVisionForPoseEstimation(
                pathFindTo(optionalPose.get().toPose2d().plus(offset))
        );
    }

    public Command followPath(Path path) {
        return followPath(path, false, false);
    }

    public Command followPath(Path path, boolean preserveEndVelocity) {
        return followPath(path, preserveEndVelocity, false);
    }

    public Command followPath(Path path, boolean preserveEndVelocity, boolean resetOdometry) {
        PathPlannerPath pathPlannerPath = PathPlannerPath.fromPathFile(path.name());

        if (resetOdometry) {
            swerveDrive.resetOdometry(pathPlannerPath.getStartingDifferentialPose());
            return AutoBuilder.followPathWithEvents(pathPlannerPath);
        }

        return pathFindTo(pathPlannerPath.getStartingDifferentialPose(), preserveEndVelocity).andThen(
                AutoBuilder.followPathWithEvents(pathPlannerPath)
        );
    }

    public Command useVisionForPoseEstimation(Command command) {
        return Commands
                .runOnce(() -> vision.useVisionForPoseEstimation(true))
                .andThen(command)
                .andThen(Commands.runOnce(() -> vision.useVisionForPoseEstimation(false)));
    }
}
