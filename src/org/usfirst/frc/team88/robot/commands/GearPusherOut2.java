package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearPusherOut2 extends CommandGroup {

    public GearPusherOut2() {
    	addSequential(new GearReceiverIn());
    	addSequential(new Delay(0.2));
    	addSequential(new GearPusherOut());
    }
}
