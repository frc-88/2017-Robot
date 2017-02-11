package org.usfirst.frc.team88.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Drive
	public static final int[] driveLeft = {13, 9, 10, 15};
	public static final int[] driveRight = {12, 8, 11, 14};
	
	//Intake
	public static final int intakeMotor = 5;
	
	//Shooter
	public static final int shooterMotor = 0;
	public static final int feederMotor = 4;
	
	//Hanger
	public static final int hangerMotor = 1;
	
	//Agitators
	public static final int[] agitatorMotors = {2, 3}; 
	
	public static final int shifterSolenoidLow = 0;
	public static final int shifterSolenoidHigh = 1;

	public static int shooterServo = 0;
}
