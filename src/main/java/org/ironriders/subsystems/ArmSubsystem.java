package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.commands.ArmCommands;
import org.ironriders.constants.Ports;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkMax.IdleMode.kCoast;
import static com.revrobotics.CANSparkMax.SoftLimitDirection.kForward;
import static com.revrobotics.CANSparkMax.SoftLimitDirection.kReverse;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.ironriders.constants.Arm.*;
import static org.ironriders.constants.Arm.PIDFF.*;

public class ArmSubsystem extends SubsystemBase {
    private final ArmCommands commands;
    private final CANSparkMax leader = new CANSparkMax(Ports.Arm.RIGHT_MOTOR, kBrushless);
    @SuppressWarnings("FieldCanBeLocal")
    private final CANSparkMax follower = new CANSparkMax(Ports.Arm.LEFT_MOTOR, kBrushless);
    private final DutyCycleEncoder primaryEncoder = new DutyCycleEncoder(Ports.Arm.PRIMARY_ENCODER);
    private final DutyCycleEncoder secondaryEncoder = new DutyCycleEncoder(Ports.Arm.SECONDARY_ENCODER);
    private final ProfiledPIDController pid = new ProfiledPIDController(P, I, D, PROFILE);

    public ArmSubsystem() {
        leader.setSmartCurrentLimit(CURRENT_LIMIT);
        follower.setSmartCurrentLimit(CURRENT_LIMIT);

        leader.setSoftLimit(kReverse, Limit.REVERSE);
        leader.enableSoftLimit(kReverse, true);
        leader.setSoftLimit(kForward, Limit.FORWARD);
        leader.enableSoftLimit(kForward, true);
        follower.setSoftLimit(kReverse, Limit.REVERSE);
        follower.enableSoftLimit(kReverse, true);
        follower.setSoftLimit(kForward, Limit.FORWARD);
        follower.enableSoftLimit(kForward, true);

        primaryEncoder.setPositionOffset(PRIMARY_ENCODER_OFFSET);
        primaryEncoder.setDistancePerRotation(360);
        secondaryEncoder.setPositionOffset(SECONDARY_ENCODER_OFFSET);
        secondaryEncoder.setDistancePerRotation(360); // try -

        follower.follow(leader);
        pid.reset(getPosition());

        commands = new ArmCommands(this);
    }

    /*
    TODO: Test
    See if we need break mode
     */

    @Override
    public void periodic() {
        leader.getEncoder().setPosition(getPrimaryPosition());
        follower.getEncoder().setPosition(getSecondaryPosition());
        leader.setIdleMode(kCoast);
        follower.setIdleMode(kCoast);

        SmartDashboard.putNumber("Left Encoder", getSecondaryPosition());
        SmartDashboard.putNumber("Right Encoder", getPrimaryPosition());

        SmartDashboard.putNumber("Integrated Left Encoder", follower.getEncoder().getPosition());
        SmartDashboard.putNumber("Integrated Right Encoder", leader.getEncoder().getPosition());

        SmartDashboard.putNumber("pid", MathUtil.clamp(pid.calculate(getPosition()), -1, 1) * SPEED);
        SmartDashboard.putBoolean("at target", Utils.isWithinTolerance(getPosition(), pid.getSetpoint().position, TOLERANCE));

        leader.set(MathUtil.clamp(pid.calculate(getPosition()), -1, 1) * SPEED);
    }

    public ArmCommands getCommands() {
        return commands;
    }

    public void set(State state) {
        set(state.getPosition());
    }

    public double getPrimaryPosition() {
        return primaryEncoder.getDistance() + PRIMARY_ENCODER_OFFSET;
    }

    public double getSecondaryPosition() {
        return -(secondaryEncoder.getDistance() + SECONDARY_ENCODER_OFFSET);
    }

    /**
     * In degrees.
     */
    public double getPosition() {
        return getPrimaryPosition();
    }

    public void set(double target) {
        pid.reset(getPosition());
        pid.setGoal(target);
    }
}
