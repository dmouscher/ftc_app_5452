package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DMNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{

		mapHardware();

		driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		waitForStart();

		moveTarget((int)(0.45*FT /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
		turn(90, 0.8, 1000); /* Make sure this turns right */
		moveTarget((int)(0.45*FT /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
	}
}
