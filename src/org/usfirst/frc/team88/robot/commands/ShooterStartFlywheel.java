package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterStartFlywheel extends InstantCommand {
	private boolean dynamicSpeed;
	
	public ShooterStartFlywheel(boolean useDynamic) {
		super();
		requires(Robot.shooter);
		
		dynamicSpeed = useDynamic;
	}
	
	public ShooterStartFlywheel() {
		this(false);
	}

	// Called once when the command executes
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();
		double distance = Robot.jetson.getBoilerDistance();
		double speed = 0.0;
		
		if (!dynamicSpeed) {
			speed = prefs.getDouble("flywheelSpeed", 0.0);
		} else if (distance < 0) {
			speed = prefs.getDouble("flywheelSpeed", 0.0);
		} else if (distance < 40) {
			speed = 700;
		} else if (distance < 50) {
			speed = 750;
		} else if (distance < 60) {
			speed = 800;
		} else if (distance < 70) {
			speed = 850;
		}

		Robot.shooter.setFlywheel(speed);
	}
}
