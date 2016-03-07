package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Jefferson Starship on 3/6/2016.
 */
public class ConstTest extends LinearBase
{
	double inch = IN;
	double deg = DEG;

	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();
		turn(360, 0.4, 10000);
	}
}
