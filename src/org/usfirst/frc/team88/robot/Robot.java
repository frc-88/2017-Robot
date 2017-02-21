
package org.usfirst.frc.team88.robot;

import org.usfirst.frc.team88.robot.commands.*;
import org.usfirst.frc.team88.robot.subsystems.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static Drive drive;
	public static Shooter shooter;
	public static Jetson jetson;
	public static Intake intake;
	public static Hanger hanger;
	public static Gearage gearage;
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		drive = new Drive();
		shooter = new Shooter();
		jetson = new Jetson();
		intake = new Intake();
		hanger = new Hanger();
		gearage = new Gearage();
		oi = new OI();
		
		chooser.addDefault("40 Ball", new Auto40Ball());
		chooser.addObject("Drive Forward", new DriveDistance(2.0));
		
		SmartDashboard.putData("Auto mode", chooser);

        SmartDashboard.putData(Scheduler.getInstance());

        SmartDashboard.putData("Drive Tank", new DriveTank());
		SmartDashboard.putData("Drive Field Orientated", new DriveFieldOrientated());
		SmartDashboard.putData("Drive Split Arcade", new DriveSplitArcade());
		SmartDashboard.putData("Toggle Autoshift", new DriveToggleAutoShift());
		SmartDashboard.putData("Manual Shift", new DriveShift());
	
		SmartDashboard.putData("Drive Distance", new DriveDistance());
		SmartDashboard.putData("Drive Distance Arc", new DriveDistanceArc());
	
		SmartDashboard.putData("Zero Yaw", new DriveZeroYaw());
		SmartDashboard.putData("Rotate to 0", new DriveRotateToAngle(0.0));
		SmartDashboard.putData("Rotate to 90", new DriveRotateToAngle(90.0));
		SmartDashboard.putData("Rotate to 180", new DriveRotateToAngle(180.0));
		SmartDashboard.putData("Rotate to -90", new DriveRotateToAngle(-90.0));
		SmartDashboard.putData("Rotate to Boiler", new DriveRotateToBoiler());
		
		SmartDashboard.putData("Turn Left 90", new DriveTurnLeft90());
		SmartDashboard.putData("Turn Right 90", new DriveTurnRight90());

		SmartDashboard.putData("Jetson On", new JetsonPowerOn());
		SmartDashboard.putData("Jetson Off", new JetsonPowerOffSsh());
		
		SmartDashboard.putData("Start Flywheel", new ShooterStartFlywheel());
		SmartDashboard.putData("Start Feeder", new ShooterStartFeeder());
		SmartDashboard.putData("Start Agitator", new ShooterStartAgitator());
		SmartDashboard.putData("Stop Flywheel", new ShooterStopFlywheel());
		SmartDashboard.putData("Stop Feeder", new ShooterStopFeeder());
		SmartDashboard.putData("Stop Agitator", new ShooterStopAgitator());
		SmartDashboard.putData("Set Hood", new ShooterSetHood());
		SmartDashboard.putData("Stop Shooter", new ShooterStopAll());
		
		SmartDashboard.putData("Start Intake Motor", new IntakeStart());
		SmartDashboard.putData("Stop Intake Motor", new IntakeStop());
		SmartDashboard.putData("Intake In", new IntakeIn());
		SmartDashboard.putData("Intake Out", new IntakeOut());
		
		SmartDashboard.putData("Hanger Start", new HangerStart());
		SmartDashboard.putData("Hanger Stop", new HangerStop());
		
		SmartDashboard.putData("Gear Pusher In", new GearPusherIn());
		SmartDashboard.putData("Gear Pusher Out", new GearPusherOut());
		SmartDashboard.putData("Gear Receiver In", new GearReceiverIn());
		SmartDashboard.putData("Gear Receiver Out", new GearReceiverOut());
		SmartDashboard.putData("Gear Eject",new GearEject());
		
		SmartDashboard.putData("40 Ball Auto", new Auto40Ball());
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Command disabledCommand = new ShutDownAll();
		
		disabledCommand.start();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
