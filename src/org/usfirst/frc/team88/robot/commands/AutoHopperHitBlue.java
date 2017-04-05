package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoHopperHitBlue extends CommandGroup {

    public AutoHopperHitBlue() {
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));

		addSequential(new DriveDistance(-6.0));
		
		addSequential(new DriveRotateToAngle(90));
    	addSequential(new Delay(0.1));
		
    	addSequential(new DriveDistance(1.5));

    	addSequential(new Delay(2.0));
    	
		addSequential(new DriveDistance(-1.0));

		addSequential(new DriveRotateToAngle(18.0));
		    	
		addSequential(new AutoShoot(10.0));
    }
}
