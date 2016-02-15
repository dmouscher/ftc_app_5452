package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTestB extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initialize();
		waitForStart();

		//moveEn(1440*2, 0.8); // just to make sure this works

		//turnG(90, 0.5);
		//halt();

		//Thread.sleep(1000);

		//turnGT(-90, 0.5);

		turnB("Left", 90, 0.5);
		halt();

		Thread.sleep(1000);

		turnB("Right", 90, 0.5);
		halt();

		Thread.sleep(3000);

		turnB("Left", 90, 0.5);
		halt();

		Thread.sleep(3000);

		turnB("Left", 90, 0.5);
		halt();

		Thread.sleep(3000);
	}
}
