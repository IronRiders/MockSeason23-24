package org.ironriders.subsystems.drive;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;
import org.ironriders.lib.Utils;

import static com.revrobotics.CANSparkMax.IdleMode.kBrake;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

public class DriveModuleSubsystem extends SubsystemBase {
    private final CANSparkMax speedMotor;
    private final CANSparkMax directionMotor;
    private final RelativeEncoder distanceEncoder;
    // TODO: Calibrate encoder.
    private final CANCoder directionEncoder;
    private double targetDirection;

    public DriveModuleSubsystem(int speedMotorPort, int directionMotorPort, int directionEncoderPort) {
        speedMotor = new CANSparkMax(speedMotorPort, kBrushless);
        directionMotor = new CANSparkMax(directionMotorPort, kBrushless);

        speedMotor.setSmartCurrentLimit(Drive.NEO_CURRENT_LIMIT);
        directionMotor.setSmartCurrentLimit(Drive.NEO_CURRENT_LIMIT);

        speedMotor.setIdleMode(kBrake);
        directionMotor.setIdleMode(kBrake);

        distanceEncoder = speedMotor.getEncoder();
        directionEncoder = new CANCoder(directionEncoderPort);

        directionEncoder.setPositionToAbsolute();

        targetDirection = getDirection();
    }

    @Override
    public void periodic() {
        directionTick();
    }

    private void directionTick() {
        double error = targetDirection - getDirection();
        if (error > 180) {
            error -= 360;
        } else if (error < -180) {
            error += 360;
        }

        if (Math.abs(error) > 90) {
            error += 180;
        }

        directionMotor.set(error * Drive.DIRECTION_KP);
    }

    /**
     * In full rotations.
     */
    public double getDistance() {
        return distanceEncoder.getPosition() * Drive.Encoders.DISTANCE_GEARING;
    }

    /**
     * In degrees (0 >= x < 360).
     */
    public double getDirection() {
        return Utils.absoluteRotation(directionEncoder.getPosition() * 360);
    }

    public void setSpeed(double power) {
        speedMotor.set(power);
    }

    /**
     * Will always take the fastest way to direct the wheel the desired direction.
     */
    public void setDirection(double direction) {
        targetDirection = direction;
    }
}
