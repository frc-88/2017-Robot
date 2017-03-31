package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveRotateToBoiler extends Command {
	boolean noTargetFound = false;
	double targetAngle;

	public DriveRotateToBoiler() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		noTargetFound = false;

		if (Robot.jetson.getBoilerDistance() == -1.0) {
			noTargetFound = true;
		} else {
			targetAngle = Robot.drive.getYaw() + Robot.jetson.getBoilerAngle();
//			Robot.drive.rotateController.setSetpoint(targetAngle);
			Robot.drive.rotateController.setSetpoint(0.0);
			Robot.drive.disableRampRate();
			Robot.drive.rotateController.enable();
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
		SmartDashboard.putNumber("Rot Target Angle", targetAngle);
		SmartDashboard.putNumber("Rot Error", Robot.drive.rotateController.getError());
		SmartDashboard.putNumber("Rot On Target", Robot.drive.rotateController.onTarget()?1:0);

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return noTargetFound || Robot.drive.rotateController.onTarget();}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.rotateController.disable();
		Robot.drive.setTarget(0.0, 0.0);
		Robot.drive.enableRampRate();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.rotateController.disable();
		Robot.drive.setTarget(0.0, 0.0);
		Robot.drive.enableRampRate();
	}
}
