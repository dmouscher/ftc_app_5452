package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initialize();
		waitForStart();

		turnGyro(-90, 0.5);
		Thread.sleep(1000);
		turnGyro(90, 0.5);
		Thread.sleep(1000);
		turnGyro(-90, 0.5);
		Thread.sleep(1000);
		turnGyro(-90, 0.5);
		Thread.sleep(1000);
	}
}
