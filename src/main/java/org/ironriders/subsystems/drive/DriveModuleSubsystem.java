package org.ironriders.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;
import org.ironriders.lib.ThriftyEncoder;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkMax.IdleMode.kBrake;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

public class DriveModuleSubsystem extends SubsystemBase {
    private final CANSparkMax speedMotor;
    private final CANSparkMax directionMotor;
    private final RelativeEncoder distanceEncoder;
    private final ThriftyEncoder directionEncoder;
    private double targetDirection;

    private double speedMultiplier = 1;

    public DriveModuleSubsystem(int speedMotorPort, int directionMotorPort, int directionEncoderPort,
                                double magnetOffset) {
        speedMotor = new CANSparkMax(speedMotorPort, kBrushless);
        directionMotor = new CANSparkMax(directionMotorPort, kBrushless);

        speedMotor.setSmartCurrentLimit(Drive.NEO_CURRENT_LIMIT);
        directionMotor.setSmartCurrentLimit(Drive.NEO_CURRENT_LIMIT);

        speedMotor.setIdleMode(kBrake);
        directionMotor.setIdleMode(kBrake);

        distanceEncoder = speedMotor.getEncoder();
        directionEncoder = new ThriftyEncoder(directionEncoderPort, magnetOffset);

        targetDirection = getDirection();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("encoder", directionEncoder.getPosition());

        directionSetpointTick(targetDirection);
    }

    private void directionSetpointTick(double target) {
        double error = Utils.rotationalError(target, getDirection());

        if (Math.abs(error) > 90) {
            directionMotor.set(Utils.rotationalError(target + 180, getDirection()) * Drive.DIRECTION_KP);
            speedMultiplier *= -Utils.sign(speedMultiplier);
            return;
        }

        directionMotor.set(error * Drive.DIRECTION_KP);
        speedMultiplier *= Utils.sign(speedMultiplier);
    }

    /**
     * In full rotations.
     */
    public double getDistance() {
        return distanceEncoder.getPosition() / Drive.Encoders.DISTANCE_GEARING;
    }

    /**
     * In degrees with range of [0, 360).
     */
    public double getDirection() {
        return directionEncoder.getPosition();
    }

    public void setSpeed(double power) {
        speedMotor.set(power * speedMultiplier);
    }

    /**
     * Will always take the fastest way to direct the wheel the desired direction.
     */
    public void setDirection(double direction) {
        targetDirection = direction;
    }

    public void stop() {
        setSpeed(0);
        setDirection(getDirection());
    }
}
