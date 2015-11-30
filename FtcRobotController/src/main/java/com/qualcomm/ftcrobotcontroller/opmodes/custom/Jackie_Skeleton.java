
package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class Jackie_Skeleton extends LinearOpMode
{
	DcMotor motor;
	boolean firstloop = true;

	@Override
	public void runOpMode() throws InterruptedException
	{
		motor = hardwareMap.dcMotor.get("motor");
<<<<<<< HEAD
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
=======
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
>>>>>>> origin/master


		waitForStart();
		while(opModeIsActive())
		{
			motor.setPower(0.8);
			if(firstloop)
			{
				motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
				firstloop = false;
			}

			motor.setTargetPosition(1440);
		}
	}
}