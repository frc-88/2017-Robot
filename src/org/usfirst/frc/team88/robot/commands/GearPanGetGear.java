package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPanGetGear extends Command {

	private double speed;
    public GearPanGetGear(double speed) {
    	requires(Robot.gearPan);
    	this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearPan.resetIsDone();
    	Robot.gearPan.panOut();
    	Robot.gearPan.intakeSpeed(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.gearPan.isDone();
    }

    // Called once after isFinished returns true
    protected void end() {
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
