package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoDeliverGear extends CommandGroup {

	public AutoDeliverGear() {
		addSequential(new DriveDeliverGear());
		addSequential(new GearReceiverOut());
		
		addSequential(new GearPusherOut());
		addSequential(new Delay(0.75));
		addSequential(new DriveDistance(1.5));
		addSequential(new GearPusherIn());
	}
}
