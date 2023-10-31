// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.robot;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import org.ironriders.commands.AutoOptions;
import org.ironriders.constants.Drive;
import org.ironriders.constants.Ports;
import org.ironriders.lib.Utils;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

import java.io.File;
import java.io.IOException;

import static org.ironriders.constants.Teleop.Controllers.Joystick;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer extends SubsystemBase {
    private SwerveDrive drive = null;

    private final CommandJoystick controller =
            new CommandJoystick(Ports.Controllers.JOYSTICK);
    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

        try {
            drive = new SwerveParser(
                    new File(Filesystem.getDeployDirectory(), Drive.SWERVE_CONFIG_LOCATION)
            ).createSwerveDrive();
        } catch (IOException ignored) {}

        configureBindings();
    }

    private void configureBindings() {
    }

    @Override
    public void periodic() {
        super.periodic();
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
