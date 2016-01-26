// DFNB = Dump from Floor, Near, Blue

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jackiehirsch on 12/14/15.
 */
public class DFNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

		dropperBase .setPosition(0.267);

		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);

		waitForStart();

		dropperBase.setPosition(0.518);   // Raise up the climber-dropper
		movePlow(0.75, 9500);             // Extend the plow
		moveForward(5.7 * FT, 0.7, 5000); // Move forward 5.7 feet
		moveForward(FT, 0.7, 10000);      // Move forward 1 foot
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);   // Dump
		Thread.sleep(1000);
	}
}
