package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


public class ResetEncoders extends OpMode
{
	DcMotor driveLeft;
	DcMotor driveRight;

	DcMotor armRotate;
	DcMotor armExtend;

	DcMotor plow;

	Servo dropperBase;
	Servo dropperJoint;
	Servo rescueLeft;
	Servo rescueRight;

	@Override
	public void init()
	{
		driveLeft  = hardwareMap.dcMotor.get("left" );
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");

		dropperBase  = hardwareMap.servo.get("base" );
		dropperJoint = hardwareMap.servo.get("joint");
		rescueLeft   = hardwareMap.servo.get("rql"  );
		rescueRight  = hardwareMap.servo.get("rqr"  );

		driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void loop()
	{
		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}

	@Override
	public void stop()
	{
	}
}
