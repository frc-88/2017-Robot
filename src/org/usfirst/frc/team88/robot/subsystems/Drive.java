package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {
	// Constants
	private final static int LOW_PROFILE = 0;
	private final static double LOW_P = 0.065;
	private final static double LOW_I = 0.0;
	private final static double LOW_D = 0.0;
	private final static double LOW_F = 0.6;
	private final static int LOW_IZONE = 0;
	private final static double LOW_MAX = 600;

	private final static int HIGH_PROFILE = 1;
	private final static double HIGH_P = 0.065;
	private final static double HIGH_I = 0.0;
	private final static double HIGH_D = 0.0;
	private final static double HIGH_F = 0.6;
	private final static int HIGH_IZONE = 0;
	private final static double HIGH_MAX = 1400;

	private final static double RAMPRATE = 60;

	private final static double ROTATE_P = 0.007;
	private final static double ROTATE_I = 0.00003;
	private final static double ROTATE_D = 0.0;
	private final static double ROTATE_F = 0.0;
	private final static double ROTATE_TOLERANCE = 3.0;
	private final static double ROTATE_MAX = 0.5;
	private final static double ROTATE_MIN = 0.06;

	public final static double DFT_SENSITIVITY = 0.2;

	private final CANTalon[] lTalons, rTalons;
	private final DoubleSolenoid shifter;
	private AHRS navx;
	private PIDController rotateController;

	private double maxSpeed;
	private double targetMaxSpeed;
	private boolean autoShift;
	private CANTalon.TalonControlMode controlMode;
	
	public Drive() {
		// init talons
		lTalons = new CANTalon[RobotMap.driveLeft.length];
		initTalons(lTalons, RobotMap.driveLeft, false, true, false);
		rTalons = new CANTalon[RobotMap.driveLeft.length];
		initTalons(rTalons, RobotMap.driveRight, false, true, false);
		setClosedLoopSpeed();
		
		// init shifter
		shifter = new DoubleSolenoid(RobotMap.shifterSolenoidLow, RobotMap.shifterSolenoidHigh);
		shifter.set(Value.kForward);
		autoShift = true;
		maxSpeed = LOW_MAX;
		targetMaxSpeed = LOW_MAX;

		// init navx
		navx = new AHRS(SerialPort.Port.kMXP);

		// init rotateController
		rotateController = new PIDController(ROTATE_P, ROTATE_I, ROTATE_D, ROTATE_F, navx, this);
		rotateController.setInputRange(-180.0f, 180.0f);
		rotateController.setOutputRange(-1.0, 1.0);
		rotateController.setAbsoluteTolerance(ROTATE_TOLERANCE);
		rotateController.setContinuous(true);
		
	}

	private void initTalons(CANTalon[] talons, int[] ids, boolean reverseSensor, boolean reverseOutput,
			boolean brakeMode) {
		for (int i = 0; i < ids.length; i++) {
			talons[i] = new CANTalon(ids[i]);
			if (i == 0) {
				// master
				talons[i].setPID(LOW_P, LOW_I, LOW_D, LOW_F, LOW_IZONE, RAMPRATE, LOW_PROFILE);
				talons[i].setPID(HIGH_P, HIGH_I, HIGH_D, HIGH_F, HIGH_IZONE, RAMPRATE, HIGH_PROFILE);
				talons[i].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				talons[i].configEncoderCodesPerRev(360);
				talons[i].configNominalOutputVoltage(+0.0f, -0.0f);
				talons[i].configPeakOutputVoltage(+12.0f, -12.0f);
				talons[i].reverseSensor(reverseSensor);
				talons[i].reverseOutput(reverseOutput);
				talons[i].enableBrakeMode(brakeMode);
				talons[i].setVoltageRampRate(RAMPRATE);
				talons[i].setProfile(LOW_PROFILE);
			} else {
				// follower
				talons[i].changeControlMode(CANTalon.TalonControlMode.Follower);
				talons[i].set(lTalons[0].getDeviceID());
				talons[i].enableBrakeMode(brakeMode);
			}
		}

	}

	public void setOpenLoop() {
		controlMode = CANTalon.TalonControlMode.PercentVbus;

		lTalons[0].changeControlMode(controlMode);
		rTalons[0].changeControlMode(controlMode);
	}

	public void setClosedLoopSpeed() {
		controlMode = CANTalon.TalonControlMode.Speed;

		lTalons[0].changeControlMode(controlMode);
		rTalons[0].changeControlMode(controlMode);
	}
	
	public void setTarget(double left, double right) {
		switch (controlMode) {
		case PercentVbus:
			lTalons[0].set(left);
			rTalons[0].set(-right);
			break;
		case Speed:
			lTalons[0].set(left * getMaxSpeed());
			rTalons[0].set(right * getMaxSpeed());
			break;
		case Position:
		case Disabled:
		default:
			break;
		}
	}

	/**
	 * The below was based on (ie, copied from) very similar code in the WPILib
	 * RobotDrive class on 1/18/2017
	 * 
	 * Drive the motors at "outputMagnitude" and "curve". Both outputMagnitude
	 * and curve are -1.0 to +1.0 values, where 0.0 represents stopped and not
	 * turning. {@literal curve < 0 will turn left
	 * and curve > 0} will turn right.
	 *
	 * <p>
	 * The algorithm for steering provides a constant turn radius for any normal
	 * speed range, both forward and backward. Increasing sensitivity causes
	 * sharper turns for fixed values of curve.
	 *
	 * <p>
	 * This function will most likely be used in an autonomous routine.
	 *
	 * @param outputMagnitude
	 *            The speed setting for the outside wheel in a turn, forward or
	 *            backwards, +1 to -1.
	 * @param curve
	 *            The rate of turn, constant for different forward speeds. Set
	 *            {@literal
	 *                        curve < 0 for left turn or curve > 0 for right turn.}
	 *            Set curve = e^(-r/w) to get a turn radius r for wheelbase w of
	 *            your robot. Conversely, turn radius r = -ln(curve)*w for a
	 *            given value of curve and wheelbase w.
	 */
	public void driveCurve(double outputMagnitude, double curve) {
		driveCurve(outputMagnitude, curve, DFT_SENSITIVITY);

	}

	public void driveCurve(double outputMagnitude, double curve, double sensitivity) {
		final double leftOutput;
		final double rightOutput;
		final double minimum = 0.15;
		final double minRange = 0.008;

		if (outputMagnitude == 0) {
			if(curve < minRange && curve > -minRange){
				curve = 0;
			}
			else if(curve < minimum && curve > 0){
				curve = minimum;
			}
			else if(curve > -minimum && curve < 0){
				curve = -minimum;
			}
			SmartDashboard.putNumber("The Curve Value", curve);
			leftOutput = curve;
			rightOutput = -curve;
		} else if (curve < 0) {
			double value = Math.log(-curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0) {
			double value = Math.log(curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else {
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}

		updateDashboard();
		setTarget(leftOutput, rightOutput);
	}

	private double getMaxSpeed() {
		if (targetMaxSpeed > maxSpeed) {
			maxSpeed++;
		} else if (targetMaxSpeed < maxSpeed) {
			maxSpeed--;
		}
		return maxSpeed;
	}

	public void shift() {
		if (shifter.get() == Value.kForward) {
			shifter.set(Value.kReverse);
			targetMaxSpeed = HIGH_MAX;
			lTalons[0].setProfile(HIGH_PROFILE);
			rTalons[0].setProfile(HIGH_PROFILE);
		} else {
			shifter.set(Value.kForward);
			targetMaxSpeed = LOW_MAX;
			lTalons[0].setProfile(LOW_PROFILE);
			rTalons[0].setProfile(LOW_PROFILE);
		}
	}

	public boolean isLowGear() {
		if (shifter.get() == Value.kForward) {
			return true;
		} else
			return false;
	}

	public void disableRampRate() {
		lTalons[0].setVoltageRampRate(0.0);
		rTalons[0].setVoltageRampRate(0.0);
	}

	public void enableRampRate() {
		lTalons[0].setVoltageRampRate(RAMPRATE);
		rTalons[0].setVoltageRampRate(RAMPRATE);
	}


	public void resetEncoders() {
		lTalons[0].setPosition(0);
		rTalons[0].setPosition(0);
	}

	public double getAvgPosition() {
		return (lTalons[0].getPosition() + rTalons[0].getPosition()) / 2.0;
	}

	public double getAvgSpeed() {
		double speed = (lTalons[0].getSpeed() + rTalons[0].getSpeed()) / 2;

		return speed;
	}

	public boolean isAutoShift() {
		return autoShift;
	}

	public void toggleAutoShift() {
		autoShift = !autoShift;
	}

	public double getYaw() {
		return navx.getYaw();
	}
	
	public void zeroYaw(){
		navx.zeroYaw();
	}

	public void updateDashboard() {
		SmartDashboard.putNumber("LeftPosition: ", lTalons[0].getPosition());
		SmartDashboard.putNumber("LeftEncVel: ", lTalons[0].getEncVelocity());
		SmartDashboard.putNumber("LeftSpeed: ", lTalons[0].getSpeed());
		SmartDashboard.putNumber("LeftSetPoint", lTalons[0].getSetpoint());
		SmartDashboard.putNumber("LeftError: ", lTalons[0].getClosedLoopError());
		SmartDashboard.putNumber("LeftCurrent", lTalons[0].getOutputCurrent());
		SmartDashboard.putNumber("LeftVoltage", lTalons[0].getOutputVoltage());

		SmartDashboard.putNumber("RightPosition: ", rTalons[0].getPosition());
		SmartDashboard.putNumber("RightEncVel: ", rTalons[0].getEncVelocity());
		SmartDashboard.putNumber("RightSpeed: ", rTalons[0].getSpeed());
		SmartDashboard.putNumber("RightSetPoint", rTalons[0].getSetpoint());
		SmartDashboard.putNumber("RightError: ", rTalons[0].getClosedLoopError());
		SmartDashboard.putNumber("RightCurrent", rTalons[0].getOutputCurrent());
		SmartDashboard.putNumber("RightVoltage", rTalons[0].getOutputVoltage());

		SmartDashboard.putNumber("targetMaxspeed", targetMaxSpeed);
		SmartDashboard.putNumber("maxSpeed", getMaxSpeed());
		SmartDashboard.putBoolean("lowGear", isLowGear());

		// NavX stuff
		SmartDashboard.putBoolean("IMU_Connected", navx.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", navx.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", navx.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", navx.getPitch());
		SmartDashboard.putNumber("IMU_Roll", navx.getRoll());
		SmartDashboard.putNumber("Displacement_X", navx.getDisplacementX());
		SmartDashboard.putNumber("Displacement_Y", navx.getDisplacementY());
	}
	
	@Override
	public void pidWrite(double output) {
		if (output > ROTATE_MAX) {
			output = ROTATE_MAX;
		} else if (output > ROTATE_MIN) {
			// no change
		} else if (output > 0) {
			output = ROTATE_MIN;
		} else if (output == 0) {
			output = 0;
		} else if (output > (0 - ROTATE_MIN)) {
			output = 0 - ROTATE_MIN;
		} else if (output < (0 - ROTATE_MAX)) {
			output = 0 - ROTATE_MAX;
		}

		setTarget(output, -output);	
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

}
