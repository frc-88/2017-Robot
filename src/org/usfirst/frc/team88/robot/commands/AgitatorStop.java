package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class AgitatorStop extends InstantCommand {

    public AgitatorStop() {
        super();
        requires(Robot.agitator);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
		Robot.agitator.setSpeed(0.0);
    }

}
