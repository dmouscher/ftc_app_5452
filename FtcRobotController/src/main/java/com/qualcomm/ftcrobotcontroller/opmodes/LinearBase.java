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
	DcMotor winch;

	Servo rescueLeft;
	Servo rescueRight;

	Servo dropperBase;
	Servo hook;

	GyroSensor gyro;

	final double DEG = 2900/90.0;
	final double IN  = 144.796;
	final double FT  = 12*IN;

	final double BASE_RESTING    = 0.280; // fits inside the sizing cube
	final double BASE_VERTICAL   = 0.475;
	final double RESCUELEFT_IN   = 0.280;
	final double RESCUERIGHT_IN  = 0    ;
	final double RESCUELEFT_OUT  = 1    ;
	final double RESCUERIGHT_OUT = 0.775;
	final double HOOK_RESTING    = 0    ;

	final int PLOW_EXTEND_LENGTH = 3800;

	boolean verbose = false;
	int truegyro = 0;

	public void mapHardware()
	{
		driveLeft  = hardwareMap.dcMotor.get("left");
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");
		winch = hardwareMap.dcMotor.get("winch");

		rescueLeft  = hardwareMap.servo.get("rql");
		rescueRight = hardwareMap.servo.get("rqr");

		dropperBase = hardwareMap.servo.get("base");
		hook = hardwareMap.servo.get("hook");

		gyro = hardwareMap.gyroSensor.get("gyro"); // uncomment if you are using the gyro

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		plow       .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);
	}

	public void drivetrainSetMode(DcMotorController.RunMode mode)
	{
		driveLeft .setMode(mode);
		driveRight.setMode(mode);
	}

	public void resetServos()
	{
		dropperBase.setPosition(BASE_RESTING  );
		rescueLeft .setPosition(RESCUELEFT_IN );
		rescueRight.setPosition(RESCUERIGHT_IN);
		hook       .setPosition(HOOK_RESTING);
	}

	public void initialize()
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
			if(verbose) telemetry.addData("M: ", driveLeft.getCurrentPosition() + ", " + driveRight.getCurrentPosition());

			waitOneFullHardwareCycle();
		}
	}

	public void turn(int deg, double speed, int waitTime) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int) (-deg * DEG));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int) ( deg * DEG));

		driveLeft .setPower(-speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void turnG(int deg, double speed) throws InterruptedException // Pos Values, turn right
	{
		resetGyro();

		if(verbose) telemetry.addData("", "Starting the motors");
		driveLeft .setPower(speed * (deg < 0 ?  1 : -1)); // Make sure these turn the right way
		driveRight.setPower(speed * (deg < 0 ? -1 :  1));
		if(verbose) telemetry.addData("", "Motors should be started");

		if(verbose) telemetry.addData("", "Starting loops");
		while(deg > 0 && gyro.getHeading() < deg) { if(verbose) telemetry.addData("", gyro.getHeading()); } // turn right

		while(gyro.getHeading()  >= 0 & gyro.getHeading()  <= 5) waitOneFullHardwareCycle();
		while(deg < 0 && (map(gyro.getHeading(), 359, 0, 0, 359) < deg)){ if(verbose) telemetry.addData("", (map(gyro.getHeading(), 359, 0, 0, 359))); } // turn left
		if(verbose) telemetry.addData("", "Done looping");
	}

	public void turnGT(int deg, double speed) throws InterruptedException
	{
		resetGyro();
		truegyro = deg;

		int targetdeg = truegyro % 360 < 0 ? (truegyro % 360) + 360 : truegyro % 360;
		int gyroinit = gyro.getHeading();
		boolean leftLocked = gyro.getHeading() < targetdeg;
		boolean rightLocked = gyro.getHeading() > targetdeg;

		if(verbose) telemetry.addData("", "Starting motors, targetdeg: " + targetdeg);
		driveLeft .setPower(speed * (deg < 0 ?  1 : -1)); // Make sure these turn the right way
		driveRight.setPower(speed * (deg < 0 ? -1 :  1));
		if(verbose) telemetry.addData("", "Motors started (?)");

		if(deg > 0)
		{
			while(gyro.getHeading() < targetdeg || rightLocked)
			{
				waitOneFullHardwareCycle();
				if(rightLocked) rightLocked = gyro.getHeading() > targetdeg || gyro.getHeading() > 354 || gyro.getHeading() < 5;
				telemetry.addData("Heading: ", gyro.getHeading());
				telemetry.addData("Target: ", targetdeg);
				telemetry.addData("Locked: ", rightLocked);
			}
		} //right

		if(deg < 0)
		{
			while(gyro.getHeading() > targetdeg || leftLocked)
			{
				waitOneFullHardwareCycle();
				if(leftLocked) leftLocked = gyro.getHeading() < targetdeg || gyro.getHeading() > 354 || gyro.getHeading() < 5;
				telemetry.addData("Heading: ", gyro.getHeading());
				telemetry.addData("Target: ", targetdeg);
				telemetry.addData("Locked: ", leftLocked);
			}
		} //left

		if(verbose) telemetry.addData("", "Done.");

	}

	boolean firstTurn = true;
	public void turnB(String direction, int degrees, double speed) throws InterruptedException // Big thanks to Brendan Chay for providing this code.
	{
		//resetGyro();

		if(direction.equals("Left")) {
			speed *= -1; //Reverses robot direction
			if(firstTurn) {
				firstTurn = false;
				degrees = 360 - degrees - 1; //Corrects for threshold value if first turn
			} else {
				degrees = gyro.getHeading() - degrees - 1;
			}
		} else {
			degrees += gyro.getHeading() + 1;
			if(degrees > 360) degrees -= 360;
		}

		while(Math.abs(gyro.getHeading() - degrees) > 0) { //Robot has not completed turn
			driveRight.setPower(-1 * speed); // // TODO: 2/8/2016  make sure that these go the same way
			driveLeft.setPower(speed);
			if(verbose)telemetry.addData("", gyro.getHeading());
			waitOneFullHardwareCycle();
		}
		waitOneFullHardwareCycle();

		resetGyro();
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
