package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBoilerLiftBlue extends CommandGroup {

    public AutoBoilerLiftBlue() {
		DriverStation ds = DriverStation.getInstance();
		Preferences prefs = Preferences.getInstance();
		
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));
    	addSequential(new DriveDistance(-6));
//    	addSequential(new DriveDistanceAndCurve(-6.8, 0.405, 2.95));
		addSequential(new DriveRotateToAngle(60));
    	addSequential(new Delay(0.3));
		addSequential(new AutoDeliverGear(this));
    	
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new ShooterStartFlywheel());
    	addSequential(new Delay(0.3));
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	addSequential(new Delay(4.0));

    	addSequential(new ShooterStopAll());
		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-14.0));
    }
}
