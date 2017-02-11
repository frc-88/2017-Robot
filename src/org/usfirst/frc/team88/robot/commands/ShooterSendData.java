package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterSendData extends Command {

    public ShooterSendData() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*
		SmartDashboard.putDouble("Left Input", Robot.oi.getDriverLeftVerticalAxis());
		SmartDashboard.putDouble("Right Input", Robot.oi.getDriverRightVerticalAxis());
		SmartDashboard.putDouble("Left Maxed", Robot.oi.applyMaxValue(Robot.oi.getDriverLeftVerticalAxis()));
		SmartDashboard.putDouble("Right Maxed", Robot.oi.applyMaxValue(Robot.oi.getDriverRightVerticalAxis()));
		*/
    	
		Robot.shooter.updateDashboard();
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
