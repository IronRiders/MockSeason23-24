package org.ironriders.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.subsystems.ManipulatorSubsystem;

public class ManipulatorCommands {
    private final ManipulatorSubsystem manipulator;

    public ManipulatorCommands(ManipulatorSubsystem manipulatorSubsystem) {
        manipulator = manipulatorSubsystem;

        NamedCommands.registerCommand("Grab", grab());
        NamedCommands.registerCommand("Eject", eject());
        NamedCommands.registerCommand("Stop Manipulator", stop());
    }

    public Command set(ManipulatorSubsystem.State state) {
        return Commands.runOnce(() -> manipulator.set(state), manipulator);
    }

    private Command grab() {
        return Commands.runOnce(manipulator::grab, manipulator);
    }

    private Command eject() {
        return Commands.runOnce(manipulator::eject, manipulator);
    }

    private Command stop() {
        return Commands.runOnce(manipulator::stop, manipulator);
    }
}