
package org.usfirst.frc.team88.robot;

import org.usfirst.frc.team88.robot.commands.*;
import org.usfirst.frc.team88.robot.subsystems.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	public static Hanger hanger;
	public static Gearage gearage;
	public static FuelFlap fuelFlap;
	public static OI oi;

	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		jetson = new Jetson();

		drive = new Drive();
		shooter = new Shooter();
		hanger = new Hanger();
		gearage = new Gearage();
		fuelFlap = new FuelFlap();
		
		oi = new OI();

		SmartDashboard.putData(Scheduler.getInstance());

		SmartDashboard.putData("Drive Tank", new DriveTank());
		SmartDashboard.putData("Drive Field Orientated", new DriveFieldOrientated());
		SmartDashboard.putData("Drive Split Arcade", new DriveSplitArcade());
		SmartDashboard.putData("Toggle Autoshift", new DriveToggleAutoShift());
		SmartDashboard.putData("Manual Shift", new DriveShift());

		SmartDashboard.putData("Drive Distance", new DriveDistance());
		SmartDashboard.putData("Drive Distance Arc", new DriveDistanceArc());
		SmartDashboard.putData("DDAC", new AutoBoilerLiftRed());
		SmartDashboard.putData("Drive Deliver Gear", new DriveDeliverGear());
		SmartDashboard.putData("Drive Wiggle", new DriveWiggle());
		SmartDashboard.putData("Drive Scan", new DriveScanForGear(-160.0));

		SmartDashboard.putData("Zero Yaw", new DriveZeroYaw());
		SmartDashboard.putData("Rotate to 0", new DriveRotateToAngle(0.0));
		SmartDashboard.putData("Rotate to 90", new DriveRotateToAngle(90.0));
		SmartDashboard.putData("Rotate to 180", new DriveRotateToAngle(180.0));
		SmartDashboard.putData("Rotate to -90", new DriveRotateToAngle(-90.0));
		SmartDashboard.putData("Rotate to Boiler", new DriveRotateToBoiler());
		SmartDashboard.putData("Drive to Boiler", new DriveToBoiler());

		SmartDashboard.putData("Turn Left 90 v2", new DriveRotateToAngle2(-90.0));
		SmartDashboard.putData("Turn Right 90 v2", new DriveRotateToAngle2(90.0));

		SmartDashboard.putData("Turn Left 30", new DriveTurn(-30));
		SmartDashboard.putData("Turn Right 30", new DriveTurn(30));

		SmartDashboard.putData("Start Flywheel", new ShooterStartFlywheel());
		SmartDashboard.putData("Start Feeder", new ShooterStartFeeder());
		SmartDashboard.putData("Start Agitator", new ShooterStartAgitator());
		SmartDashboard.putData("Stop Flywheel", new ShooterStopFlywheel());
		SmartDashboard.putData("Stop Feeder", new ShooterStopFeeder());
		SmartDashboard.putData("Stop Agitator", new ShooterStopAgitator());
		SmartDashboard.putData("Set Hood", new ShooterSetHood());
		SmartDashboard.putData("Stop Shooter", new ShooterStopAll());
		SmartDashboard.putData("Test Shooter", new ShooterStartFlywheel(false));

		SmartDashboard.putData("Hanger Start", new HangerStart());
		SmartDashboard.putData("Hanger Stop", new HangerStop());

		SmartDashboard.putData("Gear Pusher In", new GearPusherIn2());
		SmartDashboard.putData("Gear Pusher Out", new GearPusherOut2());
		SmartDashboard.putData("Gear Receiver In", new GearReceiverIn());
		SmartDashboard.putData("Gear Receiver Out", new GearReceiverOut());
		SmartDashboard.putData("Gear Eject", new GearEject());
		
		SmartDashboard.putData("Fuel Flap In", new FuelFlapIn());
		SmartDashboard.putData("Fuel Flap Out", new FuelFlapOut());

		SmartDashboard.putData("40 Ball Auto", new Auto40Ball());
		SmartDashboard.putData("Boiler Lift Auto B", new AutoBoilerLiftBlue());
		SmartDashboard.putData("Boiler Lift Auto R", new AutoBoilerLiftRed());
		SmartDashboard.putData("Center Lift Auto B", new AutoCenterLiftBlue());
		SmartDashboard.putData("Center Lift Auto R", new AutoCenterLiftRed());
		SmartDashboard.putData("Far Lift Auto B", new AutoFarLiftBlue());
		SmartDashboard.putData("Far Lift Auto R", new AutoFarLiftRed());
		SmartDashboard.putData("Hopper Auto R", new AutoHopperHitRed());
		SmartDashboard.putData("Hopper Auto B", new AutoHopperHitBlue());
		SmartDashboard.putData("Dead Reckon", new AutoDeliverGearNoVision());
		
		SmartDashboard.putData("JetsonView", new JetsonSwapView());
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		jetson.disableImage();
		shooter.setAgitator(0);
		shooter.setFeeder(0);
		shooter.setFlywheel(0);
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
		jetson.enableImage();
		// autonomousCommand = chooser.getSelected();

		// autonomousCommand = new Auto40Ball();
		// autonomousCommand = new AutoDeliverGear();
		// autonomousCommand = new AutoFarLiftBlue();
		// autonomousCommand = new AutoFarLiftRed();
		// autonomousCommand = new AutoBoilerLiftBlue();
		// autonomousCommand = new AutoBoilerLiftRed();
		// autonomousCommand = new AutoCenterLiftBlue();
		// autonomousCommand = new AutoCenterLiftRed();
		autonomousCommand = new AutoDeliverGearNoVision();
		
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
		shooter.setHood(0.42);
		jetson.enableImage();
		Robot.gearage.pusherIn();
		
		
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
