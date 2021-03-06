package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveSplitArcade;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drive
 * 
 *  racing down the field
 *  stopping on a dime to get
 *       a bright yellow gear
 * 
 */
public class Drive extends Subsystem implements PIDOutput {
	// Constants
	private final static int LOW_PROFILE = 0;
	private final static double LOW_P = 1.0;
	private final static double LOW_I = 0.0;
	private final static double LOW_D = 0.0;
	private final static double LOW_F = 1.0;
	private final static int LOW_IZONE = 0;
	private final static double LOW_MAX = 500;

	private final static int HIGH_PROFILE = 1;
	private final static double HIGH_P = 0.0;
	private final static double HIGH_I = 0.0;
	private final static double HIGH_D = 0.0;
	private final static double HIGH_F = 0.7;
	private final static int HIGH_IZONE = 0;
	private final static double HIGH_MAX = 1000;

	private final static double RAMPRATE = 45;

	private final static double ROTATE_P = 0.0035;
	private final static double ROTATE_I = 0.00004;
	private final static double ROTATE_D = 0.0;
	private final static double ROTATE_F = 0.0;
	private final static double ROTATE_TOLERANCE = 3.0;
	
	private final static double ROTATE_BOILER_P = 0.003;
	private final static double ROTATE_BOILER_I = 0.00033;
	private final static double ROTATE_BOILER_D = 0.0;
	private final static double ROTATE_BOILER_F = 0.0;
	private final static double ROTATE_BOILER_TOLERANCE = 0.7;
	
	private final static double ROTATE_FAST_MAX = 0.4;
	private final static double ROTATE_SLOW_MAX = 0.15;
	private final static double ROTATE_MIN = 0.05;
	private final static double ROTATE_BOILER_MIN = 0.015;

	public final static double DFT_SENSITIVITY = 0.15;
	
	public PIDController rotateController;
	public PIDController rotateBoilerController;

	private final CANTalon[] lTalons, rTalons;
	private final DoubleSolenoid shifter;

	private AHRS navx;
	private final static double COLLISION_THRESHOLD = 0.5f;
	private double lastAccelX = 0;
	private double lastAccelY = 0;
	private double currentAccelX;
	private double currentAccelY;
	private double currentJerkX;
	private double currentJerkY;
	private double maxRotate;
	private boolean rotateBoilerMode = false;

	private double maxSpeed;
	private double targetMaxSpeed;
	private boolean autoShift;
	private boolean isGearFront;
	private CANTalon.TalonControlMode controlMode;
	NetworkTable robotTable;
	NetworkTable jetsonTable;
	DriverStation ds;

