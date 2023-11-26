package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Manipulator;
import org.ironriders.subsystems.ManipulatorSubsystem;

import static org.ironriders.constants.Manipulator.State.*;

public class ManipulatorCommands {
    private final ManipulatorSubsystem manipulator;

    public ManipulatorCommands(ManipulatorSubsystem manipulatorSubsystem) {
        manipulator = manipulatorSubsystem;

        NamedCommands.registerCommand("Manipulator Grab", grab());
        NamedCommands.registerCommand("Manipulator Eject", eject());
        NamedCommands.registerCommand("Manipulator Stop", stop());
    }

    public Command set(Manipulator.State state) {
        return Commands.runOnce(() -> manipulator.set(state), manipulator);
    }

    private Command grab() {
        return Commands.runOnce(() -> set(GRAB), manipulator);
    }

    private Command eject() {
        return Commands.runOnce(() -> set(EJECT), manipulator);
    }

    private Command stop() {
        return Commands.runOnce(() -> set(STOP), manipulator);
    }
}