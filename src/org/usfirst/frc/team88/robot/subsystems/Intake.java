package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.IntakeSendData;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		intakeMotor.setVoltageRampRate(30);
		
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
	
	public void updateDashboard(){
		SmartDashboard.putNumber("IntakeCurrent", intakeMotor.getOutputCurrent());
		SmartDashboard.putNumber("IntakeVoltage", intakeMotor.getOutputVoltage());
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new IntakeSendData());
    }
}

