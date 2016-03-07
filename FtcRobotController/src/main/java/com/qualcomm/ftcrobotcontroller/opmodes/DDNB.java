// DFNB: Drive to Floor, Near, Blue
// Drives in front of the mountain to the floor goal/rescue beacon repair zone.
// The robot starts out facing the zones at a 45 degree angle from the wall and centered on the border between the 3rd and 4th tiles (from the left.)

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DDNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		//movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget((int) (6.75 * Math.sqrt(2) * FT), 0.7, 7500);   // Move forward
		turn(45, 0.7, 2000);                   // Turn
		moveTarget((int) (1.25*FT), 0.7, 1000);
		dropperBase.setPosition(BASE_DUMPING);  // Dump
		moveTarget((int)(-1*FT), 0.7, 2000);
		moveTarget((int) (1.25*FT), 0.7, 1000);
		Thread.sleep(1000);
	}
}
