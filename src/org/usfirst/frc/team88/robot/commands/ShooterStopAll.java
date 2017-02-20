package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterStopAll extends CommandGroup {

	public ShooterStopAll() {
		addSequential(new ShooterStopAgitator());
		addSequential(new ShooterStopFeeder());
		addSequential(new ShooterStopFlywheel());
	}
}
