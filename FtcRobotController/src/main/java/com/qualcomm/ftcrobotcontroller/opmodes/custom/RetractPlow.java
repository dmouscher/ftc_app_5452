// Retracts the plow until the op mode is manually stopped

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class RetractPlow extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		waitForStart();
		movePlow(-0.5, 0);
	}
}
