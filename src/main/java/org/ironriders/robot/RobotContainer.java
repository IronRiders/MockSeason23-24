// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import org.ironriders.commands.AutoOptions;
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

    private final CommandJoystick controller =
            new CommandJoystick(Ports.Controllers.JOYSTICK);
    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        drive.setDefaultCommand(
                Commands.run(
                        () -> drive.getSwerveDrive().drive(
                                new Translation2d(controlCurve(controller.getX()), controlCurve(controller.getY())),
                                controlCurve(controller.getTwist()),
                                true,
                                false,
                                true
                        )
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
        return new AutoOptions().getAuto();
    }
}
