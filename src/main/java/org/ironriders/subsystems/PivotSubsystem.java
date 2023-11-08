package org.ironriders.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Ports;
import static org.ironriders.constants.Pivot.*;

public class PivotSubsystem extends SubsystemBase {
    /*
    Pivot Subsystem Group Assignment

    Team Name: la programmation

    You will be split up into two groups, whichever team completes this first and meets all requirements, will receive
    a half a bag of Jolly Ranchers. As a group, you will implement a fully working pivot system on a robot.

    One must be able to:
    * Retrieve the position of the motor.
    * Retrieve a command that sets target of the motor with a valid end and timeout.

    The code must be:
    * Clean and structured (using conventions I taught you).
    * In the correct classes.

    Resources (try to use your knowledge as much as possible):
    * All other branches of this repo.
    * Online.

    You are not allowed to:
    * Use any form of AI.
    * Previous years repos.

    All the files you need have been created for you. Assume all port values are correct. There also may be some
    helpful comments scattered around.

    I will be testing your code on our test board. You have 45 minutes.
     */

    private final CANSparkMax motor = new CANSparkMax(Ports.Pivot.MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final PIDController pid = new PIDController(P,0,0);

    public PivotSubsystem() {
        motor.setSmartCurrentLimit(40);
    }

    @Override
    public void periodic() {
        motor.set(pid.calculate(getMotorPos()));
        SmartDashboard.putNumber("Pivot Pos", getMotorPos());

    }
    public double getMotorPos() {
        return motor.getEncoder().getPosition();
    }
    public void setTarget(double target) {
        pid.setSetpoint(target);
    }

    // The encoder has all the position data, get it by doing: `motor.getEncoder()`.
    // In your periodic, do `SmartDashboard.putNumber("Pivot Pos", GET POSITION METHOD)`.
    // Use the PIDController class. The calculate method returns the motor output. You can set the motor speed by doing
    // motor.set();
}
