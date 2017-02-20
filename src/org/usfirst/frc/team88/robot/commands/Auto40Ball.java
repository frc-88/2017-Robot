package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Auto40Ball extends CommandGroup {

	public Auto40Ball() {
		DriverStation ds = DriverStation.getInstance();
		Preferences prefs = Preferences.getInstance();
		
		String version = prefs.getString("40 Ball Version", "Alpha");
		Boolean redAlliance = ds.getAlliance() == DriverStation.Alliance.Red;
		
		double hopperDelay = prefs.getDouble("Hopper Delay", 0.0);
		
		double alphaDistanceA = prefs.getDouble("Alpha Distance A", 0.0);
		double alphaDistanceB = prefs.getDouble("Alpha Distance B", 0.0);
		double alphaDistanceC = prefs.getDouble("Alpha Distance C", 0.0);
		
		double betaDistanceA = prefs.getDouble("Beta Distance A", 0.0);
		double betaDistanceB = prefs.getDouble("Beta Distance B", 0.0);
		double betaCurveB = prefs.getDouble("Beta Curve B", 0.0);
		double betaDistanceC = prefs.getDouble("Beta Distance C", 0.0);
		
		addParallel(new GearReceiverOut());
		addParallel(new PlaySound("hopper"));
		
		switch(version){
		case "Alpha":
			addSequential(new DriveDistance(alphaDistanceA));
			
			addSequential(redAlliance ? new DriveTurnRight90() : new DriveTurnLeft90() );
			
			addSequential(new DriveDistance(alphaDistanceB));
						
			addSequential(new Delay(hopperDelay));

			addParallel(new ShooterStartFlywheel());
			addSequential(new DriveDistance(alphaDistanceC));
			addSequential(redAlliance ? new DriveTurnRight90() : new DriveTurnLeft90() );

			addSequential(new DriveRotateToTarget());
			addSequential(new DriveRotateToTarget());

			addParallel(new ShooterStartFeeder());
			addParallel(new ShooterStartAgitator());
			
		case "Beta":
			addParallel(new PlaySound("hopper"));
			addSequential(new DriveDistance(betaDistanceA));
			
			if (ds.getAlliance() == DriverStation.Alliance.Red) {
				addSequential(new DriveDistanceArc(betaDistanceB, betaCurveB));
			} else {
				addSequential(new DriveDistanceArc(betaDistanceB, betaCurveB));
			}			
			
			addSequential(new Delay(0.5));
			addSequential(new DriveDistance(-1));
			
			addParallel(new ShooterStartFlywheel());
			
			addSequential(new DriveTurnRight90());
			addSequential(new DriveRotateToTarget());
			addSequential(new DriveRotateToTarget());
	
			addParallel(new ShooterStartFeeder());
			addParallel(new ShooterStartAgitator());
		case "Gamma":
			
		case "Delta":
			
		}	
		
		////////////////////////////////////////////////////////////////////////
		//This is the base used for all four of the iterations of 40 ball auto//
		////////////////////////////////////////////////////////////////////////
		
//		addParallel(new PlaySound("hopper"));
//		addSequential(new DriveDistance(5.8));
//
//		addSequential(new DriveTurnRight90());
//		addSequential(new DriveTurnLeft90());
//
//		
//		if (ds.getAlliance() == DriverStation.Alliance.Red) {
//			addSequential(new DriveTurnRight90());
//		} else {
//			addSequential(new DriveTurnLeft90());
//		}
//		
//		
//		addSequential(new DriveDistance(2.2));
//		
//		addSequential(new DriveDistanceArc(4.2));
//		
//		addSequential(new Delay(0.5));
//		addSequential(new DriveDistance(-1));
//		
//		addParallel(new ShooterStartFlywheel());
//		
//		addSequential(new DriveTurnRight90());
//		addSequential(new DriveRotateToTarget());
//		addSequential(new DriveRotateToTarget());
//
//		addParallel(new ShooterStartFeeder());
//		addParallel(new ShooterStartAgitator());
		
		
		
		
		

	}
}
