package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class JetsonPower extends InstantCommand {
	private boolean target;
	
    public JetsonPower(boolean value) {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.jetson);
        
        target = value;
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.jetson.setPower(target);
    }

}
