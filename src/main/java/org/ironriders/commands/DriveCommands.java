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

    /**
     * Generates a path to the specified destination using default settings false for velocity control which will stop
     * the robot abruptly when it reaches the target.
     *
     * @param target The destination pose represented by a Pose2d object.
     * @return A Command object representing the generated path to the destination.
     */
    public Command pathFindTo(Pose2d target) {
        return pathFindTo(target, false);
    }
    /**
     * Generates a path to the specified destination with options for velocity control.
     *
     * @param target              The destination pose represented by a Pose2d object.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the robot will slow down smoothly; if false, it will stop abruptly.
     * @return A Command object representing the generated path to the destination.
     */
    public Command pathFindTo(Pose2d target, boolean preserveEndVelocity) {
        return AutoBuilder.pathfindToPose(
                target,
                CONSTRAINTS,
                preserveEndVelocity ? CONSTRAINTS.getMaxVelocityMps() : 0
        );
    }


    /**
     * Generates a path to a specified target identified by a vision tag. This will run only if the id is provided if
     * the id is not provided it will return Command that does nothing and immediately closes itself.
     *
     * @param id     The identifier of the vision tag.
     * @param offset The transformation to be applied to the identified target's pose.
     * @return A Command object representing the generated path to the identified target.
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
    /**
     * Generates a command to make the robot follow a pre-defined path with the default settings of preserveEndVelocity
     * false and resetOdometry false.
     * The default settings include preserving the robot's end velocity and not resetting odometry.
     *
     * @param path The pre-defined path that the robot should follow.
     * @return A Command object representing the sequence of actions to follow the specified path with default settings.
     */
    public Command followPath(Path path) {
        return followPath(path, false, false);
    }
    /**
     * Generates a command to make the robot follow a pre-defined path with the option to preserve the velocity at the
     * end and the default settings of resetOdometry as false.
     *
     * @param path              The pre-defined path that the robot should follow.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the robot will slow down smoothly; if false, it will stop abruptly.
     * @return A Command object representing the sequence of actions to follow the specified path.
     */
    public Command followPath(Path path, boolean preserveEndVelocity) {
        return followPath(path, preserveEndVelocity, false);
    }
    /**
     * Generates a command to make the robot follow a pre-defined path using a combination of pose planning and path
     * following.
     *
     * @param path              The pre-defined path that the robot should follow.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the robot will slow down smoothly; if false, it will stop abruptly.
     * @param resetOdometry     A boolean flag indicating whether to reset the robot's odometry to the starting pose of
     *                          the path.
     * @return A Command object representing the sequence of actions to follow the specified path.
     */
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

    /**
     * A utility method that temporarily enables vision-based pose estimation, executes a specified command,
     * and then disables vision-based pose estimation again.
     *
     * @param command The Command object to be executed after enabling vision-based pose estimation.
     * @return A new Command object representing the sequence of actions including vision-based pose estimation.
     */
    public Command useVisionForPoseEstimation(Command command) {
        return Commands
                .runOnce(() -> vision.useVisionForPoseEstimation(true))
                .andThen(command)
                .andThen(Commands.runOnce(() -> vision.useVisionForPoseEstimation(false)));
    }
}
