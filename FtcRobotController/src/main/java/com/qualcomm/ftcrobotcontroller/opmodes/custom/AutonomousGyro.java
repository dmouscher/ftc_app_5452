package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;


/**
* Created by ur mum on 12/14/15.
		*/
public class AutonomousGyro extends LinearOpMode {

	enum Direction { LEFT, RIGHT; }

	DcMotor driveLeft;
	DcMotor driveRight;

	DcMotor armRotate;
	DcMotor armExtend;

	DcMotor plow;

	GyroSensor gyro;

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

		gyro = hardwareMap.gyroSensor.get("gyro");

		driveRight.setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);
		plow.setDirection(DcMotor.Direction.REVERSE);

		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		dropperBase .setPosition(0.10);
		dropperJoint.setPosition(1.00);

		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);

		waitForStart();

		dropperBase.setPosition(0.25);
		movePlow(0.75, 9500);
		moveForward(2*FT, 0.7, 3000);
		moveForward(-FT, -0.7, 1000);
		turn(45, 0.8);
		moveForward(5*Math.sqrt(2)*FT - 4*IN, 0.8, 7000);
		turn(58, 0.8);
		moveForward(-4*IN, -0.7, 1000);
		while(dropperBase.getPosition() < 0.8 && dropperJoint.getPosition() > 0)
		{
			if(dropperBase .getPosition() < 0.8) { dropperBase .setPosition(dropperBase.getPosition() + 0.01); }
			if(dropperJoint.getPosition() > 0  ) { dropperJoint.setPosition(dropperJoint.getPosition() - 0.01); }
			Thread.sleep(25);
		}
		Thread.sleep(600);
		moveForward(-FT, -0.7, 1000);
		moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);
		Thread.sleep(1000);
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

	public void turn(int deg, double speed) throws InterruptedException // deg > 0 for right, deg < 0 for left
	{                                                                   // NO VALUES LARGER THAN 360
		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		resetGyro();
		//gyro.resetZAxisIntegrator(); // Not sure if this will work
		deg *= 0.9; // Idk im just trying stuff here

		if(deg > 0) // If deg is positive
		{
			//telemetry.addData("Phase 2+", "");
			driveLeft.setPower(speed*-1);
			driveRight.setPower(speed);
			do{
				telemetry.addData("H: ", gyro.getHeading());
				//waitOneFullHardwareCycle();
			}while(gyro.getHeading() < deg || gyro.getHeading() > 350); // 350 is  a place holder value because I havent found an efficient way to make the gyro reset in a time efficient manner
		}

		else
		{
			//telemetry.addData("Phase 2-","");
			driveLeft.setPower(speed);
			driveRight.setPower(speed*-1);
			do
			{
				telemetry.addData("H: ", gyro.getHeading());
			}while(gyro.getHeading() > 360-deg || gyro.getHeading() == 0);
		}

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		//telemetry.addData("Phase 3", "");
		//halt();
	}

	public void halt() // break and stop were taken
	{
		do {
			driveLeft.setPower(0);
			driveRight.setPower(0);
		}while(driveLeft.getPower() != 0 || driveRight.getPower() != 0);
	}

	public void resetGyro() throws InterruptedException
	{
		if(gyro.getHeading() != 0) //  can turn the if to a while in the case that this is not seeming to work
		{
			gyro.calibrate();
			while(gyro.isCalibrating()) {Thread.sleep(50);}
		}
	}

	public void movePlow(double speed, int waitTime) throws InterruptedException
	{
		plow.setPower(speed);
		Thread.sleep(waitTime);
		plow.setPower(0);
	}
}
