package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by ur mom on 1/31/2016.
 */
public class GyroTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		logging = true;
		initalize();
		waitForStart();

		turnG(90, 0.5);
		halt();
	}
}
