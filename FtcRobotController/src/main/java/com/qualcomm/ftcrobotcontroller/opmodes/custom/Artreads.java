/*
Artreads

This is a one-gamepad teleop op mode is meant for testing the prototype drive train. Operation is as follows:

• The drive motors can be controlled in the traditional way using the joysticks
• Otherwise, the y-button moves the robot forwards and the a-button moves the robot backwards both at the set speed
• The left bumper button moves the left motor forwards and the right trigger button moves the right motor forwards both at the set speed
• The bumper buttons' movement can be reversed by holding down the b-button
• The set speed can be increased by holding down "up" on the dpad and can be decreased by holding down "down" on the dpad

 */

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;

public class Artreads extends LinearOpMode
{
	DcMotor motorFL;
	DcMotor motorFR;
	DcMotor motorBL;
	DcMotor motorBR;

	boolean isPrimed = true;
	double speed = 0.5;
	final double DEADZONE = 0.2;

	private void runAllMotors(double speed)
	{
		motorFL.setPower(speed);
		motorBL.setPower(speed);
		motorFR.setPower(speed);
		motorBR.setPower(speed);
	}

	private void runLeftMotors(double speed)
	{
		motorFL.setPower(speed);
		motorBL.setPower(speed);
	}

	private void runRightMotors(double speed)
	{
		motorFR.setPower(speed);
		motorBR.setPower(speed);
	}

	private boolean areJoysticksActive() { return (Math.abs(gamepad1.left_stick_y) > DEADZONE || Math.abs(gamepad1.right_stick_y) > DEADZONE); }

	@Override
	public void runOpMode() throws InterruptedException
	{
		motorFL = hardwareMap.dcMotor.get("fl");
		motorFR = hardwareMap.dcMotor.get("fr");
		motorBL = hardwareMap.dcMotor.get("bl");
		motorBR = hardwareMap.dcMotor.get("br");

		motorFR.setDirection(DcMotor.Direction.REVERSE);
		motorBR.setDirection(DcMotor.Direction.REVERSE);

		waitForStart();

		while(opModeIsActive())
		{
			if (areJoysticksActive())
			{
				runLeftMotors(gamepad1.left_stick_y);
				runRightMotors(gamepad1.right_stick_y);
			}
			else
			{
				runAllMotors(0);

				if  (gamepad1.left_bumper) {  runLeftMotors(speed * (gamepad1.b ? -1 : 1)); }
				if (gamepad1.right_bumper) { runRightMotors(speed * (gamepad1.b ? -1 : 1)); }

				if (gamepad1.y) { runAllMotors( speed); }
				if (gamepad1.a) { runAllMotors(-speed); }
			}

			if (gamepad1.dpad_up && speed < 0.95 && isPrimed)
			{
				speed += 0.05;
				isPrimed = false;
			}
			else if (gamepad1.dpad_down && speed > 0.05 && isPrimed)
			{
				speed -= 0.05;
				isPrimed = false;
			}

			if(!(gamepad1.dpad_up && gamepad1.dpad_down && isPrimed)) { isPrimed = true; }

			telemetry.addData("Speed: ", (int)(speed*100) + "%");
			telemetry.addData("Bumpers: ", gamepad1.left_bumper + ", " + gamepad1.right_bumper);
			telemetry.addData("Joysticks: ", areJoysticksActive() ? "Active" : "Inactive");
			waitOneFullHardwareCycle();
		}
	}
}