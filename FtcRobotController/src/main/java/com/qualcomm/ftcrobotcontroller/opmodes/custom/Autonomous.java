package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class Autonomous extends LinearOpMode
{
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

	//GyroSensor gyro;

	//final int     SMOOTH_LENGTH         = 10    ;
	final boolean TELEMETRY             = true  ; //enables/disables telemetry
	final int ONE_ROTATION              = 1440  ; // For encoder use //todo: replace with inches
	final double TICKS_PER_DEGREE = 1; //placeholder

	//double lastXLeft [] = new double[SMOOTH_LENGTH];
	//double lastXRight[] = new double[SMOOTH_LENGTH];

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

		//gyro = hardwareMap.gyroSensor.get("gyro");

		driveRight.setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);

		driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		dropperBase.setPosition(0.25);
		dropperJoint.setPosition(1.00);

		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		waitForStart();

		moveAll(0.85, ONE_ROTATION);  // Just a note, these values are experimental, is 85 the best speed? is one full rotation of the motors far enough to move away from the wall to turn? or is it too much?
		turn(0.85, 10*ONE_ROTATION);              // On the paper it looks like we are starting our back bumper with the wall, we need to turn the robot to face our goal
		moveAll(0.85, 5*ONE_ROTATION);// Move to the Red Alliance Rescue Beacon Repair Zone
		turn(0.85, -10*ONE_ROTATION);
		moveAll(0.85, ONE_ROTATION);
	}

	/*public double smooth(double input, double lastX[]) //todo: implement PI/PID
	{                                                  //currently takes the average of the last ten input
	double sum = 0;                                // in the future will scale output so that the driver can drive the robot more precisely at slow speeds

	for(int i = lastX.length-1; i >= 0; i--) { lastX[i] = (i != 0) ? lastX[i - 1] : input; }
	// Put the latest value into slot 0 and move all the values up a slot

	for(int i = 0; i <= lastX.length-1; i++) { sum += lastX[i]; }
	// Add all the values from the last ten array into one variable

	return sum/lastX.length;
	}*/

	public void moveAll(double speed, int distance)
	{
		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft .setTargetPosition(distance);
		driveRight.setTargetPosition(distance);

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while(driveLeft.isBusy() || driveRight.isBusy()){}
	}

	public void turn(double speed, double deg) // Negative values are left, positive are right.
	{
		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft .setTargetPosition((int)( deg*TICKS_PER_DEGREE));
		driveRight.setTargetPosition((int)(-deg*TICKS_PER_DEGREE));

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while(driveLeft.isBusy() || driveRight.isBusy()){}
	}

	public void dump() // Make sure to add pulling back of the dumping "arm" to the end of this method
	{

	}
}
