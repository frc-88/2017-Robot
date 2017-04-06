package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoHopperHitRed extends CommandGroup {

    public AutoHopperHitRed() {
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));
		addParallel(new FuelFlapOut());

		addSequential(new DriveDistance(-6));
		
		addSequential(new DriveRotateToAngle(-90));
    	addSequential(new Delay(0.1));
		
    	addSequential(new DriveDistance(2));

    	addSequential(new Delay(2.0));
    	
		addSequential(new DriveDistance(-1.0));

		addSequential(new DriveRotateToAngle(-18.0));
		    	
		addSequential(new AutoShoot(10.0));
    }
}
