package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class JetsonSwapCam extends InstantCommand {

	private final int CAMMAX = 4;
	private final int CAMMIN = 1;
	private final int[] COMPLIST = {1, 3};
	private int count = 0;

	public JetsonSwapCam() {
		super();
		requires(Robot.jetson);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		int cam = Robot.jetson.getCam();

		if(Robot.jetson.isDebug()){
			if(cam == CAMMAX){
				cam = CAMMIN;
			} else{
				cam++;
			}
		}else{
			cam = COMPLIST[count];
			count++;
			if(count == COMPLIST.length){
				count = 0;
			}
		}
		Robot.jetson.setCam(cam);
	}
}
