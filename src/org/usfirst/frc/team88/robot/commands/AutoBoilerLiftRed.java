package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoBoilerLiftRed extends CommandGroup {

    public AutoBoilerLiftRed() {
		DriverStation ds = DriverStation.getInstance();
		Preferences prefs = Preferences.getInstance();
		boolean redAlliance = ds.getAlliance() == DriverStation.Alliance.Red;
		
		addSequential(new DriveZeroYaw());
		addParallel(new ShooterSetHood(.42));
    	addSequential(new DriveDistance(-6));
//    	addSequential(new DriveDistanceAndCurve(-6.6, -0.405, 2.75));
		addSequential(new DriveScanForGear(-90));
    	addSequential(new Delay(0.3));
		addSequential(new AutoDeliverGear(this));
    	
    	addSequential(new DriveRotateToBoiler());
    	addSequential(new DriveDistance(1.1));
//		addSequential(new DriveToBoiler());
    	addSequential(new ShooterStartFlywheel());
    	addSequential(new Delay(0.3));
    	addSequential(new ShooterStartAgitatorAndFeeder());
    	addSequential(new Delay(4.0));

    	addSequential(new ShooterStopAll());
		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-14.0));
    }
}
