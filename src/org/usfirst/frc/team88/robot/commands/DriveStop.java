package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class DriveStop extends InstantCommand {

	public DriveStop() {
		super();
		requires(Robot.drive);
	}

	// Called once when the command executes
	protected void initialize() {
		if (!Robot.drive.isLowGear()) {
			Robot.drive.shift();
		}
		Robot.drive.setClosedLoopSpeed();
		Robot.drive.setTarget(0, 0);
	}

}
