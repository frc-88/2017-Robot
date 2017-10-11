package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.SwitchesData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Switches extends Subsystem {

	DigitalInput switch1;
	DigitalInput switch2;
	DigitalInput switch3;
	DigitalInput switch4;
	DigitalInput switch5;
	DigitalInput switch6;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public Switches() {
		switch1 = new DigitalInput(RobotMap.switch1);
		switch2 = new DigitalInput(RobotMap.switch2);
		switch3 = new DigitalInput(RobotMap.switch3);
		switch4 = new DigitalInput(RobotMap.switch4);
		switch5 = new DigitalInput(RobotMap.switch5);
		switch6 = new DigitalInput(RobotMap.switch6);
	}

	public boolean isred() {
		return switch1.get();
	}

	public boolean boilerside() {
		return switch2.get() && (!switch3.get());
	}

	public boolean farside() {
		return switch3.get() && (!switch2.get());
	}

	public boolean center() {
		return switch2.get() && switch3.get();
	}

	public boolean shoot() {
		return switch4.get();
	}

	public void updateDashboard() {
		if (RobotMap.debugMode) {
			SmartDashboard.putBoolean("switch1", switch1.get());
			SmartDashboard.putBoolean("switch2", switch2.get());
			SmartDashboard.putBoolean("switch3", switch3.get());
			SmartDashboard.putBoolean("switch4", switch4.get());
			SmartDashboard.putBoolean("switch5", switch5.get());
			SmartDashboard.putBoolean("switch6", switch6.get());
		}
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new SwitchesData());
	}
}
