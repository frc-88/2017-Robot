package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShutDownAll extends CommandGroup {

    public ShutDownAll() {
    	// Drive
    	addParallel(new DriveStop());
    	// Gearage
    	addParallel(new GearReceiverIn());
    	// Hanger
    	addParallel(new HangerStop());
    	// Intake
    	addParallel(new IntakeStop());
    	// Jetson
    	
    	// Shooter
    	addSequential(new ShooterStopAll());
    }
}
