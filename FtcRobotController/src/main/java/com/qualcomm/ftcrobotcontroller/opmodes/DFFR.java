// DFFB: Drive to Floor, Far, Red

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFFR extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initalize();
		waitForStart();

		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, 9500);                   // Extend the plow
		moveEn((int)(8*FT), 0.7);               // Move forwards 8 feet
		turn(-20, 0.7, 3000);                   // Turn 20 degrees left
		moveEn((int)(3*FT), 0.7);               // Move forwards 3 feet
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}