package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by RoboticsClub on 12/9/2015.
 */
public class EncoderRobotTest extends LinearOpMode
{
	ElapsedTime clock = new ElapsedTime();

	enum Direction { LEFT, RIGHT; }

	DcMotor driveLeft;
	DcMotor driveRight;

	DcMotor armRotate;
	DcMotor armExtend;

	DcMotor plow;

	Servo dropperBase;

	Servo rescueLeft;
	Servo rescueRight;

	//GyroSensor gyro;

	//final int     SMOOTH_LENGTH         = 10    ;
	final boolean TELEMETRY             = true  ; //enables/disables telemetry
	final double TICKS_PER_DEGREE       = 2900/90.0 ;
	final double TICKS_PER_INCH         = 1000/6.375;

	int step = 0;

	//double lastXLeft [] = new double[SMOOTH_LENGTH];
	//double lastXRight[] = new double[SMOOTH_LENGTH];

	public void runOpMode()
	{
		driveLeft  = hardwareMap.dcMotor.get("left" );
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");

		dropperBase  = hardwareMap.servo.get("base" );

		rescueLeft   = hardwareMap.servo.get("rql"  );
		rescueRight  = hardwareMap.servo.get("rqr"  );

		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveRight.setTargetPosition(1440);
		driveLeft.setTargetPosition(1440);

		telemetry.addData("Encoder Values", driveRight.getCurrentPosition()+", "+driveLeft.getCurrentPosition());

		driveRight.setPower(0.8);
		driveLeft.setPower(0.8);
	}
}
