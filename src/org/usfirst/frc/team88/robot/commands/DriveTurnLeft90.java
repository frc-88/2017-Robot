package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveTurnLeft90 extends Command {
	double targetAngle;

	public DriveTurnLeft90() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (Robot.drive.getYaw() < -90) {
			targetAngle = Robot.drive.getYaw() + 270;
		} else {
			targetAngle = Robot.drive.getYaw() - 90;
		}

		Robot.drive.disableRampRate();
		Robot.drive.rotateController.reset();
		Robot.drive.rotateController.setSetpoint(targetAngle);
		Robot.drive.rotateController.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drive.rotateController.onTarget();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.rotateController.disable();
		Robot.drive.enableRampRate();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.rotateController.disable();
		Robot.drive.enableRampRate();
	}
}
