package org.ironriders.subsystems.drive;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.ironriders.constants.Ports.Drive.*;

public class DriveSubsystem extends SubsystemBase {
    private final DriveModuleSubsystem frontRightModule = new DriveModuleSubsystem(
            FR.SPEED_MOTOR,
            FR.DIRECTION_MOTOR,
            FR.DIRECTION_ENCODER,
            FR.DIRECTION_ENCODER_OFFSET
    );
    private final DriveModuleSubsystem frontLeftModule = new DriveModuleSubsystem(
            FL.SPEED_MOTOR,
            FL.DIRECTION_MOTOR,
            FL.DIRECTION_ENCODER,
            FL.DIRECTION_ENCODER_OFFSET
    );
    private final DriveModuleSubsystem backRightModule = new DriveModuleSubsystem(
            BR.SPEED_MOTOR,
            BR.DIRECTION_MOTOR,
            BR.DIRECTION_ENCODER,
            BR.DIRECTION_ENCODER_OFFSET
    );
    private final DriveModuleSubsystem backLeftModule = new DriveModuleSubsystem(
            BL.SPEED_MOTOR,
            BL.DIRECTION_MOTOR,
            BL.DIRECTION_ENCODER,
            BL.DIRECTION_ENCODER_OFFSET
    );
    private final Pigeon2 gyro = new Pigeon2(GYRO);

    private final DriveCoordinatorSubsystem coordinator =
            new DriveCoordinatorSubsystem(frontRightModule, frontLeftModule, backRightModule, backLeftModule, gyro);

    public DriveSubsystem() {
        backRightModule.setDirection(0);
    }

    public DriveCoordinatorSubsystem getCoordinator() {
        return coordinator;
    }

    public double getRotation() {
        return gyro.getYaw();
    }

    public void stop() {
        frontRightModule.stop();
        frontLeftModule.stop();
        backRightModule.stop();
        backLeftModule.stop();
    }
}
