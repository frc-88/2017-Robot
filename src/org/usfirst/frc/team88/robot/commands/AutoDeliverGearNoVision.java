package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoDeliverGearNoVision extends CommandGroup {

	public AutoDeliverGearNoVision() {
		this(null);
	}
	
	public AutoDeliverGearNoVision(CommandGroup originator) {
		addSequential(new DriveDistance());
		addSequential(new GearReceiverOut());
		addSequential(new DriveWiggle());
		addSequential(new GearPusherOut());
		addSequential(new Delay(0.75));
		addSequential(new DriveDistance(1.5));
		addSequential(new GearPusherIn());
	}
}
