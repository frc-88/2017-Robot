package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class JetsonPowerOn extends CommandGroup {

    public JetsonPowerOn() {
    	addSequential(new Delay(10));
    	addSequential(new JetsonPower(false));
    	addSequential(new Delay(0.2));
    	addSequential(new JetsonPower(true));
    	addSequential(new Delay(0.2));
    	addSequential(new JetsonPower(false));
    	addSequential(new Delay(2.0));
    	addSequential(new JetsonPower(true));
    }
}
