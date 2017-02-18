package org.usfirst.frc.team88.robot.subsystems;

//import com.jcabi.ssh.SSHByPassword;
//import com.jcabi.ssh.Shell;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Jetson extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private DigitalOutput power;

	public Jetson() {
		power = new DigitalOutput(0);
		power.set(true);
		
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
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
