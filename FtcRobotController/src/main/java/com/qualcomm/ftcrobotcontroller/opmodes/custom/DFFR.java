package com.qualcomm.ftcrobotcontroller.opmodes.custom;

/**
 * Created by jackiehirsch on 12/14/15.
 */
public class DFFR extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initalize();
		waitForStart();

		dropperBase.setPosition(0.518);   // Raise up the climber-dropper
		movePlow(0.75, 9500);           // Extend the plow
		moveEn((int)(8 * FT), 0.7); // Move forwards 8 feet
		turn(-20, 0.7, 3000);           // Turn 20 degrees left
		moveEn((int)(3 * FT), 0.7); // Move forwards 3 feet
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1); // Dump
		Thread.sleep(1000);
	}
}