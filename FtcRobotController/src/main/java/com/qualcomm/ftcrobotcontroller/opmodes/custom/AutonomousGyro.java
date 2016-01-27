package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
* Created by ur mum on 12/14/15.
		*/
public class AutonomousGyro extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

		dropperBase .setPosition(0.10);

		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);

		waitForStart();

		dropperBase.setPosition(0.25);
		movePlow(0.75, 9500);
		moveTarget(2 * FT, 0.7, 3000);
		moveTarget(-FT, -0.7, 1000);
		turn(45, 0.8);
		moveTarget(5 * Math.sqrt(2) * FT - 4 * IN, 0.8, 7000);
		turn(58, 0.8);
		moveTarget(-4 * IN, -0.7, 1000);
		while(dropperBase.getPosition() < 0.8)
		{
			if(dropperBase .getPosition() < 0.8) { dropperBase .setPosition(dropperBase.getPosition() + 0.01); }
			Thread.sleep(25);
		}
		Thread.sleep(600);
		moveTarget(-FT, -0.7, 1000);
		moveTarget(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);
		Thread.sleep(1000);
	}

	public void turn(int deg, double speed) throws InterruptedException // deg > 0 for right, deg < 0 for left
	{                                                                   // NO VALUES LARGER THAN 360
		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		resetGyro();
		//gyro.resetZAxisIntegrator(); // Not sure if this will work
		deg *= 0.9; // Idk im just trying stuff here

		if(deg > 0) // If deg is positive
		{
			//telemetry.addData("Phase 2+", "");
			driveLeft.setPower(speed*-1);
			driveRight.setPower(speed);
			do{
				telemetry.addData("H: ", gyro.getHeading());
				//waitOneFullHardwareCycle();
			}while(gyro.getHeading() < deg || gyro.getHeading() > 350); // 350 is  a place holder value because I havent found an efficient way to make the gyro reset in a time efficient manner
		}

		else
		{
			//telemetry.addData("Phase 2-","");
			driveLeft.setPower(speed);
			driveRight.setPower(speed*-1);
			do
			{
				telemetry.addData("H: ", gyro.getHeading());
			}while(gyro.getHeading() > 360-deg || gyro.getHeading() == 0);
		}

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		//telemetry.addData("Phase 3", "");
		//halt();
	}

	public void halt() // break and stop were taken
	{
		do {
			driveLeft.setPower(0);
			driveRight.setPower(0);
		}while(driveLeft.getPower() != 0 || driveRight.getPower() != 0);
	}

	public void resetGyro() throws InterruptedException
	{
		if(gyro.getHeading() != 0) //  can turn the if to a while in the case that this is not seeming to work
		{
			gyro.calibrate();
			while(gyro.isCalibrating()) {Thread.sleep(50);}
		}
	}
}
