package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Manipulator;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Manipulator.EJECT_TIMEOUT;
import static org.ironriders.constants.Manipulator.State.*;

public class ManipulatorCommands {
    private final ManipulatorSubsystem manipulator;

    public ManipulatorCommands(ManipulatorSubsystem manipulatorSubsystem) {
        manipulator = manipulatorSubsystem;

        NamedCommands.registerCommand("Manipulator Grab", set(GRAB));
        NamedCommands.registerCommand("Manipulator Eject", set(EJECT));
        NamedCommands.registerCommand("Manipulator Stop", set(STOP));
    }

    public Command set(Manipulator.State state) {
        Command command = manipulator.runOnce(() -> manipulator.set(state));
        if (state.equals(EJECT)) {
            command = command.andThen(waitThenStop(EJECT_TIMEOUT));
        }

        return command.finallyDo(() -> manipulator.set(STOP));
    }

    public Command grabAndStop(double duration) {
        return set(GRAB).andThen(waitThenStop(duration));
    }

    public Command waitThenStop(double duration) {
        return Commands
                .waitSeconds(duration)
                .andThen(() -> manipulator.set(STOP), manipulator);
    }
}