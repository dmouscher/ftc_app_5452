// DFNB = Dump from Floor, Near, Blue

package com.qualcomm.ftcrobotcontroller.opmodes.custom;


/**
 * Created by jackiehirsch on 12/14/15.
 */
public class DFNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initalize();
		waitForStart();

		dropperBase.setPosition(0.518);   // Raise up the climber-dropper
		movePlow(0.75, 9500);             // Extend the plow
		moveEn((int)(5.7 * FT), 0.7); // Move forward 5.7 feet
		moveEn((int)(FT), 0.7);      // Move forward 1 foot
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);   // Dump
		Thread.sleep(1000);
	}
}
