package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {
	DoubleSolenoid pusher;
	
	public GearPusher(){
		pusher = new DoubleSolenoid(RobotMap.pusherSolenoidIn, RobotMap.pusherSolenoidOut);
		pusher.set(Value.kForward);
	}
	
	public void pusherIn(){
		pusher.set(Value.kForward);
	}
	
	public void pusherOut(){
		pusher.set(Value.kReverse);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

