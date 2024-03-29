package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ManipulatorCommands;
import org.ironriders.constants.Ports;

import static com.revrobotics.CANSparkBase.IdleMode.kBrake;
import static com.revrobotics.CANSparkLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Manipulator.*;

public class ManipulatorSubsystem extends SubsystemBase {
    private final ManipulatorCommands commands;
    private final CANSparkMax leader = new CANSparkMax(Ports.Manipulator.RIGHT_MOTOR, kBrushless);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax follower = new CANSparkMax(Ports.Manipulator.LEFT_MOTOR, kBrushless);

    public ManipulatorSubsystem() {
        leader.setSmartCurrentLimit(CURRENT_LIMIT);
        follower.setSmartCurrentLimit(CURRENT_LIMIT);
        leader.setIdleMode(kBrake);
        follower.setIdleMode(kBrake);

        follower.follow(leader, true);

        SmartDashboard.putString("manipulator/State", "STOP");

        commands = new ManipulatorCommands(this);
    }

    public ManipulatorCommands getCommands() {
        return commands;
    }

    public void set(State state) {
        switch (state) {
            case GRAB -> grab();
            case EJECT -> eject();
            case STOP -> stop();
        }
    }

    private void grab() {
        SmartDashboard.putString("manipulator/State", "GRAB");
        leader.set(-GRAB_SPEED);
    }

    private void eject() {
        SmartDashboard.putString("manipulator/State", "EJECT");
        leader.set(EJECT_SPEED);
    }

    private void stop() {
        SmartDashboard.putString("manipulator/State", "STOP");
        leader.set(0);
    }
}