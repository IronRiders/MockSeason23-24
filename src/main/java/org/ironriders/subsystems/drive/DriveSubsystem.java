package org.ironriders.subsystems.drive;

import com.ctre.phoenix.sensors.Pigeon2;

import static org.ironriders.constants.Ports.Drive.*;

public class DriveSubsystem {
    private final DriveModuleSubsystem frontRightModule =
            new DriveModuleSubsystem(FR.SPEED_MOTOR, FR.DIRECTION_MOTOR, FR.DIRECTION_ENCODER);
    private final DriveModuleSubsystem frontLeftModule =
            new DriveModuleSubsystem(FL.SPEED_MOTOR, FL.DIRECTION_MOTOR, FL.DIRECTION_ENCODER);
    private final DriveModuleSubsystem backRightModule =
            new DriveModuleSubsystem(BR.SPEED_MOTOR, BR.DIRECTION_MOTOR, BR.DIRECTION_ENCODER);
    private final DriveModuleSubsystem backLeftModule =
            new DriveModuleSubsystem(BL.SPEED_MOTOR, BL.DIRECTION_MOTOR, BL.DIRECTION_ENCODER);
    private final Pigeon2 gyro = new Pigeon2(GYRO);

    private final DriveCoordinatorSubsystem coordinator =
            new DriveCoordinatorSubsystem(frontRightModule, frontLeftModule, backRightModule, backLeftModule, gyro);

    public DriveSubsystem() {
        backRightModule.setDirection(0);
    }
}
