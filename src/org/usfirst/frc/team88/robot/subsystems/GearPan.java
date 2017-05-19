package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.GearPanUpdateSmartDashboard;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a cool pan It makes gears go in and out Hopefully it works.
 */
public class GearPan extends Subsystem {
	private Talon intakeMotor;
	private DoubleSolenoid pan;
	private PowerDistributionPanel pdp;
	private static final double INTAKE_MAX_CURRENT = 10.0;
	private static final int IS_DONE_MAX = 5;
	private int isDone;

	public GearPan() {
		pdp = new PowerDistributionPanel();
		intakeMotor = new Talon(RobotMap.intakeMotor);

		pan = new DoubleSolenoid(RobotMap.panSolenoidIn, RobotMap.panSolenoidOut);
		pan.set(Value.kReverse);

		isDone = 0;
	}

	public void intakeSpeed(double speed) {
		intakeMotor.set(speed);
	}

	public void intakeStop() {
		intakeMotor.set(0.0);
	}

	public void resetIsDone() {
		isDone = 0;
	}

	public boolean isDone() {
		if (pdp.getCurrent(RobotMap.intakeChanel) > INTAKE_MAX_CURRENT) {
			isDone++;
		} else {
			isDone = 0;
		}
		return isDone > IS_DONE_MAX;
	}

	public void panIn() {
		pan.set(Value.kReverse);
	}

	public void panOut() {
		pan.set(Value.kForward);
	}

	public void updateDashboard() {
		if (RobotMap.debugMode) {
			SmartDashboard.putNumber("IntakeMotorCurrent", pdp.getCurrent(RobotMap.intakeChanel));
		}
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new GearPanUpdateSmartDashboard());
	}
}
