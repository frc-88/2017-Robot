package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBoilerLift extends CommandGroup {

    public AutoBoilerLift() {
		DriverStation ds = DriverStation.getInstance();
		Preferences prefs = Preferences.getInstance();
		boolean redAlliance = ds.getAlliance() == DriverStation.Alliance.Red;
		
		addSequential(new DriveZeroYaw());
    	addParallel(new ShooterSetHood());
    	addSequential(new DriveDistance(-6));
		addSequential(redAlliance ? new DriveRotateToAngle(-50) : new DriveRotateToAngle(50) );
    	addSequential(new Delay(0.3));
		addSequential(new AutoDeliverGear(this));
    	
    	// addParallel(new ShooterStartFlywheel());
    	addSequential(new DriveDistance(2.0));
    	addSequential(new DriveRotateToBoiler());
    	// addSequential(new ShooterStartAgitatorAndFeeder());
    	addSequential(new Delay(2.0));

    	// addSequential(new ShooterStopAll());
		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-10.0));
    }
}
