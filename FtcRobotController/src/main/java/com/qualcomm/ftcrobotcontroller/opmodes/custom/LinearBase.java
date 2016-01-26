package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearBase extends LinearOpMode
{
	enum Direction { LEFT, RIGHT; }

	DcMotor driveLeft;
	DcMotor driveRight;

	DcMotor armRotate;
	DcMotor armExtend;

	DcMotor plow;

	Servo dropperBase;
	Servo rescueLeft;
	Servo rescueRight;

	GyroSensor gyro;

	final double DEG = 2900/90.0;
	final double IN  = 144.796;
	final double FT  = 12*IN;

	public void mapHardware()
	{
		driveLeft  = hardwareMap.dcMotor.get("left" );
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");

		dropperBase = hardwareMap.servo.get("base");
		rescueLeft  = hardwareMap.servo.get("rql" );
		rescueRight = hardwareMap.servo.get("rqr" );

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo  .Direction.REVERSE);
		plow       .setDirection(DcMotor.Direction.REVERSE);
	}

	public void drivetrainSetMode(DcMotorController.RunMode mode)
	{
		driveLeft.setMode(mode);
		driveRight.setMode(mode);
	}

	public void moveForward(double dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
	{
		int idist = (int)dist;

		driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist);
		driveLeft .setTargetPosition(driveLeft .getCurrentPosition() + idist);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void turn(int deg, double speed, int waitTime) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft .getCurrentPosition() + (int)(-deg*DEG));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int)( deg*DEG));

		driveLeft .setPower(-speed);
		driveRight.setPower( speed);

		Thread.sleep(waitTime);
	}

	public void movePlow(double speed, int waitTime) throws InterruptedException
	{
		plow.setPower(speed);
		Thread.sleep(waitTime);
		plow.setPower(0);
	}

	@Override
	public void runOpMode() throws InterruptedException
	{
	}
}
