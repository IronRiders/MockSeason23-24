// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.DriveCommands;
import org.ironriders.commands.RobotCommands;
import org.ironriders.constants.Ports;
import org.ironriders.constants.Teleop;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.ArmSubsystem;
import org.ironriders.subsystems.DashboardSubsystem;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Teleop.Controllers.Joystick;
import static org.ironriders.constants.Teleop.Speed.DEADBAND;
import static org.ironriders.constants.Teleop.Speed.MIN_MULTIPLIER;

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
    private final DashboardSubsystem dashboard = new DashboardSubsystem();
    private final CommandXboxController primaryController =
            new CommandXboxController(Ports.Controllers.PRIMARY_CONTROLLER);
    private final CommandJoystick secondaryController =
            new CommandJoystick(Ports.Controllers.SECONDARY_CONTROLLER);
    private final RobotCommands commands = new RobotCommands(arm, drive, manipulator);
    private final DriveCommands driveCommands = drive.getCommands();

    /**
     * The container for the robot. Contains subsystems, IO devices, and commands.
     */
    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        Command switchDropOff = commands.switchDropOff();
        Command exchange = commands.exchange();
        Command exchangeReturn = commands.exchangeReturn();
        Command portal = commands.portal();

        Command cancelAuto = Commands.runOnce(() -> {
            switchDropOff.cancel();
            exchange.cancel();
            exchangeReturn.cancel();
            portal.cancel();
        });

        // Primary Driver
        drive.setDefaultCommand(
                driveCommands.teleopCommand(
                        () -> -controlCurve(primaryController.getLeftY()),
                        () -> -controlCurve(primaryController.getLeftX()),
                        () -> -controlCurve(primaryController.getRightX())
                )
        );

        primaryController.rightBumper().onTrue(cancelAuto);
        primaryController.leftBumper().onTrue(cancelAuto);

        primaryController.a().onTrue(commands.groundPickup());
        primaryController.b().onTrue(switchDropOff);
        primaryController.x().onTrue(portal);
        primaryController.y().onTrue(exchange);

        primaryController.leftStick().onTrue(commands.driving());
        primaryController.rightStick().onTrue(commands.driving());

        // Secondary Driver
        secondaryController.button(6).onTrue(exchange);
        secondaryController.button(7).onTrue(exchangeReturn);
        secondaryController.button(8).onTrue(portal);
        secondaryController.button(9).onTrue(switchDropOff);
        secondaryController.button(10).onTrue(commands.groundPickup());
        secondaryController.button(11).onTrue(commands.resting());
        secondaryController.button(12).onTrue(commands.driving());
    }

    private double controlCurve(double input) {
        // Multiplier based on trigger axis (whichever one is larger) then scaled to start at 0.35
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND) * (
                Utils.controlCurve(
                        Math.max(primaryController.getLeftTriggerAxis(), primaryController.getRightTriggerAxis()),
                        Teleop.Speed.EXPONENT,
                        DEADBAND
                ) * (1 - MIN_MULTIPLIER) + MIN_MULTIPLIER
        );
    }

    public Command getEnableCommand() {
        return arm.getCommands().reset();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return dashboard.getAuto();
    }
}
