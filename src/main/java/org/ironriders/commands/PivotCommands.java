package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.subsystems.PivotSubsystem;

public class PivotCommands {
    private final PivotSubsystem pivot;

    public PivotCommands(PivotSubsystem pivot) {
        this.pivot = pivot;
    }

    public Command setPivot(double target) {
        // Utils.isWithinTolerance() may be useful.
        return null;
    }
}
