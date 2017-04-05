package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class FuelFlap extends Subsystem {
	DoubleSolenoid flap;
	
	public FuelFlap() {
		flap = new DoubleSolenoid(RobotMap.fuelFlapSolenoidIn, RobotMap.fuelFlapSolenoidOut);
		flap.set(Value.kForward);
	}
	
	
	public void flapIn(){
		flap.set(Value.kForward);
	}
	
	public void flapOut(){
		flap.set(Value.kReverse);
	}
	
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

