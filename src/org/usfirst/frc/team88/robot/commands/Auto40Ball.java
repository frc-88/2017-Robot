package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Auto40Ball extends CommandGroup {

	public Auto40Ball() {
		DriverStation ds = DriverStation.getInstance();
		
//		addParallel(new PlaySound("hopper"));
		addSequential(new DriveDistance(5.8));

//		addSequential(new DriveTurnRight90());
		addSequential(new DriveTurnLeft90());

		/*
		if (ds.getAlliance() == DriverStation.Alliance.Red) {
			addSequential(new DriveTurnRight90());
		} else {
			addSequential(new DriveTurnLeft90());
		}
		*/
		
		addSequential(new DriveDistance(2.2));
		/*
		//addSequential(new DriveDistanceArc(4.2));
		
		addSequential(new Delay(0.5));
		addSequential(new DriveDistance(-1));
		
		addParallel(new ShooterStartFlywheel());
		
		addSequential(new DriveTurnRight90());
		addSequential(new DriveRotateToTarget());
		addSequential(new DriveRotateToTarget());

		addParallel(new ShooterStartFeeder());
		addParallel(new AgitatorStart());
		*/
	}
}
