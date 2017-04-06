package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBoilerLiftBlue extends CommandGroup {

	public AutoBoilerLiftBlue() {
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));
		
		// I change the distance below to -5.5 or blue boiler side to compensate for camera location
		// this should not match red boiler side...camera is skewed to a different side
		//     - Mr. Nick
    	addSequential(new DriveDistance(-5.5));
    	
		addSequential(new DriveScanForGear(90));
    	addSequential(new Delay(0.1));
		
    	addSequential(new AutoDeliverGear(this));

    	addSequential(new Delay(0.2));
    	
		addSequential(new AutoShoot());

		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-14.0));
	}
}
