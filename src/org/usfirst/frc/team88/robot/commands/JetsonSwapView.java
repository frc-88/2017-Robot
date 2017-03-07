package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class JetsonSwapView extends InstantCommand {

	public JetsonSwapView() {
		super();
		requires(Robot.jetson);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.jetson.toggleView();
	}
}
