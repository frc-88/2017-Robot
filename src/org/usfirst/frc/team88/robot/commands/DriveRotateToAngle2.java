package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveRotateToAngle2 extends Command {
	// states
	private static final int PREP = 0;
	private static final int ACCELERATE = 1;
	private static final int CRUISE = 2;
	private static final int DECELERATE = 3;
	private static final int STOP = 4;
	private static final int END = 5;

	private static final double MAX_SPEED = 2.5;
	private static final double ACCELERATION = 0.25;

	private int state;
	private double direction;
	private double rampupDistance;
	private double speed;
	private double distance;
	private double initialYaw;
	private double currentYaw;
	private double targetYaw;
	private double targetAngle;
	
    public DriveRotateToAngle2(double angle) {
        requires(Robot.drive);

        targetAngle = angle;
        
    	if (targetAngle < 0) {
    		targetAngle = -targetAngle;
    		direction = -1.0;
    	} else if (targetAngle > 0) {
    		direction = 1.0;
    	}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = PREP;
    	speed = 0.0;
		initialYaw = Robot.drive.getYaw();
		targetYaw = initialYaw + targetAngle * direction;
		
		if (targetYaw > 180) {
			targetYaw -= 360;
		} else if (targetYaw < -180) {
			targetYaw += 360;
		}
    	    	
		Robot.drive.resetDrive();
    	Robot.drive.disableRampRate();

    	Robot.drive.rotateController.reset();
    	Robot.drive.rotateController.setSetpoint(initialYaw);
    	Robot.drive.rotateController.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentYaw = Robot.drive.getYaw();
    	distance = Math.abs(currentYaw - initialYaw);
    	
    	if (distance > 180) {
    		distance = Math.abs(distance - 360);
    	}
    	
    	switch (state) {
    	case PREP:
    		state = ACCELERATE;
    		break;
    		
    	case ACCELERATE:
    		speed = speed +(ACCELERATION * direction);
    		
			if (distance > targetAngle / 2.0) {
				state = DECELERATE;
			} else if (Math.abs(speed) >= MAX_SPEED) {
				speed = MAX_SPEED * direction;
				rampupDistance = distance;
				state = CRUISE;
			}
			
			Robot.drive.rotateController.setSetpoint(currentYaw + speed);
			
			break;
		
    	case CRUISE:
			Robot.drive.rotateController.setSetpoint(currentYaw + speed);

			if (distance > targetAngle - rampupDistance * 2) {
				state = DECELERATE;
			}
			break;

		case DECELERATE: 
			speed = speed - (ACCELERATION * direction);

			Robot.drive.rotateController.setSetpoint(currentYaw + speed);

			if (Math.abs(speed) < ACCELERATION) {
				state = STOP;
			}
			
			if (distance - targetAngle < speed) {
				state = STOP;
			}
						
			break;

		case STOP: // stop
			Robot.drive.rotateController.setSetpoint(targetYaw);
			state = END;
			break;

		case END: // targetDistance = 0, do nothing
			break;

    		
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return state == END;
	}

    // Called once after isFinished returns true
    protected void end() {
		Robot.drive.rotateController.disable();    	
		Robot.drive.resetDrive();
		Robot.drive.enableRampRate();
   }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		Robot.drive.rotateController.disable();    	
		Robot.drive.resetDrive();
		Robot.drive.enableRampRate();
   }
   
}
