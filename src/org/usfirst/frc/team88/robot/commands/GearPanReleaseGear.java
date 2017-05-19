package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPanReleaseGear extends Command {

	private final static int START_DELAY = 20;
	private final static int STOP_DELAY = 30;
	private double count;
    public GearPanReleaseGear() {
    	requires(Robot.gearPan);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	count = 0;
    	Robot.gearPan.panOut();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(count++ > START_DELAY){
        	Robot.gearPan.intakeSpeed(0.5);
    	}
    	Robot.gearPan.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return count > STOP_DELAY;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
