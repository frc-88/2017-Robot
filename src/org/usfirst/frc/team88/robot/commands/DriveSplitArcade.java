package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveSplitArcade extends Command {
	private static final int DRIVING = 1;
	private static final int PREP = 2;
	private static final int SHIFT = 3;
	public final static double SENSITIVITY = 0.5;
	private static final double DOWNSHIFTSPEED = 150.0;
	private static final double UPSHIFTSPEED = 200.0;
	
	private int state;
	private int lastShift;
	private int count;
	private boolean stabilize;
	private double targetYaw;

	public DriveSplitArcade() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.setClosedLoopSpeed();
		Robot.drive.enableRampRate();
		stabilize = false;
		state = DRIVING;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double speed, magnitude, curve;

		switch (state) {
		case DRIVING:
			//magnitude = Robot.oi.applySquare(Robot.oi.getDriverZ());
			magnitude = -Robot.oi.applySquare(Robot.oi.getDriverLeftY());
			
			curve = Robot.oi.applySquare(Robot.oi.getDriverRightX());

			/*
			// stabilize yaw when driving straight
			if ((curve < 0.1) && (magnitude > 0.3) && !Robot.drive.isLowGear()) {
				if (!stabilize) {
					stabilize = true;
					targetYaw = Robot.drive.getYaw();
				}
				curve = (targetYaw - Robot.drive.getYaw()) * 0.03;
			} else {
				stabilize = false;
			}
			*/
			
			if (!Robot.drive.isLowGear()) {
				curve = curve * 0.5;
			}
			
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
