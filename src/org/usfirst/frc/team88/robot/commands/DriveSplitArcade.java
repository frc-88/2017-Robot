package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSplitArcade extends Command {
	private static final int DRIVING = 1;
	private static final int PREP = 2;
	private static final int SHIFT = 3;
	public final static double SENSITIVITY = 0.5;
	private static final double DOWNSHIFTSPEED = 300.0;
	private static final double UPSHIFTSPEED = 400.0;
	
	private int state;
	private int lastShift;
	private int count;

	public DriveSplitArcade() {
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
			magnitude = Robot.oi.applySquare(Robot.oi.getDriverZ());
			
			curve = Robot.oi.applyDeadZone(Robot.oi.getDriverLeftX());
			
			if(magnitude < 0){
				Robot.drive.driveCurve(magnitude, -curve, SENSITIVITY);
			} else{
				Robot.drive.driveCurve(magnitude, curve, SENSITIVITY);
			}
			speed = Math.abs(Robot.drive.getAvgSpeed());
			lastShift++;

			// Comment out in order to use open loop and set the state to
			// permanent drive
			if (Robot.drive.isAutoShift() && (lastShift > (Robot.drive.isLowGear() ? 50 : 5)
					&& ((speed > UPSHIFTSPEED && Robot.drive.isLowGear() == true)
							|| (speed < DOWNSHIFTSPEED && Robot.drive.isLowGear() == false)))) {
				count = 0;
				state = PREP;
			}
			break;

		case PREP:
    		Robot.drive.setTarget(0, 0);
    		
    		if (count++ > 2) {
    			state = SHIFT;
    		}
    		
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
