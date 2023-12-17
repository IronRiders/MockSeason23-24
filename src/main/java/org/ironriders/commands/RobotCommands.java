package org.ironriders.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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

        NamedCommands.registerCommand("Resting", resting());
        NamedCommands.registerCommand("Driving", driving());
        NamedCommands.registerCommand("Ground Pickup", groundPickup());
        NamedCommands.registerCommand("Exchange", exchange());
        NamedCommands.registerCommand("Exchange Return", exchangeReturn());
        NamedCommands.registerCommand("Portal", portal());
        NamedCommands.registerCommand("Switch", switchDropOff());
    }

    public Command resting() {
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
                .andThen(manipulator.grabAndStop(3));
    }

    public Command exchange() {
        return Commands.sequence(
                arm.setPivot(Arm.State.EXCHANGE),
                drive.pathFindToTag(vision.bestTagFor(AprilTagLocation.EXCHANGE)),
                manipulator.set(Manipulator.State.EJECT)
        );
    }

    public Command exchangeReturn() {
        return Commands.sequence(
                arm.setPivot(Arm.State.EXCHANGE),
                drive.pathFindToTag(vision.bestTagFor(AprilTagLocation.EXCHANGE)),
                manipulator.set(Manipulator.State.GRAB)
        );
    }

    public Command portal() {
        return Commands.sequence(
                arm.setPivot(Arm.State.PORTAL),
                drive.pathFindToTag(vision.bestTagFor(AprilTagLocation.PORTAL)),
                manipulator.set(Manipulator.State.GRAB)
        );
    }

    public Command switchDropOff() {
        return Commands.sequence(
                arm.setPivot(Arm.State.SWITCH),
                drive.pathFindToTag(1),
                manipulator.set(Manipulator.State.EJECT)
        );
    }

    /**
     * Builds an autonomous command based on the provided {@code AutoConfig}.
     *
     * @param auto The configuration object specifying the autonomous routine.
     * @return A command representing the autonomous routine specified by the {@code AutoConfig}.
     */
    public Command buildAuto(AutoConfig auto) {
        return buildAuto(auto, false);
    }

    public Command buildAuto(AutoConfig auto, boolean resetOdometry) {
        return AutoBuilder.buildAuto(auto.name());
    }
}
