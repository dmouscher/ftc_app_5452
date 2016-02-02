package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class GyroTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initalize();
		waitForStart();

		moveEn(1440*2, 0.8); // just to make sure this works
        
		turnG(90, 0.5);
		halt();

        Thread.sleep(1000);

        turnG(-90, 0.5);
        halt();
	}
}
