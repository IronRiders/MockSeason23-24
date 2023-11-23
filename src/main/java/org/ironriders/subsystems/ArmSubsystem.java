package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Ports;

import static com.revrobotics.CANSparkMax.IdleMode.kBrake;
import static com.revrobotics.CANSparkMax.SoftLimitDirection.kForward;
import static com.revrobotics.CANSparkMax.SoftLimitDirection.kReverse;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Arm.*;
import static org.ironriders.constants.Arm.PIDFF.*;

public class ArmSubsystem extends SubsystemBase {
    private final CANSparkMax leader = new CANSparkMax(Ports.Arm.RIGHT_MOTOR, kBrushless);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax follower = new CANSparkMax(Ports.Arm.LEFT_MOTOR, kBrushless);
    private final DutyCycleEncoder encoder = new DutyCycleEncoder(Ports.Arm.ENCODER);
    private final PIDController pid = new PIDController(P, I, D);

    public ArmSubsystem() {
        leader.setSmartCurrentLimit(CURRENT_LIMIT);
        follower.setSmartCurrentLimit(CURRENT_LIMIT);
        leader.setIdleMode(kBrake);
        follower.setIdleMode(kBrake);

        leader.getEncoder().setPosition(getPosition());
        follower.getEncoder().setPosition(getPosition());
        leader.getEncoder().setPositionConversionFactor(GEARING * 360);
        follower.getEncoder().setPositionConversionFactor(GEARING * 360);

        leader.setSoftLimit(kReverse, Limit.REVERSE);
        leader.enableSoftLimit(kReverse, true);
        leader.setSoftLimit(kForward, Limit.FORWARD);
        leader.enableSoftLimit(kForward, true);
        follower.setSoftLimit(kReverse, Limit.REVERSE);
        follower.enableSoftLimit(kReverse, true);
        follower.setSoftLimit(kForward, Limit.FORWARD);
        follower.enableSoftLimit(kForward, true);

        encoder.setPositionOffset(ENCODER_OFFSET);
        encoder.setDistancePerRotation(360);

        pid.setSetpoint(getPosition());
    }

    @Override
    public void periodic() {
        leader.set(MathUtil.clamp(pid.calculate(getPosition()), -1, 1) * SPEED);
    }

    public void set(State state) {
        set(state.getPosition());
    }

    /**
     * In degrees.
     */
    public double getPosition() {
        return encoder.getAbsolutePosition();
    }

    public void set(double target) {
        pid.setSetpoint(target);
    }

    public enum State {
        REST(0),
        SWITCH(0),
        PORTAL(0),
        PORTAL_RETURN(0),
        PLAYER_STATION(0),
        FULL(0);

        final int position;

        State(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }
}
