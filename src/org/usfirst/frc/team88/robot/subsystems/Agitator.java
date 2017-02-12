package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.AgitatorSendData;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Agitator extends Subsystem {
	private CANTalon agitatorMotor;

	public Agitator(){
		agitatorMotor = new CANTalon(RobotMap.agitatorMotor);
		agitatorMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		agitatorMotor.enableBrakeMode(false);
	}

	public void setSpeed(double speed){
		agitatorMotor.set(speed);
	}

	public void updateDashboard(){
		SmartDashboard.putNumber("AgitatorCurrent", agitatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("AgitatorVoltage", agitatorMotor.getOutputVoltage());
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new AgitatorSendData());
	}
}

