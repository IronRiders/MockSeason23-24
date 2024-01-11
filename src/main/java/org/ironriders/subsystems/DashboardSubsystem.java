package org.ironriders.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.lib.sendable_choosers.EnumSendableChooser;

import static org.ironriders.constants.Auto.AutoOption;
import static org.ironriders.constants.Auto.AutoOption.NONE;

public class DashboardSubsystem extends SubsystemBase {
    EnumSendableChooser<AutoOption> a = new EnumSendableChooser<>(AutoOption.class, "auto/A");
    EnumSendableChooser<AutoOption> b = new EnumSendableChooser<>(AutoOption.class, "auto/B");
    EnumSendableChooser<AutoOption> c = new EnumSendableChooser<>(AutoOption.class, "auto/C");
    EnumSendableChooser<AutoOption> d = new EnumSendableChooser<>(AutoOption.class, "auto/D");

    @Override
    public void periodic() {
        addCompatibleOptions(a.getSelected(), b);
        addCompatibleOptions(b.getSelected(), c);
        addCompatibleOptions(c.getSelected(), d);
    }

    private void addCompatibleOptions(AutoOption lastOption, EnumSendableChooser<AutoOption> field) {
        field.clear();

        for (AutoOption option : AutoOption.values()) {
            if (lastOption == null) break;
            if (lastOption.isCompatible(option)) field.addOption(option.name(), option);
        }
    }

    public Command getAuto() {
        return Commands.sequence(
                buildAuto(a.getSelected()),
                buildAuto(b.getSelected()),
                buildAuto(c.getSelected()),
                buildAuto(d.getSelected())
        );
    }

    public Command buildAuto(AutoOption auto) {
        if (auto.equals(NONE)) return Commands.none();
        return AutoBuilder.buildAuto(auto.name());
    }
}
