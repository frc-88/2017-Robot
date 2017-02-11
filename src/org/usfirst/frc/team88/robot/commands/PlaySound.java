package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class PlaySound extends InstantCommand {
	private String sound;
	NetworkTable robotTable;
    public PlaySound(String sound) {
        super();
        this.sound = sound;
        robotTable = NetworkTable.getTable("robot");
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
			robotTable.putString("sound", sound);
    }

}
