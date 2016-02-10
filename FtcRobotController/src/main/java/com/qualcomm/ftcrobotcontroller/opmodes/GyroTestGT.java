package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTestGT extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initalize();
		waitForStart();

		//moveEn(1440*2, 0.8); // just to make sure this works

		//turnG(90, 0.5);
		//halt();

		//Thread.sleep(1000);

		//turnGT(-90, 0.5);
		telemetry.addData("Turns:", 0);
		turnGT(-90, 0.5);
		halt();
		telemetry.addData("Turns:", 1);
		Thread.sleep(1000);


		turnGT(90, 0.5);
		halt();
		telemetry.addData("Turns:", 2);
		Thread.sleep(3000);

		turnGT(-90, 0.5);
		halt();
		telemetry.addData("Turns:", 3);
		Thread.sleep(3000);

		turnGT(-90, 0.5);
		halt();
		telemetry.addData("Turns:", 4);
		Thread.sleep(3000);
	}
}
