package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
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
import static org.ironriders.constants.Arm.PID.*;

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

        leader.setIdleMode(kCoast);
        follower.setIdleMode(kCoast);

        primaryEncoder.setPositionOffset(PRIMARY_ENCODER_OFFSET);
        primaryEncoder.setDistancePerRotation(360);
        secondaryEncoder.setPositionOffset(SECONDARY_ENCODER_OFFSET);
        secondaryEncoder.setDistancePerRotation(360);

        leader.getEncoder().setPositionConversionFactor(360.0 / GEARING);
        leader.getEncoder().setPosition(getPrimaryPosition());

        follower.getEncoder().setPositionConversionFactor(360.0 / GEARING);
        follower.getEncoder().setPosition(getPrimaryPosition());

        leader.setSoftLimit(kReverse, Limit.REVERSE);
        leader.enableSoftLimit(kReverse, true);
        leader.setSoftLimit(kForward, Limit.FORWARD);
        leader.enableSoftLimit(kForward, true);
        follower.setSoftLimit(kReverse, Limit.REVERSE);
        follower.enableSoftLimit(kReverse, true);
        follower.setSoftLimit(kForward, Limit.FORWARD);
        follower.enableSoftLimit(kForward, true);

        follower.follow(leader, true);
        resetPID();

        commands = new ArmCommands(this);
    }

    @Override
    public void periodic() {
        if (!Utils.isWithinTolerance(getPrimaryPosition(), getSecondaryPosition(), FAILSAFE_DIFFERENCE)) {
            leader.set(0);
            return;
        }

        SmartDashboard.putNumber("Left Encoder", getSecondaryPosition());
        SmartDashboard.putNumber("Left Encoder Int", follower.getEncoder().getPosition());
        SmartDashboard.putNumber("Right Int Encoder", leader.getEncoder().getPosition());
        SmartDashboard.putNumber("Right Encoder", getPrimaryPosition());
        SmartDashboard.putNumber("Target", pid.getSetpoint().position);

        SmartDashboard.putNumber("pid", pid.calculate(getPosition()));

        leader.set(pid.calculate(getPosition()));
    }

    public ArmCommands getCommands() {
        return commands;
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
        pid.setGoal(target);
    }

    public void resetPID() {
        pid.reset(getPosition());
        pid.setGoal(getPosition());
    }
}
