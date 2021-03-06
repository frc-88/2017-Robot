package org.usfirst.frc.team88.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static final boolean isJetFuel = false;
	
	// Drive
	public static final int[] driveLeft = {13, 9, 10, 15};
	public static final int[] driveRight = {12, 8, 11, 14};
	public static final int shifterSolenoidLow = isJetFuel ? 4 : 5;
	public static final int shifterSolenoidHigh = isJetFuel ? 5 : 4;
	
	//Shooter
	public static final int flywheelMotor = 0;
	public static final int flywheelMotorFollower = isJetFuel ? 3 : 5;
	public static final int feederMotor = 4;
	public static final int hoodServo = 0;
	
	//Agitators
	public static final int agitatorMotor = 2;
	
	//Hanger
	public static final int hangerMotor = 1;
	public static final int hangerMotorFollower = isJetFuel ? 5 : 3;
	
	//Gearage
	public static final int pusherSolenoidIn = 1;
	public static final int pusherSolenoidOut = 0;
	public static final int receiverSolenoidIn = isJetFuel ? 7 : 6;
	public static final int receiverSolenoidOut = isJetFuel ? 6 : 7;

	// FuelFlap
	public static final int fuelFlapSolenoidIn = 3;
	public static final int fuelFlapSolenoidOut = 2;
}
