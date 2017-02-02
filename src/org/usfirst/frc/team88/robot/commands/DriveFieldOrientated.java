package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveFieldOrientated extends Command {
	private static final int DRIVING = 1;
	private static final int PREP = 2;
	private static final int SHIFT = 3;
	public final static double SENSITIVITY = 0.5;
	private static final double DOWNSHIFTSPEED = 300.0;
	private static final double UPSHIFTSPEED = 400.0;
	
	private int state;
	private int lastShift;

	public DriveFieldOrientated() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.setClosedLoopSpeed();
		Robot.drive.enableRampRate();
		state = DRIVING;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double x, y, speed, magnitude, angle, curve;

		switch (state) {
		case DRIVING:
			magnitude = Robot.oi.getDriverLeftY();

			x = Robot.oi.getDriverRightX();
			y = Robot.oi.getDriverRightY();

			if (x == 0 && y == 0) {
				angle = 0;
				curve = 0;
			} else {
				angle = Math.toDegrees(Math.atan2(x, y));
				curve = angle - Robot.drive.getYaw();
			}

			if (curve > 180) {
				curve = curve - 360;
			} else if (curve < -180) {
				curve = curve + 360;
			}

			if(magnitude < 0){
				Robot.drive.driveCurve(magnitude, -curve / 180.0, SENSITIVITY);
			} else{
				Robot.drive.driveCurve(magnitude, curve / 180.0, SENSITIVITY);
			}
			speed = Math.abs(Robot.drive.getAvgSpeed());
			lastShift++;

			// Comment out in order to use open loop and set the state to
			// permanent drive
			if (Robot.drive.isAutoShift() && (lastShift > (Robot.drive.isLowGear() ? 50 : 5)
					&& ((speed > UPSHIFTSPEED && Robot.drive.isLowGear() == true)
							|| (speed < DOWNSHIFTSPEED && Robot.drive.isLowGear() == false)))) {
				state = PREP;
			}
			break;

		case PREP:
			state = SHIFT;
			break;

		case SHIFT:
			Robot.drive.shift();
			lastShift = 0;
			state = DRIVING;
			break;
		}

		Robot.drive.updateDashboard();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
