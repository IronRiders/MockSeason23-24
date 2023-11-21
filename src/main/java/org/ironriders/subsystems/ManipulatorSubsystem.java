package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Manipulator;
import org.ironriders.constants.Ports;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

public class ManipulatorSubsystem extends SubsystemBase {
    private final CANSparkMax rightMotor =
            new CANSparkMax(Ports.Manipulator.RIGHT_MOTOR, kBrushless);
    private final CANSparkMax leftMotor =
            new CANSparkMax(Ports.Manipulator.LEFT_MOTOR, kBrushless);

    // TODO: test directions for grab() and eject()
    public void grab() {
        rightMotor.set(Manipulator.GRAB_SPEED);
        leftMotor.set(-Manipulator.GRAB_SPEED);
    }

    public void eject() {
        rightMotor.set(-Manipulator.EJECT_SPEED);
        leftMotor.set(Manipulator.EJECT_SPEED);
    }

    public void stop() {
        rightMotor.set(0);
        leftMotor.set(0);
    }
}