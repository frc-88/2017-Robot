package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class FuelFlapIn extends InstantCommand {

    public FuelFlapIn() {
        super();
        requires(Robot.fuelFlap);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.fuelFlap.flapOut();
    }

}
