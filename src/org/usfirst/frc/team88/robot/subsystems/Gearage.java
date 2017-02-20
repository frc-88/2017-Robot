package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Gearage extends Subsystem {
	DoubleSolenoid pusher, receiver;
	
	public Gearage(){
		pusher = new DoubleSolenoid(RobotMap.pusherSolenoidIn, RobotMap.pusherSolenoidOut);
		receiver = new DoubleSolenoid(RobotMap.receiverSolenoidIn, RobotMap.receiverSolenoidOut);
		pusher.set(Value.kForward);
		receiver.set(Value.kForward);
	}
	
	public void pusherIn(){
		pusher.set(Value.kForward);
	}
	
	public void pusherOut(){
		pusher.set(Value.kReverse);
	}
	
	public void receiverIn(){
		receiver.set(Value.kForward);
	}
	
	public void receiverOut(){
		receiver.set(Value.kReverse);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

