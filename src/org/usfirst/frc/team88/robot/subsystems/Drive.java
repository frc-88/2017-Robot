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

	private double getMaxSpeed() {
		if (targetMaxSpeed > maxSpeed) {
			maxSpeed++;
		} else if (targetMaxSpeed < maxSpeed) {
			maxSpeed--;
		}
		return maxSpeed;
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
