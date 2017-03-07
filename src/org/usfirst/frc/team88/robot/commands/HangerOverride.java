package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HangerOverride extends InstantCommand {

	private boolean override;
    public HangerOverride(boolean OR) {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hanger);
        override = OR;
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.hanger.setOverride(override);
    }

}
