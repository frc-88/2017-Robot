//package org.usfirst.frc.team88.robot.commands;
//
//import org.usfirst.frc.team88.robot.Robot;
//
//import edu.wpi.first.wpilibj.command.Command;
//import jaci.pathfinder.Pathfinder;
//import jaci.pathfinder.Trajectory;
//import jaci.pathfinder.Waypoint;
//import jaci.pathfinder.followers.EncoderFollower;
//import jaci.pathfinder.modifiers.TankModifier;
//
///**
// *
// */
//public class DriveDeliverGear2 extends Command {
//	private static final double TIME_STEP = 0.02;
//	private static final double MAX_VELOCITY = 5.0;
//	private static final double MAX_ACCELERATION = 1.0;
//	private static final double MAX_JERK = 40.0;
//
//	private EncoderFollower left, right;
//	double yawOffset;
//
//	public DriveDeliverGear2() {
//		requires(Robot.drive);
//	}
//
//	// Called just before this Command runs the first time
//	protected void initialize() {
//		double distance = Robot.drive.getGearDistance();
//		double gamma = Robot.drive.getGearGamma();
//		double theta = Robot.drive.getGearTheta();
//
//		Robot.drive.disableRampRate();
//
//		yawOffset = Robot.drive.getYaw() + theta;
//
//		// Create the Trajectory Configuration
//		//
//		// Arguments:
//		// Fit Method: HERMITE_CUBIC or HERMITE_QUINTIC
//		// Sample Count: SAMPLES_HIGH (100 000)
//		// SAMPLES_LOW (10 000)
//		// SAMPLES_FAST (1 000)
//		// Time Step: 0.05 Seconds
//		// Max Velocity: 1.7 m/s
//		// Max Acceleration: 2.0 m/s/s
//		// Max Jerk: 60.0 m/s/s/s
//		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
//				Trajectory.Config.SAMPLES_HIGH, TIME_STEP, MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK);
//		Waypoint[] points = new Waypoint[] {
//				new Waypoint(-distance * Math.cos(Math.toRadians(theta - gamma)),
//						distance * Math.sin(Math.toRadians(theta - gamma)), theta),
//				new Waypoint(0, -2, 0), new Waypoint(0, -0.75, 0) };
//
//		Trajectory trajectory = Pathfinder.generate(points, config);
//
//		// Wheelbase Width = 0.5m
//		TankModifier modifier = new TankModifier(trajectory).modify(0.5);
//
//		left = new EncoderFollower(modifier.getLeftTrajectory());
//		right = new EncoderFollower(modifier.getRightTrajectory());
//
//		// Encoder Position is the current, cumulative position of your encoder.
//		// If you're using an SRX, this will be the 'getEncPosition' function.
//		// 1000 is the amount of encoder ticks per full revolution
//		// Wheel Diameter is the diameter of your wheels (or pulley for a track
//		// system) in meters
//		left.configureEncoder(Robot.drive.getLeftEncPosition(), 410, 4 / 12);
//		right.configureEncoder(Robot.drive.getRightEncPosition(), 410, 4 / 12);
//
//		// The first argument is the proportional gain. Usually this will be quite high
//		// The second argument is the integral gain. This is unused for motion profiling
//		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
//		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the
//		// trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
//		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
//		left.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
//		right.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
//	}
//
//	// Called repeatedly when this Command is scheduled to run
//	protected void execute() {
//		double l = left.calculate(Robot.drive.getLeftEncPosition());
//		double r = right.calculate(Robot.drive.getRightEncPosition());
//
//		double gyro_heading = Robot.drive.getYaw() - yawOffset; // Assuming the gyro is giving a value in degrees
//		double desired_heading = Pathfinder.r2d(left.getHeading()); // Should also be in degrees
//
//		double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
//		double turn = 0.8 * (-1.0 / 80.0) * angleDifference;
//
//		// invert paths because we are driving backwards
//		Robot.drive.setTarget( -r - turn, -l + turn);
//	}
//
//	// Make this return true when this Command no longer needs to run execute()
//	protected boolean isFinished() {
//		return left.isFinished() && right.isFinished();
//	}
//
//	// Called once after isFinished returns true
//	protected void end() {
//		Robot.drive.enableRampRate();
//	}
//
//	// Called when another command which requires one or more of the same
//	// subsystems is scheduled to run
//	protected void interrupted() {
//		Robot.drive.enableRampRate();
//	}
//}
