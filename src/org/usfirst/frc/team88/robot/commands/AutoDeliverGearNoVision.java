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
		addSequential(new DriveZeroYaw());
		addSequential(new GearReceiverOut());
		addSequential(new DriveDistance(-6.2));
		addSequential(new GearPusherOut());
		addSequential(new Delay(0.75));
		addSequential(new DriveDistance(1.5));
		addSequential(new GearPusherIn());
	}
}
