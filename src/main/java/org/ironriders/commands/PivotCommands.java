package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.PivotSubsystem;

import static org.ironriders.constants.Commands.Pivot.TIMEOUT;
import static org.ironriders.constants.Pivot.TOLERANCE;

public class PivotCommands {
    private final PivotSubsystem pivot;

    public PivotCommands(PivotSubsystem pivot) {
        this.pivot = pivot;
    }

    public PivotCommands registerNamedCommands() {
        return this;
    }

    public Command setPivot(double target) {
        return Commands
                .run(() -> pivot.setTarget(target))
                .until(() -> Utils.isWithinTolerance(pivot.getPosition(), target, TOLERANCE))
                .withTimeout(TIMEOUT);
    }
}