	public Drive() {
		ds = DriverStation.getInstance();

		// init talons
		// left talons are reversed for comp bot
		// right talons are reversed for practice bot
		lTalons = new CANTalon[RobotMap.driveLeft.length];
		initTalons(lTalons, RobotMap.driveLeft, true, RobotMap.isJetFuel, false);
		rTalons = new CANTalon[RobotMap.driveRight.length];
		initTalons(rTalons, RobotMap.driveRight, !RobotMap.isJetFuel, !RobotMap.isJetFuel, false);

		//setClosedLoopSpeed();
		setOpenLoop();

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
		
		rotateBoilerController = new PIDController(ROTATE_BOILER_P, ROTATE_BOILER_I, ROTATE_BOILER_D, ROTATE_BOILER_F, Robot.jetson, this);
		rotateBoilerController.setInputRange(-180.0f, 180.0f);
		rotateBoilerController.setOutputRange(-1.0, 1.0);
		rotateBoilerController.setAbsoluteTolerance(ROTATE_BOILER_TOLERANCE);
		rotateBoilerController.setContinuous(true);

		// init NetworkTables
		robotTable = NetworkTable.getTable("robot");
		jetsonTable = NetworkTable.getTable("imfeelinglucky");

		setRotateSpeedFast();
		setRotateModeNormal();
		isGearFront = true;
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
				talons[i].configEncoderCodesPerRev(420);
				talons[i].configNominalOutputVoltage(+0.0f, -0.0f);
				talons[i].configPeakOutputVoltage(+10.0f, -10.0f);
				talons[i].reverseSensor(reverseSensor);
				talons[i].reverseOutput(reverseOutput);
				talons[i].enableBrakeMode(brakeMode);
				talons[i].setVoltageRampRate(RAMPRATE);
				talons[i].setProfile(LOW_PROFILE);
			} else {
				// follower
				talons[i].changeControlMode(CANTalon.TalonControlMode.Follower);
				talons[i].set(talons[0].getDeviceID());
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
		double currentMaxSpeed;

		switch (controlMode) {
		case PercentVbus:
			lTalons[0].set(left);
			rTalons[0].set(-right);
			break;
		case Speed:
			currentMaxSpeed = getMaxSpeed();
			lTalons[0].set(left * currentMaxSpeed);
			rTalons[0].set(right * currentMaxSpeed);
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
		final double minimum = 0.02;
		final double minRange = 0.008;

		if (outputMagnitude == 0 && Math.abs(getAvgSpeed()) < 200 ) {
			if (curve < minRange && curve > -minRange) {
				curve = 0;
			} else if (curve < minimum && curve > 0) {
				curve = minimum;
			} else if (curve > -minimum && curve < 0) {
				curve = -minimum;
			}

			leftOutput = curve * (isLowGear() ? 0.5 : 1.0);
			rightOutput = -curve * (isLowGear() ? 0.5 : 1.0);
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

		setTarget(leftOutput, rightOutput);
	}

	private double getMaxSpeed() {
		// note that this adjusts maxSpeed every time it is called
		// and so should only be called once per setTarget call
		if (targetMaxSpeed > maxSpeed) {
			maxSpeed += 50;
			if (maxSpeed > targetMaxSpeed) {
				maxSpeed = targetMaxSpeed;
			}
		} else if (targetMaxSpeed < maxSpeed) {
			maxSpeed -= 50;
			if (maxSpeed < targetMaxSpeed) {
				maxSpeed = targetMaxSpeed;
			}
		}
		return maxSpeed;
	}

	public void resetDrive() {
		setTarget(0, 0);

		if (!isLowGear()) {
			shift();
		}
		maxSpeed = targetMaxSpeed;

		setClosedLoopSpeed();
		resetEncoders();
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

	public int getLeftEncPosition() {
		return lTalons[0].getEncPosition();
	}
	
	public int getRightEncPosition() {
		return rTalons[0].getEncPosition();
	}
	
	public double getAvgPosition() {
		return (lTalons[0].getPosition() + rTalons[0].getPosition()) / 2.0;
	}

	public double getLeftSpeed() {
		return lTalons[0].getSpeed();
	}

	public double getRightSpeed() {
		return rTalons[0].getSpeed();
	}

	public double getAvgSpeed() {
		double speed = (lTalons[0].getSpeed() + rTalons[0].getSpeed()) / 2;

		return speed;
	}

	public double getAvgCurrent() {
		return (lTalons[0].getOutputCurrent() + rTalons[0].getOutputCurrent()) / 2;
	}

	public boolean isAutoShift() {
		return autoShift;
	}

	public void toggleAutoShift() {
		autoShift = !autoShift;
	}
	
	public boolean isGearFront() {
		return isGearFront();
	}
	
	public void toggleFront() {
		isGearFront = !isGearFront;
	}

	public void enableBrakeMode(boolean value) {
		lTalons[0].enableBrakeMode(value);
		rTalons[0].enableBrakeMode(value);
	}

	public double getJerkX() {
		currentAccelX = navx.getWorldLinearAccelX();
		currentJerkX = currentAccelX - lastAccelX;
		lastAccelX = currentAccelX;
		return currentJerkX;
	}

	public double getJerkY() {
		currentAccelY = navx.getWorldLinearAccelY();
		currentJerkY = currentAccelY - lastAccelY;
		lastAccelY = currentAccelY;
		return currentJerkY;
	}

	public boolean collisionDetected() {

		return ((Math.abs(getJerkX()) > COLLISION_THRESHOLD) || (Math.abs(getJerkY()) > COLLISION_THRESHOLD));
	}

	public double getYaw() {
		return navx.getYaw();
	}

	public void zeroYaw() {
		navx.zeroYaw();
	}

	public void updateDashboard() {
		int i;

		SmartDashboard.putNumber("LeftPosition: ", lTalons[0].getPosition());
		SmartDashboard.putNumber("LeftEncPosition: ", lTalons[0].getEncPosition());
		SmartDashboard.putNumber("LeftEncVel: ", lTalons[0].getEncVelocity());
		SmartDashboard.putNumber("LeftSpeed: ", lTalons[0].getSpeed());
		SmartDashboard.putNumber("LeftSetPoint", lTalons[0].getSetpoint());
		SmartDashboard.putNumber("LeftError: ", lTalons[0].getClosedLoopError());
		for (i = 0; i < lTalons.length; i++) {
			SmartDashboard.putNumber("LeftCurrent" + i, lTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("LeftVoltage" + i, lTalons[i].getOutputVoltage());
		}

		SmartDashboard.putNumber("RightPosition: ", rTalons[0].getPosition());
		SmartDashboard.putNumber("RightEncPosition: ", rTalons[0].getEncPosition());
		SmartDashboard.putNumber("RightEncVel: ", rTalons[0].getEncVelocity());
		SmartDashboard.putNumber("RightSpeed: ", rTalons[0].getSpeed());
		SmartDashboard.putNumber("RightSetPoint", rTalons[0].getSetpoint());
		SmartDashboard.putNumber("RightError: ", rTalons[0].getClosedLoopError());
		for (i = 0; i < rTalons.length; i++) {
			SmartDashboard.putNumber("RightCurrent" + i, rTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("RightVoltage" + i, rTalons[i].getOutputVoltage());
		}

		SmartDashboard.putString("Speed", lTalons[0].getSpeed() + ":" + rTalons[0].getSpeed());
		SmartDashboard.putBoolean("lowGear", isLowGear());
		SmartDashboard.putBoolean("Autoshift", autoShift);

		// NavX stuff
		SmartDashboard.putBoolean("IMU_Connected", navx.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", navx.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", navx.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", navx.getPitch());
		SmartDashboard.putNumber("IMU_Roll", navx.getRoll());
		SmartDashboard.putNumber("Displacement_X", navx.getDisplacementX());
		SmartDashboard.putNumber("Displacement_Y", navx.getDisplacementY());
		SmartDashboard.putNumber("Jerk_Y", getJerkY());
		SmartDashboard.putNumber("Jerk_X", getJerkX());
		SmartDashboard.putBoolean("Collision_Detected", collisionDetected());
		
		SmartDashboard.putBoolean("Red?", ds.getAlliance() == DriverStation.Alliance.Red);

		robotTable.putBoolean("inLow", isLowGear());
		robotTable.putBoolean("climbing", isClimbing());
		robotTable.putBoolean("collision", collisionDetected());
		robotTable.putBoolean("lessthan20", twentySecondsLeft());
	}

	public boolean isClimbing() {
		return Math.abs(navx.getPitch()) > 10.0;
	}
	
	public boolean twentySecondsLeft() {
		DriverStation driverStation = DriverStation.getInstance();

		double timeLeft = driverStation.getMatchTime();

		if (timeLeft <= 20) {
			return true;
		} else {
			return false;
		}
	}

	public void setRotateSpeedSlow(){
		maxRotate = ROTATE_SLOW_MAX;
	}
	
	public void setRotateSpeedFast(){
		maxRotate = ROTATE_FAST_MAX;
	}

	public void setRotateModeNormal(){
		rotateBoilerMode = false;
	}
	
	public void setRotateModeBoiler(){
		rotateBoilerMode = true;
	}
	
	@Override
	public void pidWrite(double output) {
		double minRotate = rotateBoilerMode ? ROTATE_BOILER_MIN : ROTATE_MIN;
		
		if (output > maxRotate) {
			output = maxRotate;
		} else if (output > minRotate) {
			// no change
		} else if (output > 0) {
			output = minRotate;
		} else if (output == 0) {
			output = 0;
		} else if (output > (0 - minRotate)) {
			output = 0 - minRotate;
		} else if (output < (0 - maxRotate)) {
			output = 0 - maxRotate;
		}

		if (rotateBoilerMode) {
			setTarget(-output, output);
		} else {
			setTarget(output, -output);
		}

		updateDashboard();
	}

	public void initDefaultCommand() {
		//setDefaultCommand(new DriveSplitArcade());
		setDefaultCommand(new DriveTank());
	}

}
