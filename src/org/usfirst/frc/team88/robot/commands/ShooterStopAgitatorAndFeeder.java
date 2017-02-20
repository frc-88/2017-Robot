package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterStopAgitatorAndFeeder extends CommandGroup {

    public ShooterStopAgitatorAndFeeder() {
        addSequential(new ShooterStopAgitator());
        addSequential(new ShooterStopFeeder());
    }
}
