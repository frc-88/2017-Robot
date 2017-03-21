package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoShoot extends CommandGroup {

    public AutoShoot() {
    	addSequential(new ShooterStartFlywheel());
    	addSequential(new Delay(.1));
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	
    }
}
