package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * FuelFlap
 * 
 * if we are ever
 * running hopper hit auto
 * fuel flap will be key
 * 
 */
public class FuelFlapOut extends InstantCommand {

    public FuelFlapOut() {
        super();
        requires(Robot.fuelFlap);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.fuelFlap.flapOut();
    }
}
