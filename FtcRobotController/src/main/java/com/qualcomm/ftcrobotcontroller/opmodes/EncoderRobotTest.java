package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EncoderRobotTest extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	/*
	final int SMOOTH_LENGTH = 10;

	double[] lastXLeft  = new double[SMOOTH_LENGTH];
	double[] lastXRight = new double[SMOOTH_LENGTH];
	*/

	public void runOpMode()
	{
		mapHardware();

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

		driveRight.setTargetPosition(1440);
		driveLeft .setTargetPosition(1440);

		telemetry.addData("Encoder Values", driveRight.getCurrentPosition() + ", " + driveLeft.getCurrentPosition());

		driveRight.setPower(0.8);
		driveLeft .setPower(0.8);
	}
}
