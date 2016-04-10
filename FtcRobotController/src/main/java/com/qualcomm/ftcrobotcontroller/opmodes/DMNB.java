// DMNB: Drive to Mountain, Near, Blue
// Drives partially up the nearest mountain zone.
// The robot starts out facing the zones at a 45 degree angle from the wall and centered between the 3rd and 4th tiles from the mountain.
// This op mode is not fully tested/implemented

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DMNB extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		movePlow(0.75, PLOW_EXTEND_LENGTH); // Extend the plow
		moveTarget(42 * IN, 0.8, 5000);     // Move forwards
		turn(135, 0.8, 3000);               // Sharply turns right
		moveTarget(18 * IN, 0.8, 3000);     // Move forwards onto ramp
	}
}
