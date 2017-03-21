package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCenterLiftRed extends CommandGroup {

    public AutoCenterLiftRed() {
    	addSequential(new AutoDeliverGear());
    	addSequential(new DriveRotateToAngle(-90));
    	addSequential(new AutoShoot());
    }
}
