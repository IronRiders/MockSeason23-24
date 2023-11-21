package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.lib.AutoConfig;

public class RobotCommands {
    private final ArmCommands arm;
    private final DriveCommands drive;
    private final ManipulatorCommands manipulator;

    public RobotCommands(ArmCommands arm, DriveCommands drive, ManipulatorCommands manipulator) {
        this.arm = arm;
        this.drive = drive;
        this.manipulator = manipulator;
    }

    // TODO: Make commands (and named commands too) with software on Wednesday.

    /**
     * Builds an autonomous command based on the provided {@code AutoConfig}.
     *
     * @param auto The configuration object specifying the autonomous routine.
     * @return A command representing the autonomous routine specified by the {@code AutoConfig}.
     */
    public Command buildAuto(AutoConfig auto) {
        return AutoBuilder.buildAuto(auto.name());
    }
}
