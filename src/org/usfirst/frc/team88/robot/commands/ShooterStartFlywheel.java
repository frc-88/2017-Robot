package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStartFlywheel extends InstantCommand {
	private Preferences prefs;

	public ShooterStartFlywheel() {
		super();
		requires(Robot.shooter);

	}

	// Called once when the command executes
	protected void initialize() {
		prefs = Preferences.getInstance();

		double speed = prefs.getDouble("flywheelSpeed", 0.0);

		Robot.shooter.setFlywheel(speed);
	}

}
