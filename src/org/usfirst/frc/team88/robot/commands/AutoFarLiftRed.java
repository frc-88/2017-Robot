package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoFarLiftRed extends CommandGroup {

	public AutoFarLiftRed() {
		addSequential(new DriveZeroYaw());
    	//addSequential(new DriveDistanceAndCurve(-6.6, 0.405, 2.75));
		addSequential(new DriveDistance(-6));
		//addSequential(new DriveScanForGear(90));
		addSequential(new DriveRotateToAngle(60));
		addSequential(new Delay(0.1));

		addSequential(new AutoDeliverGear(this));

		addSequential(new DriveRotateToAngle(0));
		addSequential(new DriveDistance(-18.0));
	}
}
