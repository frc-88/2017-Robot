package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStartFeeder extends InstantCommand {
	private Preferences prefs;

	public ShooterStartFeeder() {
		super();
		requires(Robot.shooter);
		requires(Robot.agitator);

	}

	// Called once when the command executes
	protected void initialize() {
		prefs = Preferences.getInstance();

		double feederSpeed = prefs.getDouble("feederSpeed", 0.0);
		double agitatorSpeed = prefs.getDouble("agitatorSpeed", 0.0);

		Robot.shooter.setFeeder(feederSpeed);
		Robot.agitator.setSpeed(agitatorSpeed);
	}

}
