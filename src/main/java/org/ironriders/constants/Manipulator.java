package org.ironriders.constants;

public class Manipulator {
    public static final int CURRENT_LIMIT = 20;
    public static final double GRAB_SPEED = 0.6;
    public static final double EJECT_SPEED = 1;
    public static final double EJECT_TIMEOUT = 0.7;

    public enum State {
        GRAB,
        EJECT,
        STOP
    }
}