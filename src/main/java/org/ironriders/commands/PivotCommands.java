package org.ironriders.commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.lib.Utils;
import org.ironriders.subsystems.PivotSubsystem;

import static org.ironriders.constants.Commands.Pivot.*;
import static org.ironriders.constants.Pivot.*;
import static org.ironriders.robot.RobotContainer.*;

public class PivotCommands {
    private final PivotSubsystem pivot;

    public PivotCommands(PivotSubsystem pivot) {
        this.pivot = pivot;
    }

    public Command setPivot(double target) {
        return Commands
                .run(() -> pivot.setTarget(target))
                .until(() -> !Utils.isWithinTolerance(pivot.getMotorPos(), target, TOLERANCE))
                .withTimeout(TIMEOUT);
    }
    public Command setPivotTwist() {
        return Commands
                .run(() -> pivot.setTarget(joystick.getTwist()*360))
                .until(() -> !Utils.isWithinTolerance(pivot.getMotorPos(), joystick.getTwist(), TOLERANCE))
                .withTimeout(TIMEOUT);
    }
}
