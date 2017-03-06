package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.commands.JetsonUpdateSmartDashboard;

//import com.jcabi.ssh.SSHByPassword;
//import com.jcabi.ssh.Shell;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Jetson
 * 
 *  sure it can manage
 *  more than one teraflops but
 *  can it fry an egg?
 *  
 */
public class Jetson extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private final static double BOILER_RANGE = 12.0;
	private final static double BOILER_TOLERANCE = 1.0;
	private final static double GEAR_RANGE = 90.0;
	private final static double GEAR_TOLERANCE = 15.0;
	
	Preferences prefs;
	NetworkTable jetsonTable;
	private DigitalOutput power;
	private int camNum;
	private boolean targeting;
	NetworkTable robotTable;

	public Jetson() {
		prefs = Preferences.getInstance();
		power = new DigitalOutput(0);
		power.set(true);
		robotTable = NetworkTable.getTable("robot");
		jetsonTable = NetworkTable.getTable("imfeelinglucky");
		targeting = false;
		camNum = 1;
		
		jetsonTable.putNumber("visionFeed", camNum);
		powerOn();
	}

	public void setPower(boolean value) {
		power.set(value);
	}

	public void powerOn() {
		try {
			Thread.sleep(5000);
			power.set(false);
			Thread.sleep(200);
			power.set(true);
			Thread.sleep(200);
			power.set(false);
			Thread.sleep(2000);
			power.set(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateSmartdashboard(){
		//robotTable.putBoolean("boilerLock", boilerInRange());
		robotTable.putBoolean("gearLock", gearInRange());
		robotTable.putBoolean("VisionReady", jetsonTable.getBoolean("VisionReady", false));
		
		SmartDashboard.putNumber("J DistanceB", jetsonTable.getNumber("DistanceB", -1.0));
		SmartDashboard.putNumber("J AngleB", jetsonTable.getNumber("AngleB", 0.0));
		SmartDashboard.putNumber("J DistanceG", jetsonTable.getNumber("DistanceG", -1.0));
		SmartDashboard.putNumber("J Theta", jetsonTable.getNumber("Theta", 0.0));
		SmartDashboard.putNumber("J Gamma", jetsonTable.getNumber("Gamma", 0.0));
		SmartDashboard.putNumber("J DistanceH", jetsonTable.getNumber("DistanceH", -1.0));
		SmartDashboard.putNumber("J Beta", jetsonTable.getNumber("Beta", 0.0));
		SmartDashboard.putNumber("Jetson Cam", getCam());
		SmartDashboard.putBoolean("Gear Lock", gearInRange());
		SmartDashboard.putBoolean("Chute Lock", chuteInRange());
		SmartDashboard.putBoolean("Targeting Activated", targeting);
		SmartDashboard.putBoolean("Vision Ready", jetsonTable.getBoolean("VisionReady", false));

		
		jetsonTable.putNumber("visionBH", prefs.getDouble("visionGH", -1.0));
		jetsonTable.putNumber("visionBS", prefs.getDouble("visionGS", -1.0));
		jetsonTable.putNumber("visionBV", prefs.getDouble("visionGV", -1.0));
		jetsonTable.putNumber("visionGH", prefs.getDouble("visionGH", -1.0));
		jetsonTable.putNumber("visionGS", prefs.getDouble("visionGS", -1.0));
		jetsonTable.putNumber("visionGV", prefs.getDouble("visionGV", -1.0));
		jetsonTable.putBoolean("camSwitch", prefs.getBoolean("camSwitch", true));
	}

	public void activateTarget(){
		robotTable.putString("sound", "ping");
		targeting = true;
	}

	public void deactivateTarget(){
		robotTable.putString("sound", "pong");
		targeting = false;
	}

	public boolean isTargetActive(){
		return targeting;
	}
	
	public boolean isDebug(){
		return prefs.getBoolean("visionIsDebug", false);
	}

	public void powerOff() throws Exception {

		//	Shell shell = new SSHByPassword("vision-frc88.local", 22, "ubuntu", "ubuntu");

		//new Shell.Plain(shell).exec("sudo shutdown");
	}

	private boolean boilerInRange() {
		double distance = getBoilerDistance();

		return ((distance != -1.0) && (Math.abs(distance - BOILER_RANGE) <= BOILER_TOLERANCE) && isTargetActive());
	}

	private boolean gearInRange() {
		double distance = getGearDistance();
		double gamma = getGearGamma();

		return ((distance > 18.0) && (Math.abs(gamma) <= GEAR_TOLERANCE) && (distance <= GEAR_RANGE) && isTargetActive());
	}

	private boolean chuteInRange() {
		double distance = getChuteDistance();
		double angle = getChuteAngle();

		return ((distance > 15.0) && (Math.abs(angle) <= GEAR_TOLERANCE) && (distance <= GEAR_RANGE) && isTargetActive());
	}
	public void setCam(int cam){
		camNum = cam;
		jetsonTable.putNumber("visionFeed", camNum);
		if(camNum < 3){
			robotTable.putBoolean("isBoiler", true);
		}else{
			robotTable.putBoolean("isBoiler", false);
		}
	}


	public double getBoilerDistance() {
		return jetsonTable.getNumber("DistanceB", -1.0);
	}

	public double getBoilerAngle() {
		return getBoilerDistance() < 0 ? 0.0 : jetsonTable.getNumber("AngleB", 0.0);
	}

	public double getGearDistance() {
		return jetsonTable.getNumber("DistanceG", -1.0);
	}

	public double getGearGamma() {
		return getGearDistance() < 0 ? 0.0 : jetsonTable.getNumber("Gamma", 0.0);
	}

	public double getGearTheta() {
		return getGearDistance() < 0 ? 0.0 : jetsonTable.getNumber("Theta", 0.0);
	}

	public double getChuteDistance() {
		return jetsonTable.getNumber("DistanceH", -1.0);
	}

	public double getChuteAngle() {
		return getChuteDistance() < 0 ? 0.0 : jetsonTable.getNumber("Beta", 0.0);
	}

	public int getCam(){
		return camNum;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new JetsonUpdateSmartDashboard());
	}
}
