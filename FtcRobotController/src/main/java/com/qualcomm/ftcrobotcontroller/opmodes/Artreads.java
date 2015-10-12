/*
Artreads

This is a one-gamepad teleop op mode is meant for testing the prototype drive train. Operation is as follows:

• The drive motors can be controlled in the traditional way using the joysticks
• Otherwise, the y-button moves the robot forwards and the a-button moves the robot backwards both at the set speed
• The left trigger button moves the left motor forwards and the right trigger button moves the right motor forwards both at the set speed
• The trigger buttons' movement can be reversed by holding down the b-button
• The set speed can be increased by holding down "up" on the dpad and can be decreased by holding down "down" on the dpad

 */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class Artreads extends LinearOpMode
{
	DcMotor motorFL;
	DcMotor motorFR;
	DcMotor motorBL;
	DcMotor motorBR;

	public int reverseMultiplier()
	{
		if (gamepad1.b)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}

	@Override
	public void runOpMode() throws InterruptedException
	{
		motorFL = hardwareMap.dcMotor.get("motor_fl");
		motorFR = hardwareMap.dcMotor.get("motor_fr");
		motorBL = hardwareMap.dcMotor.get("motor_bl");
		motorBR = hardwareMap.dcMotor.get("motor_br");

		motorFL.setDirection(DcMotor.Direction.REVERSE);
		motorFR.setDirection(DcMotor.Direction.REVERSE);

		double speed = 0.5;
		final double DEADZONE = 0.05;

		waitForStart();

		while (opModeIsActive())
		{
			if (gamepad1.left_stick_y < DEADZONE || gamepad1.right_stick_y < DEADZONE)
			{
				motorFL.setPower(gamepad1.left_stick_y);
				motorBL.setPower(gamepad1.left_stick_y);
				motorFR.setPower(gamepad1.right_stick_y);
				motorBR.setPower(gamepad1.right_stick_y);
			}
			else
			{
				if (gamepad1.left_bumper)
				{
					motorFL.setPower(speed * reverseMultiplier());
					motorBL.setPower(speed * reverseMultiplier());
				}
				if (gamepad1.right_bumper)
				{
					motorFR.setPower(speed * reverseMultiplier());
					motorBR.setPower(speed * reverseMultiplier());
				}
				if (gamepad1.y)
				{
					motorFL.setPower(speed);
					motorBL.setPower(speed);
					motorFR.setPower(speed);
					motorBR.setPower(speed);
				}
				if (gamepad1.a)
				{
					motorFL.setPower(-speed);
					motorBL.setPower(-speed);
					motorFR.setPower(-speed);
					motorBR.setPower(-speed);
				}
			}

			if (gamepad1.dpad_up && speed < 1)
			{
				speed += 0.005;
			}
			else if (gamepad1.dpad_down && speed > 0)
			{
				speed -= 0.005;
			}

			telemetry.addData("Speed: ", speed * 100 + "%");
			waitOneFullHardwareCycle();
		}
	}
}