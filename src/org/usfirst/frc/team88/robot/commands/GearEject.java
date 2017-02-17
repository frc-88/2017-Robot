package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearEject extends CommandGroup {

    public GearEject() {
    	addSequential(new GearPusherOut());
    	addSequential(new Delay(0.13));
    	addSequential(new GearPusherIn());
    }
}
