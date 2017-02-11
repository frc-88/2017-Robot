package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.ShooterSendData;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {

	private static final double FLY_P = 0.0;
	private static final double FLY_I = 0.0;
	private static final double FLY_D = 0.0;
	private static final double FLY_F = 3.0;
	private static final double FEEDER_P = 0.0;
	private static final double FEEDER_I = 0.0;
	private static final double FEEDER_D = 0.0;
	private static final double FEEDER_F = 3.0;
	private static final double HOOD_INIT = 0.5;

	private CANTalon flywheelTalon, feederTalon;
	private Servo hoodServo;

	public Shooter() {
		// initialize flywheel
		flywheelTalon = new CANTalon(RobotMap.flywheelMotor);
		flywheelTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		flywheelTalon.configEncoderCodesPerRev(80);
		flywheelTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		flywheelTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		flywheelTalon.reverseSensor(false);
		flywheelTalon.reverseOutput(false);
		flywheelTalon.enableBrakeMode(false);
		flywheelTalon.setPID(FLY_P, FLY_I, FLY_D, FLY_F, 0, 0, 0);
		flywheelTalon.changeControlMode(CANTalon.TalonControlMode.Speed);

		// initialize feeder
		feederTalon = new CANTalon(RobotMap.feederMotor);
		feederTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		feederTalon.configEncoderCodesPerRev(80);
		feederTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		feederTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		feederTalon.reverseSensor(false);
		feederTalon.reverseOutput(false);
		feederTalon.enableBrakeMode(false);
		feederTalon.setPID(FEEDER_P, FEEDER_I, FEEDER_D, FEEDER_F, 0, 0, 0);
		feederTalon.changeControlMode(CANTalon.TalonControlMode.Speed);

		// initialize hood
		hoodServo = new Servo(RobotMap.hoodServo);
		setHood(HOOD_INIT);
	}

	public void setFlywheel(double target) {
		flywheelTalon.set(target);
	}

	public void setFeeder(double target) {
		feederTalon.set(target);
	}

	public void setHood(double target) {
		hoodServo.set(target);
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
		
		SmartDashboard.putNumber("HoodPosition", hoodServo.getPosition());
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterSendData());
	}
}
