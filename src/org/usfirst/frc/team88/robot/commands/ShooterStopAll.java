package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStopAll extends InstantCommand {
	private Preferences prefs;

	public ShooterStopAll() {
		super();
		requires(Robot.shooter);

	}

	// Called once when the command executes
	protected void initialize() {
		Robot.shooter.setFlywheel(0.0);
		Robot.shooter.setFeeder(0.0);
	}

}
