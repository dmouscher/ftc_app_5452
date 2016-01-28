// DFNB = Drive to Floor, Near, Blue

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

public class DFNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initalize();
		waitForStart();

		Global.ranAutonomous = true;
		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, 9500);                   // Extend the plow
		moveEn((int)(5.7*FT), 0.7);             // Move forward 5.7 feet
		moveEn((int)(    FT), 0.7);             // Move forward 1 foot
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}
