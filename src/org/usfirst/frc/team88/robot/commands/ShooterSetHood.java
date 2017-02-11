package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterSetHood extends InstantCommand {
	private double target;

	public ShooterSetHood() {
		super();
		requires(Robot.shooter);

		target = -1;
	}

	public ShooterSetHood(double value) {
		super();
		requires(Robot.shooter);

		target = value;
	}

	// Called once when the command executes
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();

		if (target == -1) {
			target = prefs.getDouble("hoodPosition", 0.5);
		}

		Robot.shooter.setHood(target);
	}

}
