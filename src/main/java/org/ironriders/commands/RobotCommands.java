package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import org.ironriders.constants.Arm;
import org.ironriders.constants.Manipulator;
import org.ironriders.lib.AutoConfig;
import org.ironriders.subsystems.ArmSubsystem;
import org.ironriders.subsystems.DriveSubsystem;
import org.ironriders.subsystems.ManipulatorSubsystem;
import org.ironriders.subsystems.VisionSubsystem;

import static org.ironriders.constants.Game.Field.AprilTagLocation;

public class RobotCommands {
    private final ArmCommands arm;
    private final DriveCommands drive;
    private final VisionSubsystem vision;
    private final ManipulatorCommands manipulator;

    public RobotCommands(ArmSubsystem arm, DriveSubsystem drive, ManipulatorSubsystem manipulator) {
        this.arm = arm.getCommands();
        this.drive = drive.getCommands();
        vision = drive.getVision();
        this.manipulator = manipulator.getCommands();

        NamedCommands.registerCommand("Resting", Resting());
        NamedCommands.registerCommand("Driving", driving());
        NamedCommands.registerCommand("Ground Pickup", groundPickup());
        NamedCommands.registerCommand("Exchange", exchange());
        NamedCommands.registerCommand("Exchange Return", exchangeReturn());
        NamedCommands.registerCommand("Portal", portal());
        NamedCommands.registerCommand("Switch", switchDropOff());
    }

    public Command Resting() {
        return arm
                .setPivot(Arm.State.REST)
                .alongWith(manipulator.set(Manipulator.State.STOP));
    }

    public Command driving() {
        return arm
                .setPivot(Arm.State.FULL)
                .andThen(manipulator.set(Manipulator.State.STOP));
    }

    public Command groundPickup() {
        return driving()
                .andThen(manipulator.set(Manipulator.State.GRAB));
    }

    public Command exchange() {
        return drive
                .pathFindToTag(vision.bestTagFor(AprilTagLocation.EXCHANGE))
                .alongWith(arm.setPivot(Arm.State.EXCHANGE))
                .andThen(manipulator.set(Manipulator.State.EJECT));
    }

    public Command exchangeReturn() {
        return drive
                .pathFindToTag(vision.bestTagFor(AprilTagLocation.EXCHANGE))
                .alongWith(arm.setPivot(Arm.State.EXCHANGE_RETURN))
                .andThen(manipulator.set(Manipulator.State.GRAB));
    }

    public Command portal() {
        return drive
                .pathFindToTag(vision.bestTagFor(AprilTagLocation.PORTAL))
                .alongWith(arm.setPivot(Arm.State.PORTAL))
                .andThen(manipulator.set(Manipulator.State.GRAB));
    }

    public Command switchDropOff() {
        return drive
                .pathFindToTag(vision.bestTagFor(AprilTagLocation.SWITCH))
                .alongWith(arm.setPivot(Arm.State.SWITCH))
                .andThen(manipulator.set(Manipulator.State.EJECT));
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
