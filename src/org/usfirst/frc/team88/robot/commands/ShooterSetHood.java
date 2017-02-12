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
		double position;
		Preferences prefs = Preferences.getInstance();

		if (target < 0) {
			position = prefs.getDouble("hoodPosition", 0.5);
		} else {
			position = target;
		}

		Robot.shooter.setHood(position);
	}

}
