package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.GearPanUpdateSmartDashboard;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a cool pan
 * It makes gears go in and out
 * Hopefully it works.
 */
public class GearPan extends Subsystem {
	private Talon intakeMotor;
	private DoubleSolenoid pan;
	private PowerDistributionPanel pdp;
	private static final double INTAKE_MAX_CURRENT = 50.0;
	private boolean isDone;
	
	public GearPan(){
		pdp = new PowerDistributionPanel();
		intakeMotor = new Talon(RobotMap.intakeMotor);
		
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
		if(pdp.getCurrent(RobotMap.intakeChanel) > INTAKE_MAX_CURRENT){
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
		SmartDashboard.putNumber("IntakeMotorCurrent", pdp.getCurrent(RobotMap.intakeChanel));
		
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new GearPanUpdateSmartDashboard());
    }
}

