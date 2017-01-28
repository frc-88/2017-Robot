package org.usfirst.frc.team88.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Drive
	public static final int[] driveLeft = {1, 2, 3, 4};
	public static final int[] driveRight = {5, 6, 7, 8};
	
	public static final int shifterSolenoidLow = 0;
	public static final int shifterSolenoidHigh = 1;
}
