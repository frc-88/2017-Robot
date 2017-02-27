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
		boolean redAlliance = prefs.getBoolean("RedAlliance", ds.getAlliance() == DriverStation.Alliance.Red);

    	addSequential(new DriveDistance(-6));
		addSequential(redAlliance ? new DriveRotateToAngle2(30) : new DriveRotateToAngle2(-30) );
    	addSequential(new DriveRotateToAngle(45));
    	addSequential(new AutoDeliverGear());
    }
}
