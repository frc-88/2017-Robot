package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearPusherIn2 extends CommandGroup {

    public GearPusherIn2() {
    	addSequential(new GearPusherIn());
    	addSequential(new Delay(0.2));
    	addSequential(new GearReceiverOut());
    }
}
