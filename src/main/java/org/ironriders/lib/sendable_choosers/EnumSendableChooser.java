package org.ironriders.lib.sendable_choosers;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EnumSendableChooser<E extends Enum<?>> extends CustomSendableChooser<E> {
    public EnumSendableChooser(Class<E> enumType, String name) {
        E[] options = enumType.getEnumConstants();

        for (E option : options) {
            addOption(option.name(), option);
        }

        SmartDashboard.putData(name, this);
    }

    public EnumSendableChooser(Class<E> enumType, E defaultOption, String name) {
        E[] options = enumType.getEnumConstants();

        for (E option : options) {
            addOption(option.name(), option);
        }
        setDefaultOption(defaultOption.name(), defaultOption);

        SmartDashboard.putData(name, this);
    }
}
