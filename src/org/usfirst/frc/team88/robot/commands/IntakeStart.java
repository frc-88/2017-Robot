package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class IntakeStart extends InstantCommand {

	private Preferences prefs;
	
    public IntakeStart() {
        super();
        requires(Robot.intake);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
    	prefs = Preferences.getInstance();

		double speed = prefs.getDouble("intakeSpeed", 0.0);

		Robot.intake.setIntakeSpeed(speed);
    }

}
