package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class FourDrivableMotorsTELEOP extends LinearOpMode
{
	DcMotor motor1;
	DcMotor motor2;
	DcMotor motor3;
	DcMotor motor4;
	Servo servo;


	@Override
	public void runOpMode() throws InterruptedException
			/*
			The .get for the servo is hardwareMap.servo.get();
			 */
	{
		motor1 = hardwareMap.dcMotor.get("motor1");
		motor2 = hardwareMap.dcMotor.get("motor2");
		motor3 = hardwareMap.dcMotor.get("motor3");
		motor4 = hardwareMap.dcMotor.get("motor4");
		servo = hardwareMap.servo.get("servo");

		waitForStart();
		while(opModeIsActive())
		{

			/*
			Motors 1 and 2 are controlled from left side and Motors 3 and 4 are controlled on right side.
			 */
			motor1.setPower(gamepad1.left_stick_y);
			motor2.setPower(gamepad1.left_stick_y);
			motor3.setPower(gamepad1.right_stick_y);
			motor4.setPower(gamepad1.right_stick_y);
			if (gamepad1.a)
			{
				servo.setPosition(127);
			}
			else
			{
				servo.setPosition(0);
			}


		}
	}
}
