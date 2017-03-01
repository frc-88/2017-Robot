package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *	now turn left 30
 */
public class DriveTurn extends Command {
	double inputAngle;
	double targetAngle;

	public DriveTurn(double angle) {
		requires(Robot.drive);

		inputAngle = angle;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		targetAngle = Robot.drive.getYaw() + inputAngle;
		
		if (targetAngle < -180) {
			targetAngle += 360;
		} else if (targetAngle > 180) {
			targetAngle -= 360;
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
