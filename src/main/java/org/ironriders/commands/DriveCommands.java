package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.AutoConfig;
import org.ironriders.lib.Path;
import org.ironriders.lib.VisionPipeline;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.VisionSubsystem;
import org.photonvision.PhotonUtils;
import swervelib.SwerveController;
import swervelib.SwerveDrive;

import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import static org.ironriders.constants.Auto.Drive.CONSTRAINTS;
import static org.ironriders.constants.Drive.MAX_SPEED;
import static org.ironriders.constants.Robot.LIMELIGHT_POSITION;

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
                () -> swerve.swerveController.getTargetSpeeds(
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
    public Command setGyro(Rotation3d rotation) {
        return Commands.runOnce(() -> swerve.setGyro(rotation), drive);
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
        return Commands.runOnce(
                () -> swerve.drive(
                        SwerveController.getTranslation2d(speeds.get()),
                        speeds.get().omegaRadiansPerSecond,
                        fieldCentric,
                        openLoop
                ),
                drive
        );
    }

    /**
     * Creates a command to navigate to a cube using a predefined path offset.
     *
     * @param offset The additional transformation to adjust the path.
     * @return A command to navigate to a cube with the given offset.
     */
    public Command pathFindToCube(Transform2d offset) {
        return pathFindToCube(offset, 0);
    }

    /**
     * Creates a command to navigate to a cube with a specified path offset and target height.
     *
     * @param offset       The additional transformation to adjust the path.
     * @param targetHeight The height of the target cube.
     * @return A command to navigate to a cube with the given offset and target height.
     */
    public Command pathFindToCube(Transform2d offset, double targetHeight) {
        SmartDashboard.putString(
                "Distance",
                String.valueOf(PhotonUtils.calculateDistanceToTargetMeters(
                        LIMELIGHT_POSITION.getZ(),
                        targetHeight,
                        0,
                        0
                ))
        );

        return Commands.runOnce(() -> vision.setPipeline(VisionPipeline.CUBES), vision).andThen(
                pathFindTo(
                        swerve.getPose().plus(
                                new Transform2d(
                                        PhotonUtils.estimateCameraToTargetTranslation(
                                                PhotonUtils.calculateDistanceToTargetMeters(
                                                        LIMELIGHT_POSITION.getZ(),
                                                        targetHeight,
                                                        0,
                                                        0
                                                ),
                                                Rotation2d.fromDegrees(vision.getResult().getBestTarget().getYaw())
                                        ),
                                        new Rotation2d()
                                ).plus(offset)
                        )
                ).onlyIf(() -> vision.getResult().hasTargets())
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
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
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
                pathFindTo(optionalPose.get().toPose2d().plus(offset.inverse()))
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
     * @param path              The pre-defined path that the robot should follow.
     * @param preserveEndVelocity A boolean flag indicating whether to preserve velocity at the end of the path.
     *                            If true, the path will end with the robot going the max velocity; if false, it will
     *                            stop abruptly.
     * @param resetOdometry     A boolean flag indicating whether to reset the robot's odometry to the starting pose of
     *                          the path.
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
     * Builds an autonomous command based on the provided {@code AutoConfig}.
     *
     * @param auto The configuration object specifying the autonomous routine.
     * @return A command representing the autonomous routine specified by the {@code AutoConfig}.
     */
    public Command buildAuto(AutoConfig auto) {
        return AutoBuilder.buildAuto(auto.name());
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
