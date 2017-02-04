package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.subsystems.Player;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class PlaySound extends InstantCommand {

    public PlaySound(String u) {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.player);
        Player player = new Player(u);
    }

    // Called once when the command executes
    protected void initialize() {
    }

}
