package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class JetsonPowerOn extends CommandGroup {

    public JetsonPowerOn() {
    	addSequential(new JetsonPower(false));
    	addSequential(new Delay(5));
    	addSequential(new JetsonPower(true));
    }
}
