package org.usfirst.frc.team88.robot.subsystems;

//import com.jcabi.ssh.SSHByPassword;
//import com.jcabi.ssh.Shell;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

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
	NetworkTable jetsonTable;
	private DigitalOutput power;

	public Jetson() {
		power = new DigitalOutput(0);
		power.set(true);
		jetsonTable = NetworkTable.getTable("imfeelinglucky");

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
	
	public void powerOff() throws Exception {

	//	Shell shell = new SSHByPassword("vision-frc88.local", 22, "ubuntu", "ubuntu");

		//new Shell.Plain(shell).exec("sudo shutdown");
	}
	
	public void setCam(double cam){
		jetsonTable.putNumber("visionFeed", cam);
	}
	
	public double getCam(){
		return jetsonTable.getNumber("visionFeed");
	}
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
