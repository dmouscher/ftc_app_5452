package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class Jackie_Skeleton1 extends LinearOpMode
{
	DcMotor motor;
	boolean firstloop = true;

	@Override
	public void runOpMode() throws InterruptedException
	{
		motor = hardwareMap.dcMotor.get("motor");
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);


		waitForStart();
		while (opModeIsActive())

		{
			motor.setPower(0.8);
		/*	if (firstloop)
		}
		{
		*/ //delete this line and the /* on line 24 to fix this, I dont like errors

			motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
			firstloop = false;
		}
	}
}
