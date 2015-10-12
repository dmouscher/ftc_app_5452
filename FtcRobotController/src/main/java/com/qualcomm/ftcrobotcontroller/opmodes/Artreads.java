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
	DcMotor motorRight;
	DcMotor motorLeft;

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
		motorLeft = hardwareMap.dcMotor.get("motor_left");
		motorRight = hardwareMap.dcMotor.get("motor_right");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		double speed = 0.5;
		final double DEADZONE = 0.05;

		waitForStart();

		while (opModeIsActive())
		{
			if (gamepad1.left_stick_y < DEADZONE || gamepad1.right_stick_y < DEADZONE)
			{
				motorRight.setPower(gamepad1.right_stick_y);
				motorLeft.setPower(gamepad1.right_stick_x);
			}
			else
			{
				if (gamepad1.left_bumper)
				{
					motorLeft.setPower(speed * reverseMultiplier());
				}
				if (gamepad1.right_bumper)
				{
					motorRight.setPower(speed * reverseMultiplier());
				}
				if (gamepad1.y)
				{
					motorRight.setPower(speed);
					motorLeft.setPower(speed);
				}
				if (gamepad1.a)
				{
					motorRight.setPower(-speed);
					motorLeft.setPower(-speed);
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
