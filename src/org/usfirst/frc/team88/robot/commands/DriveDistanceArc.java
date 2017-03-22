package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistanceArc extends Command {
	// states
	private static final int PREP = 0;
	private static final int ACCELERATE = 1;
	private static final int CRUISE = 2;
	private static final int DECELERATE = 3;
	private static final int STOP = 4;
	private static final int END = 5;
	
	private static final double MAX_SPEED = 1.0;
	private static final double ACCELERATION_SCALE = 0.08;
	private final static double SENSITIVITY = 0.5;
	private static double CURVE;

	private int state;
	private double inputDistance;
	private double targetDistance;
	private double rampupDistance;
	private double speed;

	public DriveDistanceArc() {
		requires(Robot.drive);
		inputDistance = -1;
	}
	
	public DriveDistanceArc(double distance) {
		requires(Robot.drive);
		CURVE = 0.4;
		inputDistance = distance;
	}
	
	public DriveDistanceArc(double distance, double arc){
		requires(Robot.drive);
		CURVE = arc;
		inputDistance = distance;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();

		state = PREP;

		if (inputDistance == -1) {
			targetDistance = prefs.getDouble("driveDistanceArc", 1.0);
			CURVE = prefs.getDouble("driveDistanceArcCurve", 0.4);
		} else {
			targetDistance = inputDistance;
		}
		
		Robot.drive.resetDrive();
		Robot.drive.disableRampRate();
		speed = 0.0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch (state) {
		case PREP:
			if (Robot.drive.getAvgPosition() < 1) {
				state = ACCELERATE;
			}
			break;
		case ACCELERATE: // ramping up, gradually increase velocity until we get
							// to desired speed
			speed = speed + ACCELERATION_SCALE;
			Robot.drive.driveCurve(speed, CURVE, SENSITIVITY);

			if (Robot.drive.getAvgPosition() > targetDistance / 2.0) {
				state = DECELERATE;
			}

			if (speed > MAX_SPEED) {
				rampupDistance = Robot.drive.getAvgPosition();
				state = CRUISE;
			}
			break;
		case CRUISE: // consistent speed until we get close
			Robot.drive.driveCurve(speed, CURVE, SENSITIVITY);

			if (Robot.drive.getAvgPosition() > targetDistance - rampupDistance) {
				state = DECELERATE;
			}
			break;
		case DECELERATE: // slow down as we approach target, gradually decrease
							// velocity
			speed = speed - ACCELERATION_SCALE;
			Robot.drive.driveCurve(speed, CURVE, SENSITIVITY);
			if (speed < 0.02) {
				state = STOP;
			}
			break;
		case STOP: // stop
			Robot.drive.driveCurve(0.0, 0.0);
			state = END;
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
