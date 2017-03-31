package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBoilerLiftBlue extends CommandGroup {

	public AutoBoilerLiftBlue() {
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));
		addSequential(new DriveDistance(-6));
		addSequential(new DriveScanForGear(90));
		addSequential(new Delay(0.1));

		addSequential(new AutoDeliverGear(this));

		addSequential(new AutoShoot());
		
		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-14.0));
	}
}
