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
public class Autonomous extends LinearOpMode {

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

	final double TICKS_PER_DEGREE       = 2900/90.0 ;
	final double TICKS_PER_INCH         = 1000/6.375;

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

		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		dropperBase .setPosition(0.25);
		dropperJoint.setPosition(1.00);

		waitForStart();

		telemetry.addData("Phase 1", "");
		moveForward(1440, 0.8);

		telemetry.addData("Phase 2", "");
		turn(90, 0.8);

		telemetry.addData("Phase 3", "");
		moveForward(1440, 0.8);

		telemetry.addData("Phase 4", "");
		telemetry.addData("Encoder Pos: ", "L: " + driveLeft.getCurrentPosition() + ", R: " + driveRight.getCurrentPosition());

	}

	public void moveForward(int dist, double speed) throws InterruptedException
	{
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + dist/**TICKS_PER_INCH*/);
		driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + dist/**TICKS_PER_INCH*/);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		Thread.sleep(1000);
	}

	public void turn(int deg, double speed) throws InterruptedException
	{
		driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int)(-1*deg*TICKS_PER_DEGREE));
		driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int) (deg * TICKS_PER_DEGREE));

		driveLeft .setPower(-1 * speed);
		driveRight.setPower(speed);

		Thread.sleep(1000);
	}
}
