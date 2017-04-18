package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoDeliverGear extends CommandGroup {

	public AutoDeliverGear() {
		this(null);
	}
	
	public AutoDeliverGear(CommandGroup originator) {
		addParallel(new FuelFlapOut());
		addSequential(new DriveDeliverGear(originator != null ? originator : this));
		// DO NOT REMOVE THE FOLLOWING DELAY!!!
		addSequential(new Delay(0.1));
		addSequential(new GearPanReleaseGear());
		addParallel(new Delay(0.5));
		addSequential(new GearPanReset());
		addSequential(new DriveDistanceSlow(3.5));
	}
}
