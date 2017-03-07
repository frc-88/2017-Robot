package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class JetsonToggleTarget extends InstantCommand {

    public JetsonToggleTarget() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.jetson);
        Robot.jetson.deactivateTarget();
    }

    // Called once when the command executes
    protected void initialize() {
    	if(Robot.jetson.isTargetActive()){
    		Robot.jetson.deactivateTarget();
    	}else{
    		Robot.jetson.activateTarget();
    	}
    }

}
