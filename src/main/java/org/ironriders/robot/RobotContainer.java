// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Ports;
import org.ironriders.lib.Utils;
import org.ironriders.lib.controllers.CommandXboxSeriesController;
import org.ironriders.subsystems.DriveSubsystem;

import static org.ironriders.constants.Teleop.Controllers.Joystick;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();
    private final CommandXboxSeriesController controller =
            new CommandXboxSeriesController(Ports.Controllers.CONTROLLER);
    DriveCommands driveCommands = new DriveCommands(drive);

    /**
     * The container for the robot. Contains subsystems, IO devices, and commands.
     */
    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        drive.setDefaultCommand(
                driveCommands.teleopCommand(
                        () -> -controlCurve(controller.getLeftY()),
                        () -> -controlCurve(controller.getLeftX()),
                        () -> -controlCurve(controller.getRightX())
                )
        );
    }

    private double controlCurve(double input) {
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return driveCommands.pathFindTo(new Pose2d(5, 7, new Rotation2d()));
    }
}
