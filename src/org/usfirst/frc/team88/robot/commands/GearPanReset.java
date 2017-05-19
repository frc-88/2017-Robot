package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPanReset extends Command {

    public GearPanReset() {
    	requires(Robot.gearPan);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.oi.rumbleOperator(0.0);
    	Robot.oi.rumbleDriver(0.0);
    	Robot.gearPan.panIn();
    	Robot.gearPan.intakeStop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
