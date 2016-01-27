// DFNR = Dump from Floor, Near, Red

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jackiehirsch on 12/14/15.
 */
public class DFNR extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initalize();
		waitForStart();

		dropperBase.setPosition(0.518);   // Raise up the climber-dropper
		movePlow(0.75, 9500);             // Extend the plow
		moveEn((int)(5.7 * FT), 0.7); // Move forward 5.7 feet
		turn(-20, 0.7, 2000);             // Turn left 20 degrees
		moveEn((int)(FT), 0.7);      // Move forward one foot
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);   // Dump
		Thread.sleep(1000);
	}
}