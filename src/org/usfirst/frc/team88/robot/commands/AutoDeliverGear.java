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
		addSequential(new DriveDeliverGear(originator != null ? originator : this));
//		addSequential(new GearReceiverOut());
		addSequential(new DriveWiggle());
		addSequential(new GearPusherOut2());
		addSequential(new Delay(0.75));
		addSequential(new DriveDistance(1.5));
		addSequential(new GearPusherIn2());
	}
}
