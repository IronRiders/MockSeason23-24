// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.ironriders.commands.DriveCommands;
import org.ironriders.commands.RobotCommands;
import org.ironriders.constants.Ports;
import org.ironriders.constants.Teleop;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.ArmSubsystem;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Auto.DEFAULT_AUTO;
import static org.ironriders.constants.Teleop.Controllers.Joystick;
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
    private final CommandXboxController primaryController =
            new CommandXboxController(Ports.Controllers.PRIMARY_CONTROLLER);
    private final CommandXboxController secondaryController =
            new CommandXboxController(Ports.Controllers.SECONDARY_CONTROLLER);
    private final RobotCommands commands = new RobotCommands(arm, drive, manipulator);
    private final DriveCommands driveCommands = drive.getCommands();
    private final SendableChooser<String> autoOptionSelector = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, IO devices, and commands.
     */
    public RobotContainer() {
        for (String auto : AutoBuilder.getAllAutoNames()) {
            if (auto.equals("REGISTERED_COMMANDS")) continue;
            autoOptionSelector.addOption(auto, auto);
        }
        autoOptionSelector.setDefaultOption(DEFAULT_AUTO, DEFAULT_AUTO);
        SmartDashboard.putData("auto/Auto Option", autoOptionSelector);

        configureBindings();
    }

    private void configureBindings() {
        drive.setDefaultCommand(
                driveCommands.teleopCommand(
                        () -> -controlCurve(primaryController.getLeftY()),
                        () -> -controlCurve(primaryController.getLeftX()),
                        () -> -controlCurve(primaryController.getRightX())
                )
        );

        primaryController.a().onTrue(commands.groundPickup());
        primaryController.b().onTrue(commands.switchDropOff());
        primaryController.x().onTrue(commands.portal());
        primaryController.y().onTrue(commands.exchange());
    }

    private double controlCurve(double input) {
        // Multiplier based on trigger axis (whichever one is larger) then scaled to start at 0.35
        // TODO: Test this
        return Utils.controlCurve(input, Joystick.EXPONENT, Joystick.DEADBAND) * (
                Utils.controlCurve(
                        Math.max(primaryController.getLeftTriggerAxis(), primaryController.getRightTriggerAxis()),
                        Teleop.Speed.EXPONENT,
                        0
                ) * (1 - MIN_MULTIPLIER) + MIN_MULTIPLIER
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return commands.buildAuto(autoOptionSelector.getSelected());
    }
}
