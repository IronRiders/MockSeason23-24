package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.ArmSubsystem;

import static org.ironriders.constants.Arm.TOLERANCE;
import static org.ironriders.constants.Presets.Arm.*;

public class ArmCommands {
    private final ArmSubsystem arm;

    public ArmCommands(ArmSubsystem arm) {
        this.arm = arm;

        NamedCommands.registerCommand("Rest Pivot", setPivot(REST));
        NamedCommands.registerCommand("Switch Pivot", setPivot(SWITCH));
        NamedCommands.registerCommand("Substation Pivot", setPivot(SUBSTATION));
        NamedCommands.registerCommand("Full Pivot", setPivot(FULL));
    }

    public Command setPivot(double target) {
        return Commands
                .runOnce(() -> arm.set(target))
                .until(() -> Utils.isWithinTolerance(arm.getPosition(), target, TOLERANCE));
    }
}
