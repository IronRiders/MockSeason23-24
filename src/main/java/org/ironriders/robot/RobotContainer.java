// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.DriveCommands;
import org.ironriders.commands.RobotCommands;
import org.ironriders.constants.Ports;
import org.ironriders.lib.AutoConfig;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.ArmSubsystem;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Teleop.Controllers.Joystick;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    private final DriveSubsystem drive = new DriveSubsystem();
    private final ManipulatorSubsystem manipulator = new ManipulatorSubsystem();
    private final ArmSubsystem arm = new ArmSubsystem();
    private final CommandXboxController controller = new CommandXboxController(Ports.Controllers.CONTROLLER);
    private final RobotCommands commands = new RobotCommands(arm, drive, manipulator);
    private final DriveCommands driveCommands = drive.getCommands();

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

        controller.a().onTrue(arm.getCommands().setPivot(50));
        controller.b().onTrue(commands.groundPickup());
    }

    private double controlCurve(double input) {
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND) * 0.3;
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return commands.buildAuto(AutoConfig.TEST);
    }
}
