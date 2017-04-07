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
		addParallel(new GearReceiverOut());
		addSequential(new DriveDeliverGear(originator != null ? originator : this));
		//addSequential(new DriveWiggle());
		addSequential(new GearPusherOut());
		addSequential(new Delay(0.75));
		addSequential(new DriveDistanceSlow(3.5));
		addSequential(new GearPusherIn());
	}
}
