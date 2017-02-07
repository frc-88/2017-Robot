package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class JetsonPowerOff extends CommandGroup {

    public JetsonPowerOff() {
    	addSequential(new JetsonPower(false));
    	addSequential(new Delay(10));
    	addSequential(new JetsonPower(true));
    }
}
