package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterStartAgitatorAndFeeder extends CommandGroup {

    public ShooterStartAgitatorAndFeeder() {
        addSequential(new ShooterStartAgitator());
        addSequential(new ShooterStartFeeder());
    }
}
