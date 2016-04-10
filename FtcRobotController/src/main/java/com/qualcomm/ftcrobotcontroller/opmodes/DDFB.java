// DDFB: Drive to Dump, Far, Blue
// Drives in front of the rescue beacon repair zone and dumps the climbers in the basket.
// The robot starts out facing the zones at a 45 degree angle from the wall and centered on the 5th tile from the mountain.

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DDFB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL);          // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);              // Extend the plow
		moveTarget(6.75 * Math.sqrt(2) * FT, 0.7, 7500); // Move forwards
		turn(45, 0.7, 2000);                             // Turn
		movePlow(-0.75, PLOW_EXTEND_LENGTH / 2);         // Extend the plow
		moveTarget(2.34 * FT, 0.35, 5000);               // Move forwards
		dropperBase.setPosition(BASE_DUMPING);           // Dump
		Thread.sleep(3500);                              // Wait for dump
		moveTarget(-FT, 0.7, 3000);                      // Move backwards
		Thread.sleep(1000);
		dropperBase.setPosition(BASE_VERTICAL);          // Raise up the climber-dropper
		Thread.sleep(1000);
	}
}