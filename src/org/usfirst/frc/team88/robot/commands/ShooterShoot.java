package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterShoot extends Command {
	Preferences prefs;
	double flywheelSpeed, feederSpeed, hoodPosition;

	public ShooterShoot() {
		requires(Robot.shooter);
		//requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		prefs = Preferences.getInstance();
		flywheelSpeed = prefs.getDouble("flywheelSpeed", 0.0);
		feederSpeed = prefs.getDouble("feederSpeed", 0.0);
		hoodPosition = prefs.getDouble("hoodPosition", 0.5);

		Robot.shooter.setFlywheel(flywheelSpeed);
		Robot.shooter.setHood(hoodPosition);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.shooter.flywheelOnTarget() && Robot.shooter.hoodOnTarget(hoodPosition)) {
			Robot.shooter.setFeeder(feederSpeed);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		//return Robot.drive.collisionDetected();
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.shooter.setFeeder(0.0);
		Robot.shooter.setFlywheel(0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.shooter.setFeeder(0.0);
		Robot.shooter.setFlywheel(0.0);
	}
}
