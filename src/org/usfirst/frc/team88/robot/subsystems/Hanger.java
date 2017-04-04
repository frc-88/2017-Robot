package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.HangerClimb;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Hanger
 * 
 *  knot just a frayed rope
 *  it will cling to the carding
 *  and we will all rise
 *  
 */
public class Hanger extends Subsystem {
	private CANTalon hangerMotor, hangerMotorFollower;
	private static final double HANGER_MAX_CURRENT = 50.0;
	private boolean override;
	private boolean isDone;
	
	NetworkTable robotTable;
	public Hanger(){
		hangerMotor = new CANTalon(RobotMap.hangerMotor);
		hangerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		hangerMotor.enableBrakeMode(true);
		
		hangerMotorFollower = new CANTalon(RobotMap.hangerMotorFollower);
		hangerMotorFollower.changeControlMode(CANTalon.TalonControlMode.Follower);
		hangerMotorFollower.set(RobotMap.hangerMotor);
		hangerMotorFollower.enableBrakeMode(true);
		
		override = false;
		isDone = false;
		robotTable = NetworkTable.getTable("robot");
	}
	
	public void setClimberSpeed(double speed){
		hangerMotor.set(speed);
	}
	
	public void stopClimber(){
		hangerMotor.set(0.0);
	}
	
	public void setOverride(boolean OR){
		override = OR;
	}
	
	public boolean getOverride(){
		return override;
	}
	
	public boolean isDone(){
		if(hangerMotor.getOutputCurrent() > HANGER_MAX_CURRENT){
			isDone = true;
		}
		return isDone;
	}
	public void updateDashboard(){
		SmartDashboard.putNumber("HangerMotorCurrent", hangerMotor.getOutputCurrent());
		SmartDashboard.putNumber("HangerMotorVoltage", hangerMotor.getOutputVoltage());
		
		robotTable.putBoolean("readyForTakeoff", isDone());
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new HangerClimb());
    }
}

