// DFFB = Dump from Floor, Far, Blue

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;


/**
 * Created by jackiehirsch on 12/14/15.
 */
public class DFFB extends LinearOpMode {

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

		driveRight.setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);
		plow.setDirection(DcMotor.Direction.REVERSE);

		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		dropperBase .setPosition(0.267);
		dropperJoint.setPosition(1);

		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);

		waitForStart();

		dropperBase.setPosition(0.518);   // Raise up the climber-dropper
		movePlow(0.75, 9500);             // Extend the plow
		moveForward(8.5 * FT, 0.7, 5000); // Move 8.5 feet forwards
		//moveForward(FT, 0.7, 1000);
		//dropperBase.setPosition(0.1);   // Dump
		Thread.sleep(1000);
	}

	// TODO: Shunt the custom autonomous methods into their own file so they don't have to be implemented like eight times

	public void moveForward(double dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates
	{                                                                                            // TODO: the amount of time the program
		int idist = (int)dist;                                                                   // TODO: should wait based dist and speed.
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist/**TICKS_PER_INCH*/); // Increase the target position
		driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + idist/**TICKS_PER_INCH*/);   // These should reset the encoders but
		// we add the current position value as a precaution
		driveLeft .setPower(speed); // Runs the motors
		driveRight.setPower(speed);

		Thread.sleep(waitTime); // Waits for the motors to finish the set amount of time
	}                           // Setting waitTime to too short will halt the movement early, but setting it for too long will cause
	                            // the robot to stay in place, wasting time

	public void turn(int deg, double speed, int waitTime) throws InterruptedException // Positive deg = right turn, negative deg = left turn
	{
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int)(-1*deg*DEG)); // Reverse left motors' target position
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int)(deg*DEG));

		driveLeft .setPower(-1 * speed); // Reverse left motors' speed
		driveRight.setPower(speed);

		Thread.sleep(waitTime);
	}

	public void movePlow(double speed, int waitTime) throws InterruptedException //Encoders aren't working on the plow, so this is purely time-based
	{
		plow.setPower(speed);   //Starts plow
		Thread.sleep(waitTime); //Waits a bit
		plow.setPower(0);       //Stops plow
	}
}
