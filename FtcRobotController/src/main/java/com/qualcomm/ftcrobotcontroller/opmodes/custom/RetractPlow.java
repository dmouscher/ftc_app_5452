package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by jackiehirsch on 12/14/15.
 */
public class RetractPlow extends LinearOpMode {

	enum Direction { LEFT, RIGHT; }

	DcMotor driveLeft;
	DcMotor driveRight;

	DcMotor armRotate;
	DcMotor armExtend;

	DcMotor plow;

	Servo dropperBase;
	Servo dropperJoint;
	Servo rescueLeft;
	Servo rescueRight;

	final double DEG       = 2900/90.0;
	final double IN         = 144.796;
	final double FT         = 12*IN;

	@Override
	public void runOpMode() throws InterruptedException
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

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);

		driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		dropperBase .setPosition(0.25);
		dropperJoint.setPosition(1.00);

		rescueLeft.setPosition(0);
		rescueRight.setPosition(0);

		plow.setDirection(DcMotor.Direction.REVERSE);
		waitForStart();

		//rn
		/*
		moveForward(2*FT, 0.7, 3000);
		moveForward(-FT, -0.7, 1000);
		turn(-45, -0.8, 3000);
		moveForward(5 * Math.sqrt(2)*FT, 0.8, 10000);
		turn(-60, -0.8, 3000);
		*/
		movePlow(-5000, -0.7, 10000);

	}

	public void moveForward(double dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
	{
		int idist = (int)dist;
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist/**TICKS_PER_INCH*/);
		driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + idist/**TICKS_PER_INCH*/);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void turn(int deg, double speed, int waitTime) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int)(-1*deg*DEG));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int)(deg*DEG));

		driveLeft .setPower(-1 * speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void movePlow(int target, double speed, int waitTime) throws InterruptedException
	{
		plow.setTargetPosition(plow.getCurrentPosition() + target);
		plow.setPower(speed);
		Thread.sleep(waitTime);
	}
}
