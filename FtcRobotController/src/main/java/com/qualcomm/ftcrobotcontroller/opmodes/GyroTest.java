package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initialize();
		telemetry.addData("Stage: ", 0);
		waitForStart();
		telemetry.addData("Stage: ", 1);
		turnGyro(30, 0.25); // turn right 30 degrees
		telemetry.addData("Stage: ", 2);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 3);
		turnGyro(90, 0.25); // turn right 90 degrees
		telemetry.addData("Stage: ", 4);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 5);
		turnGyro(270, 0.25); // turn right 270 degrees
		telemetry.addData("Stage: ", 6);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 7);
		turnGyro(90, 0.25); // turn right 90 degrees
		telemetry.addData("Stage: ", 8);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 9);
	}
}
