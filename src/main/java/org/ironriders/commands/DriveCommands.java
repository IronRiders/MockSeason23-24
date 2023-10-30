package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.drive.DriveSubsystem;

import static org.ironriders.constants.Commands.Drive.SetRotation;

public class DriveCommands {
    private final DriveSubsystem drive;

    public DriveCommands(DriveSubsystem drive) {
        this.drive = drive;
    }

    public Command setRotation(double rotation) {
        return Commands
                .run(() -> drive.getCoordinator().setRotation(rotation), drive)
                .until(() -> Utils.isWithinTolerance(drive.getRotation(), rotation, SetRotation.TOLERANCE))
                .withTimeout(SetRotation.TIMEOUT);
    }
}
