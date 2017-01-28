package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.ShooterHood;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {

	private Servo hood;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public Shooter()
	{
		hood = new Servo(RobotMap.shooterServo);
	}
	
	public void setHood(double target) {
		SmartDashboard.putNumber("hood", hood.get());
		SmartDashboard.putNumber("hoodIn",target);
		hood.set(1-target);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ShooterHood());
    }
}

