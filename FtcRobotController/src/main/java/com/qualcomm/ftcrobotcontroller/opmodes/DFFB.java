// DFFB: Drive to Floor, Far, Blue

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFFB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initialize();
		waitForStart();

		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget(8.5*FT, 0.7, 5000);             // Move 8.5 feet forwards
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}
