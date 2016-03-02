// DFFB: Drive to Floor, Far, Blue
// Drives to the floor goal/rescue beacon repair zone.
// The robot starts out centered on the border between the 5th and 6th tiles directly facing the rescue beacon repair zone.

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

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget(8 * FT, 0.7, 5000);            // Move forwards 8 feet
		turn(20, 0.7, 3000);                   // Turn 20 degrees left
		moveTarget(3*FT, 0.7, 5000);            // Move forwards 3 feet
		//moveForward(FT, 0.7, 1000);
		dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}
