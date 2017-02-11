package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.subsystems.Hanger;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HangerStop extends InstantCommand {

    public HangerStop() {
        super();
        requires(Robot.hanger);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.hanger.stopClimber();
    	
    }

}
