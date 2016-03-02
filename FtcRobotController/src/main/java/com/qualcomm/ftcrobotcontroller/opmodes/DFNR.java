// DFNR: Drive to Floor, Near, Red
//Drives in front of the mountain to the floor goal/rescue beacon repair zone.
// The robot starts out facing the zones at a 45 degree angle from the wall and centered on the border between the 3rd and 4th tiles (from the left.)

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFNR extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget((int)(5.7*FT), 0.7, 5000);   // Move forward 5.7 feet
		turn(-20, 0.7, 2000);                   // Turn left 20 degrees
		moveTarget((int)FT, 0.7, 10000);        // Move forward one foot
		//moveForward(FT, 0.7, 1000);
		dropperBase.setPosition(0.1);         // Dump
		Thread.sleep(1000);
	}
}