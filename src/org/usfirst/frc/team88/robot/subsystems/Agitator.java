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
	private CANTalon agitatorMotor1;
	private CANTalon agitatorMotor2;

	public Agitator(){
		agitatorMotor1 = new CANTalon(RobotMap.agitatorMotors[0]);
		agitatorMotor1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		agitatorMotor1.enableBrakeMode(false);

		agitatorMotor2 = new CANTalon(RobotMap.agitatorMotors[1]);
		agitatorMotor2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		agitatorMotor2.enableBrakeMode(false);
	}

	public void setMotor1Speed(double speed){
		agitatorMotor1.set(speed);
	}

	public void setMotor2Speed(double speed){
		agitatorMotor2.set(speed);
	}
	
	public void setMotorsSpeed(double motor1Speed, double motor2Speed){
		agitatorMotor1.set(motor1Speed);
		agitatorMotor2.set(motor2Speed);
	}
	
	public void stopMotor1(){
		agitatorMotor1.set(0.0);
	}
	
	public void stopMotor2(){
		agitatorMotor2.set(0.0);
	}
	
	public void updateDashboard(){
		SmartDashboard.putNumber("AgitatorMotor1Current", agitatorMotor1.getOutputCurrent());
		SmartDashboard.putNumber("AgitatorMotor1Voltage", agitatorMotor1.getOutputVoltage());
		
		SmartDashboard.putNumber("AgitatorMotor2Current", agitatorMotor2.getOutputCurrent());
		SmartDashboard.putNumber("AgitatorMotor2Voltage", agitatorMotor2.getOutputVoltage());
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new AgitatorSendData());
	}
}

