package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.ShooterSendData;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Shooter
 * 
 *  look what we have here
 *  a tank that is full of fuel
 *  we should make it rain
 *  
 */
public class Shooter extends Subsystem {

	private static final double FLY_P = 13.5;
	private static final double FLY_I = 0.00;
	private static final double FLY_D = 2000.0;
	private static final double FLY_F = 1.78;
	private static final double FLY_THRESHOLD = 50.0;
	
	private static final double FEEDER_P = 0.05;
	private static final double FEEDER_I = 0.0;
	private static final double FEEDER_D = 0.0;
	private static final double FEEDER_F = 0.078;
	
	private static final double HOOD_INIT = 0.5;
	private static final double HOOD_THRESHOLD = 0.05;

	private CANTalon flywheelTalon, feederTalon, agitatorTalon;
	private Servo hoodServo;

	public Shooter() {
		// initialize flywheel
		flywheelTalon = new CANTalon(RobotMap.flywheelMotor);
		flywheelTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		flywheelTalon.configEncoderCodesPerRev(80);
		flywheelTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		flywheelTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		flywheelTalon.reverseSensor(true);
		flywheelTalon.reverseOutput(true);
		flywheelTalon.enableBrakeMode(false);
		flywheelTalon.setPID(FLY_P, FLY_I, FLY_D, FLY_F, 0, 0, 0);
		flywheelTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
		flywheelTalon.setVoltageRampRate(60.0);

		// initialize feeder
		feederTalon = new CANTalon(RobotMap.feederMotor);
		feederTalon.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		feederTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		feederTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		feederTalon.reverseSensor(true);
		feederTalon.reverseOutput(false);
		feederTalon.enableBrakeMode(false);
		feederTalon.setPID(FEEDER_P, FEEDER_I, FEEDER_D, FEEDER_F, 0, 0, 0);
		feederTalon.changeControlMode(CANTalon.TalonControlMode.Speed);

		// initialize agitator
		agitatorTalon = new CANTalon(RobotMap.agitatorMotor);
		agitatorTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		agitatorTalon.enableBrakeMode(false);

		// initialize hood
		hoodServo = new Servo(RobotMap.hoodServo);
		setHood(HOOD_INIT);
	}

	public void setFlywheel(double target) {
		if (target == 0.0) {
			flywheelTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		} else {
			flywheelTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
		}
		flywheelTalon.set(target);
	}

	public void setFeeder(double target) {
		feederTalon.set(target);
	}
	
	public void setAgitator(double speed){
		agitatorTalon.set(speed);
	}
	
	public void setHood(double target) {
		// TODO add min and max for our top and bottom limits
		final double HOOD_MAX = 1.0; //Nonsense
		final double HOOD_MIN = 0.0; //Nonsense
		
		if(target>HOOD_MAX){
			target = HOOD_MAX;
		}
		else if(target<HOOD_MIN){
			target = HOOD_MIN;
		}
		
		hoodServo.set(target);
	}
	
	public boolean flywheelOnTarget() {
		return (Math.abs(flywheelTalon.getSetpoint() - flywheelTalon.getSpeed()) < FLY_THRESHOLD);
	}
	
	public boolean hoodOnTarget(double target) {
		return (Math.abs(hoodServo.getPosition() - target) < HOOD_THRESHOLD);
	}

	public void updateDashboard() {
		SmartDashboard.putNumber("FlywheelCurrent", flywheelTalon.getOutputCurrent());
		SmartDashboard.putNumber("FlywheelVoltage", flywheelTalon.getOutputVoltage());
		SmartDashboard.putNumber("FlywheelEncVelocity", flywheelTalon.getEncVelocity());
		SmartDashboard.putNumber("FlywheelSpeed", flywheelTalon.getSpeed());
		SmartDashboard.putNumber("FlywheelSetPoint", flywheelTalon.getSetpoint());
		SmartDashboard.putNumber("FlywheelError", flywheelTalon.getError());
		SmartDashboard.putNumber("FlywheelPosition", flywheelTalon.getPosition());

		SmartDashboard.putNumber("FeederCurrent", feederTalon.getOutputCurrent());
		SmartDashboard.putNumber("FeederVoltage", feederTalon.getOutputVoltage());
		SmartDashboard.putNumber("FeederEncVelocity", feederTalon.getEncVelocity());
		SmartDashboard.putNumber("FeederSpeed", feederTalon.getSpeed());
		SmartDashboard.putNumber("FeederSetPoint", feederTalon.getSetpoint());
		SmartDashboard.putNumber("FeederError", feederTalon.getError());
		SmartDashboard.putNumber("FeederPosition", feederTalon.getPosition());

		SmartDashboard.putNumber("AgitatorCurrent", agitatorTalon.getOutputCurrent());
		SmartDashboard.putNumber("AgitatorVoltage", agitatorTalon.getOutputVoltage());

		SmartDashboard.putNumber("HoodPosition", hoodServo.getPosition());
		
		SmartDashboard.putString("ShooterSpeed", flywheelTalon.getSpeed() + ":" + feederTalon.getSpeed());
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterSendData());
	}
}
