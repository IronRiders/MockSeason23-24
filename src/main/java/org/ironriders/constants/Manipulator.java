package org.ironriders.constants;

public class Manipulator {
    public enum State {
        GRAB,
        EJECT,
        STOP
    }

    public static final int CURRENT_LIMIT = 40;
    public static final double GRAB_SPEED = 0.5;
    public static final double EJECT_SPEED = 0.5;
}