package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStartFlywheel extends InstantCommand {

	public ShooterStartFlywheel() {
		super();
		requires(Robot.shooter);
	}

	// Called once when the command executes
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();
		double speed = prefs.getDouble("flywheelSpeed", 0.0);

		Robot.shooter.setFlywheel(speed);
	}
}
