package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.HangerSendData;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Hanger extends Subsystem {
	private CANTalon hangerMotor;
	private final double currentDraw = 30.0;
	NetworkTable robotTable;
	public Hanger(){
		hangerMotor = new CANTalon(RobotMap.hangerMotor);
		hangerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		hangerMotor.enableBrakeMode(true);
		robotTable = NetworkTable.getTable("robot");
	}
	
	public void setClimberSpeed(double speed){
		hangerMotor.set(speed);
	}
	
	public void stopClimber(){
		hangerMotor.set(0.0);
	}
	
	public void updateDashboard(){
		SmartDashboard.putNumber("HangerMotorCurrent", hangerMotor.getOutputCurrent());
		SmartDashboard.putNumber("HangerMotorVoltage", hangerMotor.getOutputVoltage());
		if(hangerMotor.getOutputCurrent() > currentDraw){
			robotTable.putString("sound", "prepare-flight");
		}
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new HangerSendData());
    }
}

