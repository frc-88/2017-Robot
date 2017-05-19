package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPanGetGear extends Command {

	private double speed;
	private int counter;
    public GearPanGetGear(double speed) {
    	requires(Robot.gearPan);
    	this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	counter = 0;
    	Robot.gearPan.resetIsDone();
    	Robot.gearPan.panOut();
    	Robot.gearPan.intakeSpeed(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearPan.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(counter++ > 15){
    		return Robot.gearPan.isDone();
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.oi.rumbleOperator(1.0);
    	Robot.oi.rumbleDriver(1.0);
    	Robot.gearPan.panIn();
    	Robot.gearPan.intakeStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.gearPan.panIn();
    	Robot.gearPan.intakeStop();
    }
}
