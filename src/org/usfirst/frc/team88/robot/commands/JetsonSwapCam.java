package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JetsonSwapCam extends Command {
	private final double CAMMAX = 6;
	private final double CAMMIN = 1;
    public JetsonSwapCam() {
    	requires(Robot.jetson);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double cam = Robot.jetson.getCam();
    	if(cam == CAMMAX){
    		cam = CAMMIN;
    	} else{
    		cam++;
    	}
    	Robot.jetson.setCam(cam);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
