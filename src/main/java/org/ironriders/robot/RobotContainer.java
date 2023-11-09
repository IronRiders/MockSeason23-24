// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import org.ironriders.commands.PivotCommands;
import org.ironriders.constants.Ports;
import org.ironriders.subsystems.PivotSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    private final CommandJoystick joystick = new CommandJoystick(Ports.JOYSTICK);
    private final PivotSubsystem pivot = new PivotSubsystem();
    private final PivotCommands pivotCommands = new PivotCommands(pivot).registerNamedCommands();

    /**
     * The container for the robot. Contains subsystems, IO devices, and commands.
     */
    public RobotContainer() {
        configureBindings();

        pivot.setDefaultCommand(
                pivotCommands.setPivot(joystick.getTwist())
        );
    }

    private void configureBindings() {
        joystick.button(7).onTrue(pivotCommands.setPivot(2));
    }
    
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return null;
    }
}
