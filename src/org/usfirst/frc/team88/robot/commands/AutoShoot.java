package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShoot extends CommandGroup {

    public AutoShoot() {
    	addSequential(new ShooterSetHood(0.4));
    	addSequential(new ShooterStartFlywheel());
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new Delay(0.05));
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	
    }
}
