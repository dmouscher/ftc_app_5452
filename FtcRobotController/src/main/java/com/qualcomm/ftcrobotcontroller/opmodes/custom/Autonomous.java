package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jackiehirsch on 12/14/15.
 */
public class Autonomous extends LinearBase
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
		moveTarget(5.7 * FT, 0.7, 5000);
		turn(-60, 0.7, 2000);
		moveTarget(FT, 0.7, 10000);
		Thread.sleep(1000);
	}
}
