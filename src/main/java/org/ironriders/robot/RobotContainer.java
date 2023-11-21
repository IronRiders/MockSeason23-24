// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.DriveCommands;
import org.ironriders.constants.Ports;
import org.ironriders.lib.Utils;
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
    private final CommandXboxController controller =
            new CommandXboxController(Ports.Controllers.CONTROLLER);
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

        drive.getVision().setDefaultCommand(
                driveCommands.pathFindToCube(new Transform2d())
        );

        controller.button(1).onTrue(driveCommands.setGyro(new Rotation3d()));
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
        return driveCommands.pathFindToTag(1, new Transform2d(1, 0, new Rotation2d()));
    }
}
