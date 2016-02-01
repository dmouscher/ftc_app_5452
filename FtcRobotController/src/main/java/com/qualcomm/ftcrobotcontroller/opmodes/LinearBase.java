// Base class for our custom op modes, contains functions for initialization and encoder usage

package com.qualcomm.ftcrobotcontroller.opmodes;

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

	Servo rescueLeft;
	Servo rescueRight;

	Servo dropperBase;

	GyroSensor gyro;

	final double DEG = 2900/90.0;
	final double IN  = 144.796;
	final double FT  = 12*IN;

	final double BASE_RESTING  = 0.267; // fits inside the sizing cube
	final double BASE_VERTICAL = 0.538;

	boolean verbose = false;

	public void mapHardware()
	{
		driveLeft  = hardwareMap.dcMotor.get("left");
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");

		rescueLeft  = hardwareMap.servo.get("rql");
		rescueRight = hardwareMap.servo.get("rqr");

		dropperBase = hardwareMap.servo.get("base");

		gyro = hardwareMap.gyroSensor.get("gyro"); // uncomment if you are using the gyro

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		plow       .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo  .Direction.REVERSE);
	}

	public void drivetrainSetMode(DcMotorController.RunMode mode)
	{
		driveLeft .setMode(mode);
		driveRight.setMode(mode);
	}

	public void resetServos()
	{
		dropperBase.setPosition(BASE_RESTING);
		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);
	}

	public void initalize()
	{
		if(verbose) telemetry.addData("", "Initalizing");
		mapHardware();
		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		resetServos();
		resetEncoders();
		gyro.calibrate();
		while(gyro.isCalibrating()){}
		if(verbose) telemetry.addData("", "Initialization complete");
	}

	public void moveTarget(double dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
	{
		int idist = (int)dist;

		driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist);
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + idist);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void moveEn(int dist, double speed) throws InterruptedException // Test this
	{
		int startingLeft  = driveLeft.getCurrentPosition();
		int startingRight = driveRight.getCurrentPosition();

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while (driveLeft.getCurrentPosition() - startingLeft < dist || driveRight.getCurrentPosition() - startingRight < dist)
		{
			telemetry.addData("M: ", driveLeft.getCurrentPosition() + ", " + driveRight.getCurrentPosition());
			waitOneFullHardwareCycle();
		}
	}

	public void turn(int deg, double speed, int waitTime) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft .getCurrentPosition() + (int) (-deg * DEG));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int) ( deg * DEG));

		driveLeft .setPower(-speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void turnG(int deg, double speed) throws InterruptedException // Pos Values, turn right
	{
		resetGyro();

		driveLeft .setPower(speed * (deg < 0 ?  1 : -1)); // Make sure these turn the right way
		driveRight.setPower(speed * (deg < 0 ? -1 :  1));

		while(deg>0 && gyro.getHeading() < deg) { if(verbose) telemetry.addData("", gyro.getHeading()); } // turn right
		while(deg<0 && (map(gyro.getHeading(), 359, 0, 0, 359) < deg || map(gyro.getHeading(), 359, 0, 0, 359) > 350)){ if(verbose) telemetry.addData("", gyro.getHeading()); } // turn left
	}

	public void movePlow(double speed, int waitTime) throws InterruptedException
	{
		plow.setPower(speed);
		while(opModeIsActive() && waitTime == 0) { waitOneFullHardwareCycle(); } // Pauses indefinitely if 0 is entered for waitTime
		Thread.sleep(waitTime);
		plow.setPower(0);
	}

	public void halt() throws InterruptedException
	{
		driveLeft .setPower(0);
		driveRight.setPower(0);
		waitOneFullHardwareCycle();
	}

	public double map(double x, double in_min, double in_max, double out_min, double out_max) // Thanks to arduino.cc for providing the formula
		{ return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min; }

	public void resetEncoders()
	{
		int runNum = 0;

		while (driveLeft.getCurrentPosition() != 0 || driveRight.getCurrentPosition() != 0)
		{
			drivetrainSetMode(DcMotorController.RunMode.RESET_ENCODERS);
			runNum++;
			if(verbose) telemetry.addData("", "Resetting encoders, tried " + runNum + " time" + (runNum==1?"":"s"));
		}

		if(verbose) telemetry.addData("", "Encoders reset, tried " + runNum + " time" + (runNum==1?"":"s"));
	}

	public void resetGyro() throws InterruptedException
	{
		if(verbose) telemetry.addData("", "Resetting gyro sensor");

		gyro.calibrate();
		while(gyro.isCalibrating()) {Thread.sleep(50);}

		if(verbose) telemetry.addData("", "Gyro sensor reset");
	}

	@Override
	public void runOpMode() throws InterruptedException
	{
	}
}
