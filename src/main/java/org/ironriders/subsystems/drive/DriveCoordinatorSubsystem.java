package org.ironriders.subsystems.drive;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;

import java.util.Optional;

public class DriveCoordinatorSubsystem extends SubsystemBase {
    private final DriveModuleSubsystem frontRightModule;
    private final DriveModuleSubsystem frontLeftModule;
    private final DriveModuleSubsystem backRightModule;
    private final DriveModuleSubsystem backLeftModule;
    private final Pigeon2 gyro;

    private Optional<Double> targetRotation = Optional.empty();

    public DriveCoordinatorSubsystem(DriveModuleSubsystem fr, DriveModuleSubsystem fl, DriveModuleSubsystem br,
                                     DriveModuleSubsystem bl, Pigeon2 gyro) {
        frontRightModule = fr;
        frontLeftModule = fl;
        backRightModule = br;
        backLeftModule = bl;
        this.gyro = gyro;
    }

    @Override
    public void periodic() {
        rotationTick();
    }

    private void rotationTick() {
        if (targetRotation.isEmpty()) { return; }

        double error = targetRotation.get() - gyro.getYaw();
        if (error > 180) {
            error -= 360;
        } else if (error < -180) {
            error += 360;
        }

        inplaceTurn(error * Drive.ROTATION_KP);
    }

    public void translate(double direction, double power) {
        targetRotation = Optional.empty();

        frontRightModule.setDirection(direction);
        frontLeftModule.setDirection(direction);
        backRightModule.setDirection(direction);
        backLeftModule.setDirection(direction);

        frontRightModule.setSpeed(power);
        frontLeftModule.setSpeed(power);
        backRightModule.setSpeed(power);
        backLeftModule.setSpeed(power);
    }

    public void inplaceTurn(double power) {
        targetRotation = Optional.empty();

        frontRightModule.setDirection(-45.0);
        frontLeftModule.setDirection(135.0);
        backRightModule.setDirection(-135.0);
        backLeftModule.setDirection(45.0);

        frontRightModule.setSpeed(power);
        frontLeftModule.setSpeed(power);
        backRightModule.setSpeed(power);
        backLeftModule.setSpeed(power);
    }

    public void setRotation(double rotation) {
        targetRotation = Optional.of(rotation);
    }

    /**
     * Points all wheels towards the center of the robot.
     */
    public void lockPosition() {

    }
}
