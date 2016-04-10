// DFFB: Drive to Floor, Far, Blue
// Drives to the rescue beacon repair zone.
// The robot starts out centered on the border between the 5th and 6th tiles from the mountain directly facing the rescue beacon repair zone.

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFFB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL);   // Raise the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);       // Extend the plow
		moveTarget(10 * FT, 0.7, 5000);           // Move forwards
		turn(20, 0.7, 3000);                      // Turn
		moveTarget(3 * FT - .66 * IN, 0.7, 5000); // Move forwards
		Thread.sleep(1000);
	}
}
