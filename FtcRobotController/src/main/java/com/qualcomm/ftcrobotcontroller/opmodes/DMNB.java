// DMNB: Drive to Mountain, Near, Blue

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DMNB extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{

		mapHardware();
        initalize();
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

		waitForStart();

		moveTarget((int)(42*IN /* 12 ft times the distance you want.*/), 0.8, 1000);
		turn(135, 0.8, 1000); /* Make sure this turns right */
		moveTarget((int)(18*IN /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);

	}
}
