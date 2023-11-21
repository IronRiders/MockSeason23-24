package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Ports;

import static com.revrobotics.CANSparkMax.IdleMode.kBrake;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Manipulator.*;

public class ManipulatorSubsystem extends SubsystemBase {
    private final CANSparkMax leader = new CANSparkMax(Ports.Manipulator.RIGHT_MOTOR, kBrushless);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax follower = new CANSparkMax(Ports.Manipulator.LEFT_MOTOR, kBrushless);
    public ManipulatorSubsystem() {
        leader.setSmartCurrentLimit(CURRENT_LIMIT);
        follower.setSmartCurrentLimit(CURRENT_LIMIT);
        leader.setIdleMode(kBrake);
        follower.setIdleMode(kBrake);

        follower.follow(leader, true);
    }

    public void set(State state) {
        switch (state) {
            case GRAB -> grab();
            case EJECT -> eject();
            case STOP -> stop();
        }
    }

    public void grab() {
        leader.set(GRAB_SPEED);
    }

    public void eject() {
        leader.set(-EJECT_SPEED);
    }

    public void stop() {
        leader.set(0);
    }

    public enum State {
        GRAB,
        EJECT,
        STOP
    }
}