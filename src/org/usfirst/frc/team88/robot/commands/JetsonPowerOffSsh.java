package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class JetsonPowerOffSsh extends InstantCommand {

    public JetsonPowerOffSsh() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.jetson);
    }

    // Called once when the command executes
    protected void initialize() {
    	try {
			Robot.jetson.powerOff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
