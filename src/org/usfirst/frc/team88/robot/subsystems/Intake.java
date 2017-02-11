package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	private CANTalon intakeMotor;
	private DoubleSolenoid slider;
	
	public Intake(){
		intakeMotor = new CANTalon(RobotMap.intakeMotor);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		intakeMotor.enableBrakeMode(false);
		
		slider = new DoubleSolenoid(RobotMap.sliderSolenoidIn, RobotMap.sliderSolenoidOut);
		slider.set(Value.kForward);
	}
	
	public void setIntakeSpeed(double speed){
		intakeMotor.set(speed);
	}
	
	public void sliderIn(){
		slider.set(Value.kForward);
	}
	
	public void sliderOut(){
		slider.set(Value.kReverse);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

