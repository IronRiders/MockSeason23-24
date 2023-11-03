package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Ports;

public class PivotSubsystem extends SubsystemBase {
    /*

    Pivot Subsystem Group Assigment

    As a group, you will implement a fully working pivot system on a robot.

    One must be able to:
    * Retrieve the position of the motor.
    * Retrieve a command that set target of the motor with a valid end and timeout.

    The code must be:
    * Clean and structured (using conventions I taught you).
    * In the correct classes.

    Resources (try to use your knowledge as much as possible):
    * All other branches of the repo.
    * Online.

    All the files you need have been created for you. There also may be some helpful comments scattered around.

     */

    private final CANSparkMax motor = new CANSparkMax(Ports.Pivot.MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless);

    // The encoder has all the position data, get it by doing: `motor.getEncoder()`.
    // In your periodic, do `SmartDashboard.putNumber("Pivot Pos", GET POSITION METHOD)`.
    // Use the PIDController class. The calculate method returns the motor output. You can set the motor speed by doing
    // motor.set();
}
