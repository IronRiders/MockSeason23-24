// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.ironriders.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.DriveSubsystem;

public final class AutoOptions {
    private DriveCommands driveCommands;

    public AutoOptions(DriveCommands driveCommands) {
        this.driveCommands = driveCommands;
    }

    public Command getAuto() {
        return driveCommands.pathFindTo(new Pose2d(2, 2, Rotation2d.fromDegrees(180)));
    }
}
