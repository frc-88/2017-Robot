package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveRotateToBoiler extends Command {
	// states
	private static final int PREP_NORMAL = 0;
	private static final int ROTATE_NORMAL = 1;
	private static final int PREP_BOILER = 2;
	private static final int ROTATE_BOILER = 3;
	private static final int STOP = 4;
	private static final int END = 5;
	private static final double SWEETSPOT = 10;
	
	private int state;
	private double targetAngle;

	public DriveRotateToBoiler() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (Robot.jetson.getBoilerDistance() == -1.0) {
			state = END;
		} else if (Math.abs(Robot.jetson.getBoilerAngle()) < SWEETSPOT) {
			state = PREP_BOILER;
		} else {
			state = PREP_NORMAL;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double boilerAngle = Robot.jetson.getBoilerAngle();
		
		switch (state) {
		case PREP_NORMAL:
			Robot.drive.disableRampRate();
			targetAngle = Robot.drive.getYaw() + ((Math.abs(boilerAngle) - 5) * (boilerAngle/Math.abs(boilerAngle)));
			Robot.drive.setRotateModeNormal();
			Robot.drive.rotateController.setSetpoint(targetAngle);
			Robot.drive.rotateController.enable();
			state = ROTATE_NORMAL;
			break;

		case ROTATE_NORMAL:
			if (Robot.drive.rotateController.onTarget()) {
				Robot.drive.rotateController.disable();
				state = PREP_BOILER;
			}
			break;

		case PREP_BOILER:
			Robot.drive.setRotateModeBoiler();
			Robot.drive.rotateBoilerController.setSetpoint(0.0);
			Robot.drive.rotateBoilerController.enable();
			state = ROTATE_BOILER;
			break;

		case ROTATE_BOILER:
			if (Robot.drive.rotateBoilerController.onTarget()) {
				Robot.drive.rotateBoilerController.disable();
				Robot.drive.setRotateModeNormal();
				state = STOP;
			}
			break;

		case STOP:
			Robot.drive.setTarget(0.0, 0.0);
			Robot.drive.enableRampRate();
			state = END;
			break;
			
		case END:
			break;
		}

		SmartDashboard.putNumber("Rot Target Angle", targetAngle);
		SmartDashboard.putNumber("Rot Error", Robot.drive.rotateController.getError());
		SmartDashboard.putNumber("Rot On Target", Robot.drive.rotateController.onTarget() ? 1 : 0);
		SmartDashboard.putNumber("RotB Error", Robot.drive.rotateBoilerController.getError());
		SmartDashboard.putNumber("RotB On Target", Robot.drive.rotateBoilerController.onTarget() ? 1 : 0);
		SmartDashboard.putNumber("RotB State", state);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return state == END;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.rotateController.disable();
		Robot.drive.rotateBoilerController.disable();
		Robot.drive.setRotateModeNormal();
		Robot.drive.setTarget(0.0, 0.0);
		Robot.drive.enableRampRate();
	}
}
