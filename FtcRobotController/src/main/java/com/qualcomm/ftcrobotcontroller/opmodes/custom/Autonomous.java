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
	final double TICKS_PER_DEGREE       = 2900/90.0 ;
	final double TICKS_PER_INCH         = 1000/6.375;

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
		plow.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		plow      .setMode(DcMotorController.RunMode.RESET_ENCODERS);

		waitForStart();

		dropperBase.setPosition(0.25);
		dropperJoint.setPosition(1.00);

		displayTelemetry(); // haven't tested with telemetry
		//movePlow(true);
		moveAll(0.85, 101.82);
		turn(0.85, 45);
		moveAll(0.85, 24);
		dump();
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

	public void moveAll(double speed, double in)
	{
		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft .setTargetPosition((int)(in*TICKS_PER_INCH)+(driveLeft.getCurrentPosition()));
		driveRight.setTargetPosition((int)(in*TICKS_PER_INCH)+(driveRight.getCurrentPosition()));

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while(driveLeft.isBusy() || driveRight.isBusy()){}
		displayTelemetry();
	}

	public void turn(double speed, double deg) // Negative values are left, positive are right.
	{
		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft .setTargetPosition((int)( deg*TICKS_PER_DEGREE)+(driveLeft.getCurrentPosition()));
		driveRight.setTargetPosition((int)(-deg*TICKS_PER_DEGREE)+(driveRight.getCurrentPosition()));

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while(driveLeft.isBusy() || driveRight.isBusy()){}
		displayTelemetry();
	}

	public void movePlow(boolean extend)
	{
		plow.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		plow.setTargetPosition((10000 * (extend ? -1 : 1))+(plow.getCurrentPosition()));
		plow.setPower(0.85 * (extend ? -1 : 1));
		while(plow.isBusy()){}
		displayTelemetry();
	}

	public void dump() // Make sure to add pulling back of the dumping "arm" to the end of this method
	{
		dropperBase.setPosition(0.7);
		for(int i=0;i<10000;i++){}
		dropperBase.setPosition(0.25);
		displayTelemetry();
	}

	public void displayTelemetry()
	{
		telemetry.addData("Encoder Pos", driveLeft.getCurrentPosition()+ ", " + driveRight.getCurrentPosition());
	}
}
