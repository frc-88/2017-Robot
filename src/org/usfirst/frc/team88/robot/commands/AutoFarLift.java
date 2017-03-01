package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoFarLift extends CommandGroup {

    public AutoFarLift() {
		DriverStation ds = DriverStation.getInstance();
		Preferences prefs = Preferences.getInstance();
		boolean redAlliance = ds.getAlliance() == DriverStation.Alliance.Red;
		
		addSequential(new DriveZeroYaw());
    	addSequential(new DriveDistance(-6));
		addSequential(redAlliance ? new DriveRotateToAngle(60) : new DriveRotateToAngle(-60) );
    	addSequential(new Delay(0.3));
		addSequential(new AutoDeliverGear(this));
		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-10.0));
    }
}
