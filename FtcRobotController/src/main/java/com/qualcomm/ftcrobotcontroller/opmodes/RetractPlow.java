// Retracts the plow until the op mode is manually stopped

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class RetractPlow extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		waitForStart();
		movePlow(-0.5, 0);
	}
}
