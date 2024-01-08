package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.constants.Arm;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.ArmSubsystem;

import static org.ironriders.constants.Arm.State.*;
import static org.ironriders.constants.Arm.TIMEOUT;
import static org.ironriders.constants.Arm.TOLERANCE;

public class ArmCommands {
    private final ArmSubsystem arm;

    public ArmCommands(ArmSubsystem arm) {
        this.arm = arm;

        NamedCommands.registerCommand("Pivot Rest", setPivot(REST));
        NamedCommands.registerCommand("Pivot Switch", setPivot(SWITCH));
        NamedCommands.registerCommand("Pivot Portal", setPivot(PORTAL));
        NamedCommands.registerCommand("Pivot Full", setPivot(FULL));
    }

    public Command setPivot(Arm.State preset) {
        return setPivot(preset.getPosition());
    }

    public Command setPivot(double target) {
        return arm
                .run(() -> arm.set(target))
                .until(() -> Utils.isWithinTolerance(arm.getPosition(), target, TOLERANCE))
                .finallyDo(arm::resetPID)
                .withTimeout(TIMEOUT);
    }

    public Command reset() {
        return arm.runOnce(arm::resetPID);
    }
}
