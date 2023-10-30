package org.ironriders.lib;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Small class to wrapper a Thriftybot analog encoder
 */
public class ThriftyEncoder {
    private final double magnetOffset;
    private final AnalogEncoder enc;

    public ThriftyEncoder(int port, double magnetOffset) {
        enc = new AnalogEncoder(new AnalogInput(port));
        this.magnetOffset = Rotation2d.fromDegrees(magnetOffset).getRadians();
    }

    public double getPosition() {
        return new Rotation2d(-enc.getAbsolutePosition() * Math.PI * 2 + magnetOffset).getDegrees();
    }
}