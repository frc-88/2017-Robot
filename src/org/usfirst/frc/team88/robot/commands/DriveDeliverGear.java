package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class DriveDeliverGear extends Command {
	// states
	private static final int PREP = 0;
	private static final int ALIGN = 1;
	private static final int ACCELERATE = 2;
	private static final int CRUISE = 3;
	private static final int DECELERATE = 4;
	private static final int STOP = 5;
	private static final int END = 6;

	private static final double MAX_SPEED = 0.4;
	private static final double ACCELERATION_SCALE = 0.01;
	private static final double SWEET_SPOT = 15.0;
	private static final double ALIGN_SPEED = 0.1;

	private CommandGroup originator;
	private int state;
	private double targetDistance;
	private double direction;
	private double rampupDistance;
	private double speed;
	private double curve;
	NetworkTable robotTable;

	public DriveDeliverGear() {
		this(null);
	}

	public DriveDeliverGear(CommandGroup group) {
		requires(Robot.drive);

		robotTable = NetworkTable.getTable("robot");
		originator = group;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();

		state = PREP;

		targetDistance = (Robot.jetson.getGearDistance()) / 12.0;
		direction = -1.0;

		if (targetDistance < 0.0) {
			if (originator != null) {
				originator.cancel();
			}
			state = END;
		}

		Robot.drive.resetDrive();
		Robot.drive.disableRampRate();
		speed = 0.0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double gamma = Robot.jetson.getGearGamma();
		curve = (gamma * direction) * 0.03;

		switch (state) {
		case PREP: // be sure encoders have reset before we start
			if (Math.abs(Robot.drive.getAvgPosition()) < 1) {
				state = ALIGN;
			}
			break;

		case ALIGN: // rotate so that gamma is in our sweet spot
			if (Math.abs(gamma) < SWEET_SPOT) {
				Robot.drive.setTarget(0.0, 0.0);
				state = ACCELERATE;
			} else if (gamma > 0) {
				Robot.drive.setTarget(ALIGN_SPEED, -ALIGN_SPEED);
			} else if (gamma < 0) {
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
		Robot.jetson.deactivateTarget();
		robotTable.putString("sound", "work-complete");
		Robot.drive.enableRampRate();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.driveCurve(0.0, 0.0);
		Robot.drive.enableRampRate();
	}
}
