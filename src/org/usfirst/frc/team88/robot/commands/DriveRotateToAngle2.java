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

	private static final double MAX_SPEED = 4.0;
	private static final double ACCELERATION = 0.5;

	private int state;
	private double targetYaw;
	private double direction;
	private double rampupDistance;
	private double speed;
	private double initialYaw;
	private double currentYaw;

	double targetAngle;
	
    public DriveRotateToAngle2(double angle) {
        requires(Robot.drive);

        targetAngle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = PREP;
    	speed = 0.0;
    	initialYaw = Robot.drive.getYaw();
    	if (targetAngle < 0) {
    		targetAngle = -targetAngle;
    		direction = -1.0;
    	} else if (targetAngle > 0) {
    		direction = 1.0;
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
    	
    	switch (state) {
    	case PREP:
    		break;
    	case ACCELERATE:
    		speed = speed +(ACCELERATION * direction);
    		
			if (Math.abs(currentYaw - initialYaw) > targetAngle / 2.0) {
				state = DECELERATE;
			} else if (Math.abs(speed) >= MAX_SPEED) {
				speed = MAX_SPEED * direction;
				rampupDistance = Math.abs(currentYaw - initialYaw);
				state = CRUISE;
			}
			
			Robot.drive.rotateController.setSetpoint(currentYaw + speed);
			
			break;
		
    	case CRUISE:
			Robot.drive.rotateController.setSetpoint(currentYaw + speed);

			if (Math.abs(currentYaw - initialYaw) > targetAngle - rampupDistance) {
				state = DECELERATE;
			}
			break;

		case DECELERATE: 
			speed = speed - (ACCELERATION * direction);

			Robot.drive.rotateController.setSetpoint(currentYaw + speed);

			if (Math.abs(speed) < ACCELERATION) {
				state = STOP;
			}
			
			if (Math.abs(currentYaw - initialYaw) - targetYaw < speed) {
				state = STOP;
			}
			
			break;

		case STOP: // stop
			Robot.drive.rotateController.setSetpoint(initialYaw - targetAngle);
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
		Robot.drive.enableRampRate();
   }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		Robot.drive.rotateController.disable();    	
		Robot.drive.enableRampRate();
   }
   
}
