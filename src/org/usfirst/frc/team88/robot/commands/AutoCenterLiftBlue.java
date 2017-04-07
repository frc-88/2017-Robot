package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCenterLiftBlue extends CommandGroup {

    public AutoCenterLiftBlue() {
    	addSequential(new DriveZeroYaw());
    	addSequential(new AutoDeliverGear());
    	addSequential(new Delay(0.1));
    	addSequential(new DriveRotateToAngle(85));
    	addSequential(new Delay(0.1));
    	addSequential(new DriveDistance(2.0));
    	addSequential(new Delay(0.4));
    	addSequential(new AutoShoot());
    }
}
