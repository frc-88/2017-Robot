package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStopFeeder extends InstantCommand {

	public ShooterStopFeeder() {
		super();
		requires(Robot.shooter);
	}

	// Called once when the command executes
	protected void initialize() {
		Robot.shooter.setFeeder(0.0);
	}
}
