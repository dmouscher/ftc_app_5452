// DFNB: Drive to Floor, Near, Blue

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget((int) (5.7 * FT), 0.7, 5000);     // Move forward 5.7 feet
		turn(20, 0.7, 2000);                   // Turn left 20 degrees
		moveTarget(FT, 0.7, 10000);             // Move forward 1 foot
		//moveForward(FT, 0.7, 1000);
		dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}
