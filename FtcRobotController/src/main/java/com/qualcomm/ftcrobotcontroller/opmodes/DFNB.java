// DFNB: Drive to Floor, Near, Blue
// Drives in front of the mountain to the floor goal/rescue beacon repair zone.
// The robot starts out facing the zones at a 45 degree angle from the wall centered between the 3rd and 4th tiles from the mountain

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DFNB extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, PLOW_EXTEND_LENGTH);     // Extend the plow
		moveTarget(5.7 * FT, 0.7, 5000);        // Move forwards
		turn(20, 0.7, 2000);                    // Turn
		moveTarget(2 * FT, 0.7, 10000);         // Move forwards
		Thread.sleep(1000);
	}
}
