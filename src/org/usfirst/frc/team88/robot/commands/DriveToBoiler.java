package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToBoiler extends Command {
	// states
	private static final int PREP = 0;
	private static final int ALIGN = 1;
	private static final int ACCELERATE = 2;
	private static final int CRUISE = 3;
	private static final int DECELERATE = 4;
	private static final int STOP = 5;
	private static final int END = 6;

	private static final double MAX_SPEED = 0.6;
	private static final double ACCELERATION_SCALE = 0.02;
	private static final double SWEET_SPOT = 15.0;
	private static final double ALIGN_SPEED = 0.2;

	private int state;
	private double targetDistance;
	private double direction;
	private double rampupDistance;
	private double speed;
	private double curve;

	public DriveToBoiler() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = PREP;

		targetDistance = Robot.jetson.getBoilerDistance() - 10.5;
		if (targetDistance < 0) {
			state = END;
		}
		direction = 1.0;

		Robot.drive.resetDrive();
		Robot.drive.disableRampRate();
		speed = 0.0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double angle = Robot.jetson.getBoilerAngle();
		curve = (angle * direction) * 0.03;

		switch (state) {
		case PREP: // be sure encoders have reset before we start
			if (Math.abs(Robot.drive.getAvgPosition()) < 1) {
				state = ALIGN;
			}
			break;

		case ALIGN: // rotate so that angle is in our sweet spot
			if (Math.abs(angle) < SWEET_SPOT) {
				Robot.drive.setTarget(0.0, 0.0);
				state = ACCELERATE;
			} else if (angle > 0) {
				Robot.drive.setTarget(ALIGN_SPEED, -ALIGN_SPEED);
			} else if (angle < 0) {
				Robot.drive.setTarget(-ALIGN_SPEED, ALIGN_SPEED);
			}
			break;

		case ACCELERATE: // gradually increase velocity until we get to desired speed
			speed = speed + (ACCELERATION_SCALE * direction);

			if (Math.abs(Robot.drive.getAvgPosition()) > targetDistance / 2.0) {
				state = DECELERATE;
			} else if (Math.abs(speed) >= MAX_SPEED) {
				speed = MAX_SPEED * direction;
				rampupDistance = Math.abs(Robot.drive.getAvgPosition());
				state = CRUISE;
			}

			Robot.drive.driveCurve(speed, curve);

			break;

		case CRUISE: // consistent speed until we get close
			Robot.drive.driveCurve(speed, curve);

			if (Math.abs(Robot.drive.getAvgPosition()) > targetDistance - rampupDistance) {
				state = DECELERATE;
			}
			break;

		case DECELERATE: // slow down as we approach target, gradually decrease velocity
			speed = speed - (ACCELERATION_SCALE * direction);

			Robot.drive.driveCurve(speed, curve);

			if (Math.abs(speed) < ACCELERATION_SCALE) {
				state = STOP;
			}
			break;

		case STOP: // stop
			Robot.drive.driveCurve(0.0, 0.0);
			state = END;
			break;

		case END: // targetDistance = 0, do nothing
			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return state == END;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.enableRampRate();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.driveCurve(0.0, 0.0);
		Robot.drive.enableRampRate();
	}
}
