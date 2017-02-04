package org.usfirst.frc.team88.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Jetson extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private DigitalOutput power;
	
	public Jetson(){
		power = new DigitalOutput(0);
		power.set(true);
	}
	
	public void setPower(boolean value) {
		power.set(value);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

