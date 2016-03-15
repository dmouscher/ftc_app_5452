// Base class for our custom op modes, contains functions for initialization and encoder usage

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearBase extends LinearOpMode
{
	enum Direction { LEFT, RIGHT }

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

	ModernRoboticsI2cGyro gyro;

	final double DEG = (2900*(360.0/370))/90;
	final double IN  = 144.796;
	final double FT  = 12*IN;

	final double BASE_RESTING    = 0.00  ; // fits inside the sizing cube
	final double BASE_VERTICAL   = 0.47  ;
	final double BASE_DUMPING    = 0.93  ;
	final double RESCUELEFT_IN   = 0.28  ;
	final double RESCUERIGHT_IN  = 0     ;
	final double RESCUELEFT_OUT  = 1     ;
	final double RESCUERIGHT_OUT = 0.775 ;
	final double HOOK_RESTING    = 0     ;

	final int PLOW_EXTEND_LENGTH = 3800;

	final double DEGTRUE_MULTIPLIER = 0.9;

	boolean verbose = false;

	int gyroDistance = 0;

	public void mapHardware()
	{
		hardwareMap.logDevices(); // IDK what this does, but it its in every example program

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

		gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo  .Direction.REVERSE);
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

	public void initialize() throws InterruptedException
	{
		if(verbose) telemetry.addData("", "Initializing");
		mapHardware();
		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		resetServos();
		resetEncoders();
		resetGyro();
		if(verbose) telemetry.addData("", "Initialization complete");
	}

	public void moveTarget(double dist, double speed, int waitTime) throws InterruptedException
	{
		int idist = (int)dist;

		driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist);
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + idist);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void turn(int deg, double speed, int waitTime) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int) (-deg * DEG));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int) (deg * DEG));

		driveLeft .setPower(-speed);
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void movePlow(double speed, int waitTime) throws InterruptedException
	{
		plow.setPower(speed);
		while(opModeIsActive() && waitTime == 0) { waitOneFullHardwareCycle(); } // Pauses indefinitely if 0 is entered for waitTime
		Thread.sleep(waitTime);
		plow.setPower(0);
	}

	public void movePlow() // Hopefully this works
	{
		OtherThreads t = new OtherThreads(plow);
		t.start();
	}

	public void halt() throws InterruptedException
	{
		driveLeft .setPower(0);
		driveRight.setPower(0);
		waitOneFullHardwareCycle();
	}

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
		while(gyro.isCalibrating()) { Thread.sleep(50); }

		if(verbose) telemetry.addData("", "Gyro sensor reset");
	}

	@Override
	public void runOpMode() throws InterruptedException {}
}