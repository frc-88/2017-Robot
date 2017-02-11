package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HangerStart extends InstantCommand {

	Preferences prefs;
	
    public HangerStart() {
        super();
        requires(Robot.hanger);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
    	prefs = Preferences.getInstance();

		double speed = prefs.getDouble("hangerSpeed", 0.0);

		Robot.hanger.setClimberSpeed(speed);
    }

}
