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
		
		String version = prefs.getString("40BallVersion", "Alpha");
		boolean redAlliance = prefs.getBoolean("RedAlliance", ds.getAlliance() == DriverStation.Alliance.Red);
		
		double hopperDelay = prefs.getDouble("HopperDelay", 0.0);
		boolean shoot = prefs.getBoolean("Shoot", false);
		double gearDelay = prefs.getDouble("GearDelay", 0.0);
		
		double alphaDistanceA = prefs.getDouble("AlphaDistanceA", 0.0);
		double alphaDistanceB = prefs.getDouble("AlphaDistanceB", 0.0);
		double alphaDistanceC = prefs.getDouble("AlphaDistanceC", 0.0);
		
		double betaDistanceA = prefs.getDouble("BetaDistanceA", 0.0);
		double betaDistanceB = prefs.getDouble("BetaDistanceB", 0.0);
		double betaCurveB = prefs.getDouble("BetaCurveB", 0.0);
		double betaDistanceC = prefs.getDouble("BetaDistanceC", 0.0);
		
		double gammaDistanceA = prefs.getDouble("GammaDistanceA", 0.0);
		double gammaDistanceB = prefs.getDouble("GammaDistanceB", 0.0);
		double gammaCurveB = prefs.getDouble("GammaCurveB", 0.0);
		double gammaDistanceC = prefs.getDouble("GammaDistanceC", 0.0);
		double gammaCurveC = prefs.getDouble("GammaCurveC", 0.0);
		
		double deltaDistanceA = prefs.getDouble("DeltaDistanceA", 0.0);
		double deltaDistanceB = prefs.getDouble("DeltaDistanceB", 0.0);
		double deltaCurveB = prefs.getDouble("DeltaCurve B", 0.0);
		double deltaDistanceC = prefs.getDouble("DeltaDistanceC", 0.0);
		
		addParallel(new GearReceiverOut());
		addParallel(new PlaySound("hopper"));
		addParallel(new ShooterSetHood(.42));
		
		switch(version){
		case "Alpha":
			addSequential(new DriveDistance(alphaDistanceA));
			
			addSequential(new DriveTurnLeft90());
			
			addSequential(new DriveDistance(alphaDistanceB));
						
			addSequential(new Delay(hopperDelay));
			
			addSequential(new DriveDistance(alphaDistanceC));
			addSequential(new DriveTurnLeft90());

			addSequential(new DriveRotateToBoiler());

				addSequential(new ShooterStartFlywheel());
				addSequential(new Delay(0.2));
				addSequential(new ShooterStartFeeder());
				addSequential(new ShooterStartAgitator());
			
			break;
			
		case "Beta":
			addSequential(new DriveDistance(betaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(betaDistanceB, betaCurveB) : new DriveDistanceArc(betaDistanceB, -betaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			if (shoot) {
				addParallel(new ShooterStartFlywheel());
			}
			addSequential(new DriveDistance(betaDistanceC));
			addSequential(redAlliance ? new DriveTurnRight90() : new DriveTurnLeft90() );
			
			addSequential(new DriveRotateToBoiler());
			addSequential(new DriveRotateToBoiler());
	
			if (shoot) {
				addParallel(new ShooterStartFeeder());
				addParallel(new ShooterStartAgitator());
			}
			break;
			
		case "Gamma":
			addSequential(new DriveDistance(gammaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(gammaDistanceB, gammaCurveB) : new DriveDistanceArc(gammaDistanceB, -gammaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			if (shoot) {
				addParallel(new ShooterStartFlywheel());
			}
			
			addSequential(redAlliance ? new DriveDistanceArc(gammaDistanceC, gammaCurveC) : new DriveDistanceArc(gammaDistanceC, -gammaCurveC) );
			
			addSequential(new DriveRotateToBoiler());
			addSequential(new DriveRotateToBoiler());
	
			if (shoot) {
				addParallel(new ShooterStartFeeder());
				addParallel(new ShooterStartAgitator());
			}
			break;
			
		case "Delta":
			addSequential(new DriveDistance(deltaDistanceA));
			
			addSequential(redAlliance ? new DriveDistanceArc(deltaDistanceB, deltaCurveB) : new DriveDistanceArc(deltaDistanceB, -deltaCurveB) );
			addSequential(new Delay(hopperDelay));
			
			addSequential(new DriveDeliverGear(this));
			addSequential(new GearEject());
			addSequential(new Delay(gearDelay));
			
			if (shoot) {
				addParallel(new ShooterStartFlywheel());
			}
			addSequential(new DriveDistance(deltaDistanceC));
			
			addSequential(new DriveRotateToBoiler());
			addSequential(new DriveRotateToBoiler());
	
			if (shoot) {
				addParallel(new ShooterStartFeeder());
				addParallel(new ShooterStartAgitator());
			}
			break;
			
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
