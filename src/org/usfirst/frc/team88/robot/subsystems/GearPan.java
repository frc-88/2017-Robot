package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.GearPanUpdateSmartDashboard;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a cool pan
 * It makes gears go in and out
 * Hopefully it works.
 */
public class GearPan extends Subsystem {
	private CANTalon intakeMotor;
	private DoubleSolenoid pan;
	private static final double INTAKE_MAX_CURRENT = 50.0;
	private boolean isDone;
	
	public GearPan(){
		intakeMotor = new CANTalon(RobotMap.intakeMotor);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		intakeMotor.enableBrakeMode(true);
		
		pan = new DoubleSolenoid(RobotMap.panSolenoidIn, RobotMap.panSolenoidOut);
		pan.set(Value.kReverse);
		
		isDone = false;
	}
	
	public void intakeSpeed(double speed){
		intakeMotor.set(speed);
	}
	
	public void intakeStop(){
		intakeMotor.set(0.0);
	}
	
	public void resetIsDone(){
		isDone = false;
	}
	
	public boolean isDone(){
		if(intakeMotor.getOutputCurrent() > INTAKE_MAX_CURRENT){
			isDone = true;
		}
		return isDone;
	}
	
	public void panIn(){
		pan.set(Value.kReverse);
	}
	
	public void panOut(){
		pan.set(Value.kForward);
	}

	public void updateDashboard(){
		SmartDashboard.putNumber("IntakeMotorCurrent", intakeMotor.getOutputCurrent());
		SmartDashboard.putNumber("IntakeMotorVoltage", intakeMotor.getOutputVoltage());
		
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new GearPanUpdateSmartDashboard());
    }
}

