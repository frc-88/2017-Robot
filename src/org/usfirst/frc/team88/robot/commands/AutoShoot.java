package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShoot extends CommandGroup {

    public AutoShoot() {
    	addSequential(new ShooterSetHood(0.42));
    	
		addSequential(new DriveToBoiler());
    	addSequential(new Delay(0.1));
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new Delay(0.1));

    	addSequential(new ShooterStartFlywheel());
    	addSequential(new Delay(0.3));
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	addSequential(new Delay(4.0));

    	addSequential(new ShooterStopAll());    	
    }
}
