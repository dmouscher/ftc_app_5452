package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ServoTest extends LinearOpMode
{
	Servo servo;

	@Override
	public void runOpMode() throws InterruptedException
	{
		servo = hardwareMap.servo.get("servo");
		waitForStart();

		while(opModeIsActive())
		{
			servo.setPosition(Range.clip((gamepad1.left_stick_y / 2) + 0.5, 0, 1));
			waitOneFullHardwareCycle();
		}
	}
}
