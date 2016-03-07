package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;

public class GyroTestTeleop extends GyroTest
{
	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		double drivetrainSpeed = 0.50;
		boolean isDpadPrimed = true;
		boolean isDpadActive = false;
		initialize();
		waitForStart();

		while(opModeIsActive())
		{
			if(gamepad1.dpad_up && isDpadPrimed) //dpad_up increases speed
			{
				isDpadPrimed = false;
				isDpadActive = true;
				drivetrainSpeed = Range.clip(drivetrainSpeed += 0.05, 0, 1);
			}
			if(gamepad1.dpad_down && isDpadPrimed) //dpad_down decreases speed
			{
				isDpadPrimed = false;
				isDpadActive = true;
				drivetrainSpeed = Range.clip(drivetrainSpeed -= 0.05, 0, 1);
			}

			if(gamepad1.a) resetGyro();
			if(gamepad1.b) gyro.resetZAxisIntegrator(); //a and b reset the gyro in different ways

			driveLeft .setPower(drivetrainSpeed * (gamepad1.dpad_left ?  1 : gamepad1.dpad_right ? -1 : 0));
			driveRight.setPower(drivetrainSpeed * (gamepad1.dpad_left ? -1 : gamepad1.dpad_right ?  1 : 0));

			if(!isDpadActive) isDpadPrimed = true;

			telemetry.addData("Action: ", gamepad1.dpad_left ? "Turning left" : gamepad1.dpad_right ? "Turning right" : "Staying still");
			telemetry.addData("Speed: ", drivetrainSpeed);
			telemetry.addData("Heading: ", gyro.getHeading());
			telemetry.addData("Integrated Z-Value: ", gyro.getIntegratedZValue());
		}
	}
}