package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTest2 extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initialize();
		telemetry.addData("Stage: ", 0);
		waitForStart();
		telemetry.addData("Stage: ", 1);
		turnGyro2(30, 0.25);
		telemetry.addData("Stage: ", 2);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 3);
		turnGyro2(90, 0.25);
		telemetry.addData("Stage: ", 4);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 5);
		turnGyro2(270, 0.25);
		telemetry.addData("Stage: ", 6);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 7);
		turnGyro2(90, 0.25);
		telemetry.addData("Stage: ", 8);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 9);
	}
}
