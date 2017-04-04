package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCenterLiftRed extends CommandGroup {

    public AutoCenterLiftRed() {
    	addSequential(new DriveZeroYaw());
    	addSequential(new AutoDeliverGear());
    	addSequential(new Delay(0.1));
    	addSequential(new DriveRotateToAngle(90));
    	addSequential(new Delay(0.2));
    	addSequential(new AutoShoot());
    }
}
