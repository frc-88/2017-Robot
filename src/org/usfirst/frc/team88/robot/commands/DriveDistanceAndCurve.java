
package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistanceAndCurve extends Command {
	// states
	private static final int PREP = 0;
	private static final int ACCELERATE = 1;
	private static final int CRUISE = 2;
	private static final int DECELERATE = 3;
	private static final int STOP = 4;
	private static final int END = 5;

	private static final double MAX_SPEED = 1.0;
	private static final double ACCELERATION_SCALE = 0.03;
	
	private double finalCurve;
	private int state;
	private boolean usePrefs = false;
	private double direction;
	private double inputDistance;
	private double targetDistance;
	private double targetYaw;
	private double rampupDistance;
	private double speed;
	private double curve;
	private double finalDistance;
	private double sensitivity;
	
	public DriveDistanceAndCurve(){
		requires(Robot.drive);
		usePrefs = true;
	}
	public DriveDistanceAndCurve(double distance, double arc){
		requires(Robot.drive);
		inputDistance = distance;
		finalCurve = arc;
		finalDistance = 0.0;
	}

	public DriveDistanceAndCurve(double distance, double arc, double curveDistance) {
		requires(Robot.drive);
		inputDistance = distance;
		finalCurve = arc;
		finalDistance = curveDistance;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();

		state = PREP;

		if (usePrefs) {
			targetDistance = prefs.getDouble("ddacDistance", 1.0);
			finalCurve = prefs.getDouble("ddacCurve", 0.0);
			finalDistance = prefs.getDouble("ddacStartCurve", 0.0);
		} else {
			targetDistance = inputDistance;
		}

		if (targetDistance > 0) {
			direction = 1.0;
		} else if (targetDistance < 0) {
			direction = -1.0;
			targetDistance = -targetDistance;
		} else {
			state = STOP;
		}

		Robot.drive.resetDrive();
		Robot.drive.disableRampRate();
		targetYaw = Robot.drive.getYaw();
		speed = 0.0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if(Math.abs(Robot.drive.getAvgPosition()) > Math.abs(finalDistance)){
			curve = finalCurve * direction;
			sensitivity = 0.5;
		}else{
			curve = (targetYaw - (Robot.drive.getYaw() * direction)) * 0.03;
			sensitivity = 0.15;
		}

		switch (state) {
		case PREP: // be sure encoders have reset before we start
			if (Math.abs(Robot.drive.getAvgPosition()) < 1) {
				state = ACCELERATE;
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

			Robot.drive.driveCurve(speed, curve, sensitivity);

			break;

		case CRUISE: // consistent speed until we get close
			Robot.drive.driveCurve(speed, curve, sensitivity);

			if (Math.abs(Robot.drive.getAvgPosition()) > targetDistance - rampupDistance * 1.33) {
				state = DECELERATE;
			}
			break;

		case DECELERATE: // slow down as we approach target, gradually decrease velocity
			speed = speed - (ACCELERATION_SCALE * direction);

			Robot.drive.driveCurve(speed, curve, sensitivity);

			if (Math.abs(speed) < ACCELERATION_SCALE) {
				state = STOP;
			}

			if (Math.abs(Robot.drive.getAvgPosition()) - 0.1 > targetDistance) {
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

	// Called once after is Finished returns true
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
