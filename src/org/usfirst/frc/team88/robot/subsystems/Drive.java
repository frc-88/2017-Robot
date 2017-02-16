package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
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

	private final static double ROTATE_P = 0.002;
	private final static double ROTATE_I = 0.00004;
	private final static double ROTATE_D = 0.0;
	private final static double ROTATE_F = 0.0;
	private final static double ROTATE_TOLERANCE = 2.0;
	private final static double ROTATE_MAX = 0.2;
	private final static double ROTATE_MIN = 0.03;

	public final static double DFT_SENSITIVITY = 0.2;
	public PIDController rotateController;

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

	private double maxSpeed;
	private double targetMaxSpeed;
	private boolean autoShift;
	private CANTalon.TalonControlMode controlMode;
	NetworkTable robotTable;
	NetworkTable jetsonTable;

	public Drive() {
		// init talons
		// left talons are reversed for comp bot
		// right talons are reversed for practice bot
		lTalons = new CANTalon[RobotMap.driveLeft.length];
		initTalons(lTalons, RobotMap.driveLeft, true, true, false);
		rTalons = new CANTalon[RobotMap.driveRight.length];
		initTalons(rTalons, RobotMap.driveRight, true, false, false);
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

		// init NetworkTables
		robotTable = NetworkTable.getTable("robot");
		jetsonTable = NetworkTable.getTable("imfeelinglucky");

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

		setTarget(leftOutput, rightOutput);
	}

	private double getMaxSpeed() {
		// note that this adjusts maxSpeed every time it is called
		// and so should only be called once per setTarget call
		if (targetMaxSpeed > maxSpeed) {
			maxSpeed += 50;
		} else if (targetMaxSpeed < maxSpeed) {
			maxSpeed -= 0;
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

	public double getJerkX(){
		currentAccelX = navx.getWorldLinearAccelX();
		currentJerkX = currentAccelX - lastAccelX;
		lastAccelX = currentAccelX;
		return currentJerkX;
	}

	public double getJerkY(){
		currentAccelY = navx.getWorldLinearAccelY();
		currentJerkY = currentAccelY - lastAccelY;
		lastAccelY = currentAccelY;
		return currentJerkY;
	}

	public boolean collisionDetected(){

		return ((Math.abs(getJerkX()) > COLLISION_THRESHOLD) ||
				(Math.abs(getJerkY()) > COLLISION_THRESHOLD));
	}

	public double getYaw() {
		return navx.getYaw();
	}

	public void zeroYaw(){
		navx.zeroYaw();
	}

	public double getDistance() {
		return jetsonTable.getNumber("Distance",0.0);
	}

	public double getAngle() {
		return jetsonTable.getNumber("Angle",0.0);
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
		SmartDashboard.putNumber("maxSpeed", maxSpeed);
		SmartDashboard.putBoolean("lowGear", isLowGear());

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

		SmartDashboard.putString("Speed", lTalons[0].getSpeed() + ":" + rTalons[0].getSpeed());

		//robotTable.putNumber("driveAvgCurrent", getAvgCurrent());
		robotTable.putBoolean("inLow", isLowGear());
		robotTable.putBoolean("collision", collisionDetected());
		if(twentySecondsLeft()){
			robotTable.putString("sound", "JohnStewart");
		}
		if(inRange(0.0, 0.0)){//Need to get the distance and angle from the Jetson
			robotTable.putString("sound", "turret-target"); //Need to get a file and insert the name here
		}
		SmartDashboard.putNumber("J Distance", jetsonTable.getNumber("Distance",0.0));
		SmartDashboard.putNumber("J Angle", jetsonTable.getNumber("Angle",0.0));
	}

	public boolean inRange(double distance, double angle){
		final double RANGE = 100.0;
		final double FIRING_ARC = 90.0;

		if(distance <= RANGE && Math.abs(angle) <= FIRING_ARC){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean twentySecondsLeft(){
		DriverStation driverStation = DriverStation.getInstance();

		double timeLeft = driverStation.getMatchTime();

		if(timeLeft<=20){
			return true;
		}
		else{
			return false;
		}
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

		updateDashboard();
		setTarget(output, -output);	
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveTank());
	}



}
