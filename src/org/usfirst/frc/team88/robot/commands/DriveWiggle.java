package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveWiggle extends Command {
	private static final int WIGGLE_COUNT = 3;
	private static final double WIGGLE_SPEED = 0.3;
	private static final int WIGGLE_REPS = 4;
	
	private int count;
	private int direction;
	private int reps;
	
    public DriveWiggle() {
    	requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	count = 0;
    	reps = 0;
    	direction = 1;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drive.setTarget(WIGGLE_SPEED * direction, -WIGGLE_SPEED * direction);
    	
    	if (count++ > WIGGLE_COUNT) {
    		count = 0;
    		if (reps++ % 2 == 0) {
    			direction = -direction;
    		}
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (reps == WIGGLE_REPS);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.setTarget(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.setTarget(0.0, 0.0);
    }
}
