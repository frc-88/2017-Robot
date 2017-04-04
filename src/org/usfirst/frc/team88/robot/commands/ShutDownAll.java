package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShutDownAll extends CommandGroup {

    public ShutDownAll() {
    	// Drive
    	addSequential(new DriveStop());
    	// Gearage
    	addSequential(new GearReceiverIn());
    	// Hanger
    	addSequential(new HangerStop());
    	// Intake
    	addSequential(new IntakeStop());
    	// Jetson
    	
    	// Shooter
    	addSequential(new ShooterStopAll());
    }
}
