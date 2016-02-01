package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initalize();
		waitForStart();

		turnG(90, 0.5);
		halt();
	}
}
