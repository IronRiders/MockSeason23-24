package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Arm;
import org.ironriders.constants.Auto.Path;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.VisionSubsystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;

import java.util.Optional;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import static org.ironriders.constants.Drive.MAX_SPEED;

public class DriveCommands {
    private final DriveSubsystem drive;
    private final SwerveDrive swerve;
    private final VisionSubsystem vision;

    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;
        this.vision = drive.getVision();
        this.swerve = drive.getSwerveDrive();
    }

    /**
     * Creates a teleoperated command for swerve drive using joystick inputs.
     *
     * @param x The x-axis joystick input.
     * @param y The y-axis joystick input.
     * @param a The rotation joystick input.
     * @return a command to control the swerve drive during teleop.
     */
    public Command teleopCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier a) {
        return drive(
                () -> swerve.getSwerveController().getTargetSpeeds(
                        x.getAsDouble(),
                        y.getAsDouble(),
                        a.getAsDouble() * 2 + swerve.getYaw().getRadians(),
                        swerve.getYaw().getRadians(),
                        MAX_SPEED
                )
        );
    }

    /**
     * Creates a command to set the gyro orientation to a specified rotation.
     *
     * @param rotation The desired rotation for the gyro.
     * @return A command to set the gyro orientation.
     */
    public Command setGyro(Pose2d rotation) {
        return drive.runOnce(() -> swerve.resetOdometry(rotation));
    }

    /**
     * Creates a command to drive the swerve robot using specified speeds. Must be repeated to work.
     *
     * @param speeds The supplier providing the desired chassis speeds.
     * @return A command to drive the swerve robot with the specified speeds.
     */
    public Command drive(Supplier<ChassisSpeeds> speeds) {
        return drive(speeds, true, false);
    }

    /**
     * Creates a command to drive the swerve robot using specified speeds and control options. Must be repeated to
     * work.
     *
     * @param speeds       The supplier providing the desired chassis speeds.
     * @param fieldCentric Whether the control is field-centric.
     * @param openLoop     Whether the control is open loop.
     * @return A command to drive the swerve robot with the specified speeds and control options.
     */
    public Command drive(Supplier<ChassisSpeeds> speeds, boolean fieldCentric, boolean openLoop) {
        return drive.runOnce(() -> swerve.drive(
                SwerveController.getTranslation2d(speeds.get()),
                speeds.get().omegaRadiansPerSecond,
                fieldCentric,
                openLoop
        ));
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
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
     * @return A Command object representing the generated path to the destination.
     */
    public Command pathFindTo(Pose2d target, boolean preserveEndVelocity) {
        return Commands.defer(() -> AutoBuilder.pathfindToPose(
                target,
                drive.getPathfindingConstraint().getConstraints(),
                preserveEndVelocity ? drive.getPathfindingConstraint().getConstraints().getMaxVelocityMps() : 0
        ).withTimeout(10), Set.of(drive));
    }

    public Command pathFindToTag(int id) {
        return pathFindToTag(id, -Arm.LENGTH_FROM_ORIGIN);
    }

    /**
     * Generates a path to a specified target identified by a vision tag. This will run only if the id is provided if
     * the id is not provided it will return Command that does nothing and immediately closes itself.
     *
     * @param id     The identifier of the vision tag.
     * @param offset The transformation to be applied to the identified target's pose.
     * @return A Command object representing the generated path to the identified target.
     */
    public Command pathFindToTag(int id, double offset) {
        Optional<Pose3d> pose = vision.getTag(id);
        if (pose.isEmpty()) {
            return Commands.none();
        }

        return useVisionForPoseEstimation(
                pathFindTo(Utils.accountedPose(pose.get().toPose2d(), offset).plus(
                        new Transform2d(new Translation2d(), Rotation2d.fromDegrees(180))
                ))
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
     * @param path                The pre-defined path that the robot should follow.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
     * @return A Command object representing the sequence of actions to follow the specified path.
     */
    public Command followPath(Path path, boolean preserveEndVelocity) {
        return followPath(path, preserveEndVelocity, false);
    }

    /**
     * Generates a command to make the robot follow a pre-defined path using a combination of pose planning and path
     * following.
     *
     * @param path                The pre-defined path that the robot should follow.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
     * @param resetOdometry       A boolean flag indicating whether to reset the robot's odometry to the starting pose of
     *                            the path.
     * @return A Command object representing the sequence of actions to follow the specified path.
     */
    public Command followPath(Path path, boolean preserveEndVelocity, boolean resetOdometry) {
        PathPlannerPath pathPlannerPath = PathPlannerPath.fromPathFile(path.name());

        if (resetOdometry) {
            swerve.resetOdometry(pathPlannerPath.getStartingDifferentialPose());
            return AutoBuilder.followPathWithEvents(pathPlannerPath);
        }

        return useVisionForPoseEstimation(
                pathFindTo(pathPlannerPath.getStartingDifferentialPose(), preserveEndVelocity)
                        .andThen(AutoBuilder.followPathWithEvents(pathPlannerPath))
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
        return vision
                .runOnce(() -> vision.useVisionForPoseEstimation(true))
                .andThen(command)
                .finallyDo(() -> vision.useVisionForPoseEstimation(false));
    }

    public Command resetOdometry() {
        return resetOdometry(new Pose2d());
    }

    public Command resetOdometry(Pose2d pose) {
        return drive.runOnce(() -> drive.getSwerveDrive().resetOdometry(pose));
    }
}
