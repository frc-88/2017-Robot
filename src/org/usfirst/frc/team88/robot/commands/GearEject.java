package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearEject extends CommandGroup {

    public GearEject() {
    	addSequential(new GearPusherOut());
    	//Delay Should be played with to make the pusher out be longer so as to get onto the peg with one press
    	addSequential(new Delay(0.125));
    	addSequential(new GearPusherIn());
    }
}
