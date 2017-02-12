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
	public static final int shifterSolenoidLow = 4;
	public static final int shifterSolenoidHigh = 5;
	
	//Shooter
	public static final int flywheelMotor = 0;
	public static final int feederMotor = 4;
	public static final int hoodServo = 0;
	
	//Intake
	public static final int intakeMotor = 5;
	public static final int sliderSolenoidIn = 2;
	public static final int sliderSolenoidOut = 3;
	
	//Agitators
	public static final int agitatorMotor = 2;   //might be 3
	
	//Hanger
	public static final int hangerMotor = 1;
	
	//Gear Pusher
	public static final int pusherSolenoidIn = 0;
	public static final int pusherSolenoidOut = 1;
}
