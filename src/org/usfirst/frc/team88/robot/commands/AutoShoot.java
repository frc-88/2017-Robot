package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShoot extends CommandGroup {
	private final static double DEFAULT_SHOOT_DELAY = 4.0;
	
	public AutoShoot() {
		this(DEFAULT_SHOOT_DELAY);
	}
	
    public AutoShoot(double shootDelay) {
    	addParallel(new ShooterSetHood(0.42));
    	
		//addSequential(new DriveToBoiler());
    	//addSequential(new Delay(.25));
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new Delay(0.1));

    	addSequential(new ShooterStartFlywheel());
    	addSequential(new Delay(0.2));
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	addSequential(new Delay(shootDelay));

    	addSequential(new ShooterStopAll());    	
    }
}
