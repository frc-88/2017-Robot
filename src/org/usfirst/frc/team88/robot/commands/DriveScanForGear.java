package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveScanForGear extends Command {
	double maxAngle;
	double targetAngle;
	
    public DriveScanForGear(double angle) {
        requires(Robot.drive);

        maxAngle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	targetAngle = Robot.drive.getYaw() + maxAngle;
    	if(targetAngle > 180){
    		targetAngle = targetAngle - 360;
    	}else if(targetAngle < -180){
    		targetAngle = targetAngle + 360;
    	}
		
    	Robot.drive.disableRampRate();
    	Robot.drive.rotateController.reset();
        Robot.drive.setRotateSpeedSlow();
    	Robot.drive.rotateController.setSetpoint(targetAngle);
    	Robot.drive.rotateController.enable();    	
        Robot.drive.enableBrakeMode(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean gearFound = false;
    	
    	if ( (Robot.jetson.getGearDistance() > 0.0) && (Math.abs(Robot.jetson.getGearGamma()) < 24) ) {
    		gearFound = true;
    	}
    	
    	return gearFound || Robot.drive.rotateController.onTarget();
	}

    // Called once after isFinished returns true
    protected void end() {
		Robot.drive.rotateController.disable();
		Robot.drive.setTarget(0, 0);
		Robot.drive.setRotateSpeedFast();
		Robot.drive.enableRampRate();
        Robot.drive.enableBrakeMode(false);
   }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		Robot.drive.rotateController.disable();    	
		Robot.drive.setTarget(0, 0);
		Robot.drive.setRotateSpeedFast();
		Robot.drive.enableRampRate();
        Robot.drive.enableBrakeMode(false);
   }
   
}
