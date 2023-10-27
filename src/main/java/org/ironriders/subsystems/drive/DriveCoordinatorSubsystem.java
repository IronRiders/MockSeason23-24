package org.ironriders.subsystems.drive;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Drive;
import org.ironriders.lib.Utils;

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
        rotationSetpointTick();
    }

    private void rotationSetpointTick() {
        if (targetRotation.isEmpty()) { return; }
        double power = Utils.calculateRotationalError(targetRotation.get(), gyro.getYaw()) * Drive.ROTATION_KP;

        frontRightModule.setDirection(-45);
        frontLeftModule.setDirection(135);
        backRightModule.setDirection(-135);
        backLeftModule.setDirection(45);

        frontRightModule.setSpeed(power);
        frontLeftModule.setSpeed(power);
        backRightModule.setSpeed(power);
        backLeftModule.setSpeed(power);
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

    public void setRotation(double rotation) {
        targetRotation = Optional.of(rotation);
    }

    /**
     * Points all wheels towards the center of the robot.
     */
    public void lockPosition() {
        targetRotation = Optional.empty();

        frontRightModule.setDirection(-135);
        frontLeftModule.setDirection(45);
        backRightModule.setDirection(-45);
        backLeftModule.setDirection(135);
    }
}
