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
		double gearDelay = prefs.getDouble("Gear Delay", 0.0);
		
		double alphaDistanceA = prefs.getDouble("Alpha Distance A", 0.0);
		double alphaDistanceB = prefs.getDouble("Alpha Distance B", 0.0);
		double alphaDistanceC = prefs.getDouble("Alpha Distance C", 0.0);
		
		double betaDistanceA = prefs.getDouble("Beta Distance A", 0.0);
		double betaDistanceB = prefs.getDouble("Beta Distance B", 0.0);
		double betaCurveB = prefs.getDouble("Beta Curve B", 0.0);
		double betaDistanceC = prefs.getDouble("Beta Distance C", 0.0);
		
		double gammaDistanceA = prefs.getDouble("Gamma Distance A", 0.0);
		double gammaDistanceB = prefs.getDouble("Gamma Distance B", 0.0);
		double gammaCurveB = prefs.getDouble("Gamma Curve B", 0.0);
		double gammaDistanceC = prefs.getDouble("Gamma Distance C", 0.0);
		double gammaCurveC = prefs.getDouble("Gamma Curve C", 0.0);
		
		double deltaDistanceA = prefs.getDouble("Delta Distance A", 0.0);
		double deltaDistanceB = prefs.getDouble("Delta Distance B", 0.0);
		double deltaCurveB = prefs.getDouble("Delta Curve B", 0.0);
		double deltaDistanceC = prefs.getDouble("Delta Distance C", 0.0);
		double deltaCurveC = prefs.getDouble("Delta Curve C", 0.0);
		
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
			addSequential(new DriveDistance(betaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(betaDistanceB, betaCurveB) : new DriveDistanceArc(betaDistanceB, -betaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			addParallel(new ShooterStartFlywheel());
			addSequential(new DriveDistance(betaDistanceC));
			addSequential(redAlliance ? new DriveTurnRight90() : new DriveTurnLeft90() );
			
			addSequential(new DriveRotateToTarget());
			addSequential(new DriveRotateToTarget());
	
			addParallel(new ShooterStartFeeder());
			addParallel(new ShooterStartAgitator());
			
		case "Gamma":
			addSequential(new DriveDistance(gammaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(gammaDistanceB, gammaCurveB) : new DriveDistanceArc(gammaDistanceB, -gammaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			addParallel(new ShooterStartFlywheel());
			
			addSequential(redAlliance ? new DriveDistanceArc(gammaDistanceC, gammaCurveC) : new DriveDistanceArc(gammaDistanceC, -gammaCurveC) );
			
			addSequential(new DriveRotateToTarget());
			addSequential(new DriveRotateToTarget());
	
			addParallel(new ShooterStartFeeder());
			addParallel(new ShooterStartAgitator());
			
		case "Delta":
			addSequential(new DriveDistance(deltaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(deltaDistanceB, deltaCurveB) : new DriveDistanceArc(deltaDistanceB, -deltaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			addParallel(new ShooterStartFlywheel());
						
//			addSequential(new DriveDeliverGear());
			addSequential(new GearEject());
			addSequential(new Delay(gearDelay));
			
			addParallel(new ShooterStartFlywheel());
			addSequential(new DriveDistance(deltaDistanceC));
			
			addSequential(new DriveRotateToTarget());
			addSequential(new DriveRotateToTarget());
	
			addParallel(new ShooterStartFeeder());
			addParallel(new ShooterStartAgitator());
			
		}	
		
		////////////////////////////////////////////////////////////////////////
		//This is the base used for all four of the iterations of 40 ball auto//
		////////////////////////////////////////////////////////////////////////
		
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
