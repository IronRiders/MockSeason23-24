package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Ports;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Arm.PIDFF.P;
import static org.ironriders.constants.Arm.SPEED;

public class ArmSubsystem extends SubsystemBase {
    private final CANSparkMax rightMotor = new CANSparkMax(Ports.Arm.RIGHT_MOTOR, kBrushless);
    private final CANSparkMax leftMotor = new CANSparkMax(Ports.Arm.LEFT_MOTOR, kBrushless);

    private final PIDController pid = new PIDController(P, 0, 0);

    @Override
    public void periodic() {
        double speed = MathUtil.clamp(pid.calculate(getPosition()), -1, 1) * SPEED;
        rightMotor.set(speed);
        leftMotor.set(speed);
    }

    public void set(double target) {
        pid.setSetpoint(target);
    }

    public double getPosition() {
        return rightMotor.getEncoder().getPosition();
    }
}
