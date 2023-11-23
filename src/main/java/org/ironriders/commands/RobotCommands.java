package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.lib.AutoConfig;
import org.ironriders.subsystems.ArmSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;

public class RobotCommands {
    private final ArmCommands arm;
    private final DriveCommands drive;
    private final ManipulatorCommands manipulator;

    public RobotCommands(ArmCommands arm, DriveCommands drive, ManipulatorCommands manipulator) {
        this.arm = arm;
        this.drive = drive;
        this.manipulator = manipulator;
    }

    public Command reset() {
        return arm.setPivot(ArmSubsystem.State.REST)
                .alongWith(manipulator.set(ManipulatorSubsystem.State.STOP));
    }

    public Command driving() {
        return arm.setPivot(ArmSubsystem.State.FULL)
                .andThen(manipulator.set(ManipulatorSubsystem.State.STOP));
    }

    public Command depositToSwitch() {
        return drive.pathFindToTag(1, new Transform2d())
                .alongWith(arm.setPivot(ArmSubsystem.State.SWITCH))
                .andThen(manipulator.set(ManipulatorSubsystem.State.EJECT));
    }

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
