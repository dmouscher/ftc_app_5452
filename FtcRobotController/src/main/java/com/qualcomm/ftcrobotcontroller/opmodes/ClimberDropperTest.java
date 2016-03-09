package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;

public class ClimberDropperTest extends LinearBase
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		double drivetrainSpeed = 0.50;
		boolean isDpadPrimed = true;
		initialize();
		waitForStart();

		while(opModeIsActive())
		{
			if(gamepad1.dpad_up && isDpadPrimed) //dpad_up increases speed
			{
				dropperBase.setPosition(Range.clip(dropperBase.getPosition()+0.05,0,1));
				isDpadPrimed = false;
			}
			if(gamepad1.dpad_down && isDpadPrimed) //dpad_down decreases speed
			{
				dropperBase.setPosition(Range.clip(dropperBase.getPosition()-0.05,0,1));
				isDpadPrimed = false;
			}

			if(gamepad1.a) resetGyro();
			if(gamepad1.b) gyro.resetZAxisIntegrator(); //a and b reset the gyro in different ways

			driveLeft .setPower(drivetrainSpeed * (gamepad1.dpad_left ?  1 : gamepad1.dpad_right ? -1 : 0));
			driveRight.setPower(drivetrainSpeed * (gamepad1.dpad_left ? -1 : gamepad1.dpad_right ?  1 : 0));

			if(!gamepad1.dpad_down && !gamepad1.dpad_up) isDpadPrimed = true;

			telemetry.addData("Position: ", dropperBase.getPosition());
		}
	}
}