package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutonomousSimple extends LinearOpMode
{
	ElapsedTime clock = new ElapsedTime();

	enum Direction { LEFT, RIGHT; }

	int timeslooped = 0;
	int timeslooped2 = 0;

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

	int step = 0;

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

		driveRight.setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);

		driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		plow.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		driveLeft .setMode(DcMotorController.RunMode.RESET_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		plow      .setMode(DcMotorController.RunMode.RESET_ENCODERS);

		telemetry.addData("Flag: ", "0");
		waitForStart();

		telemetry.addData("loop: ", 0);
		dropperBase.setPosition(0.25);
		dropperJoint.setPosition(1.00);
		telemetry.addData("Flag: ", "1");
		telemetry.addData("tl: ", timeslooped);
		//displayTelemetry();
		//movePlow(true);
		while(driveLeft.getTargetPosition() != 50000 && driveRight.getTargetPosition() != 50000 && driveLeft.getCurrentPosition() != 0 && driveRight.getCurrentPosition() != 0)
		{
			driveLeft.setTargetPosition(50000);
			driveRight.setTargetPosition(50000);
			driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
			driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
			telemetry.addData("loop: ", 1);
		}
int testnum = 2;
		do
		{
			driveLeft.setPower(0.85);
			driveRight.setPower(0.85);
			telemetry.addData("L C/T: ", driveLeft.getCurrentPosition() + ", " + driveLeft.getTargetPosition());
			telemetry.addData("R C/T: ", driveRight.getCurrentPosition() + ", " + driveRight.getTargetPosition());
			telemetry.addData("loop: ", testnum);
			waitOneFullHardwareCycle();
			testnum = 3;
			telemetry.addData("loop: ", testnum);
		}
		while(driveLeft.getCurrentPosition() < 15972 && driveRight.getCurrentPosition() < 15972);

		driveLeft.setPower(0);
		driveRight.setPower(0);
		telemetry.addData("loop: ", 4);
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

	public void moveAll(double speed, double in) throws InterruptedException
	{
		resetEncoders();

		//driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		//driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		while(driveLeft.getTargetPosition() != (int) (in * TICKS_PER_INCH) + (driveLeft .getCurrentPosition()) && driveRight.getTargetPosition() != (int) (in * TICKS_PER_INCH) + (driveRight .getCurrentPosition()))
		{
			driveLeft.setTargetPosition((int) (in * TICKS_PER_INCH) + (driveLeft.getCurrentPosition()));
			driveRight.setTargetPosition((int) (in * TICKS_PER_INCH) + (driveRight.getCurrentPosition()));
			timeslooped2++;
			telemetry.addData("tl2: ", timeslooped2);
		}
		telemetry.addData("tl: ", timeslooped);
		do
		{
			driveLeft .setPower(speed);
			driveRight.setPower(speed);
			waitOneFullHardwareCycle();
			telemetry.addData("L C/T: ", driveLeft.getCurrentPosition() + ", " + driveLeft.getTargetPosition());
			telemetry.addData("R C/T: ", driveRight.getCurrentPosition() + ", " + driveRight.getTargetPosition());
			timeslooped++;
			telemetry.addData("tl: ", timeslooped);
		}
		while((driveLeft.getTargetPosition() > driveLeft.getCurrentPosition()) && (driveRight.getTargetPosition() > driveRight.getCurrentPosition()));


		telemetry.addData("tl: ", timeslooped);
		driveLeft .setPower(0);
		driveRight.setPower(0);
		waitOneFullHardwareCycle();
	}

	public void turnLeft(double speed, double deg)  throws InterruptedException // Negative values are left, positive are right.
	{
		resetEncoders();

		//driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		//driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft.setTargetPosition((int) (-deg * TICKS_PER_DEGREE) + driveLeft .getCurrentPosition());
		driveRight.setTargetPosition((int) (deg * TICKS_PER_DEGREE) + driveRight.getCurrentPosition());

		do
		{
			driveLeft .setPower(-speed);
			driveRight.setPower( speed);
			waitOneFullHardwareCycle();
		}
		while((driveLeft.getTargetPosition() < driveLeft.getCurrentPosition()) && (driveRight.getTargetPosition() > driveRight.getCurrentPosition()));

		driveLeft .setPower(0);
		driveRight.setPower(0);
		waitOneFullHardwareCycle();
	}

	public void turnRight(double speed, double deg)  throws InterruptedException // Negative values are left, positive are right.
	{
		resetEncoders();

		//driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		//driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft.setTargetPosition((int) (deg * TICKS_PER_DEGREE) + (driveLeft .getCurrentPosition()));
		driveRight.setTargetPosition((int) -(deg * TICKS_PER_DEGREE) + (driveRight.getCurrentPosition()));

		do
		{
			driveLeft .setPower( speed);
			driveRight.setPower(-speed);
			waitOneFullHardwareCycle();
		}
		while((driveLeft.getTargetPosition() > driveLeft.getCurrentPosition()) && (driveRight.getTargetPosition() < driveRight.getCurrentPosition()));

		driveLeft .setPower(0);
		driveRight.setPower(0);
		waitOneFullHardwareCycle();
	}

	public void movePlow(boolean extend) throws InterruptedException
	{
		plow.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		plow.setTargetPosition((10000 * (extend ? -1 : 1)) + (plow.getCurrentPosition()));
		plow.setPower(0.85 * (extend ? -1 : 1));
		while(plow.getTargetPosition() > plow.getCurrentPosition()){}
		displayTelemetry();
		waitOneFullHardwareCycle();
	}

	public void dump() throws InterruptedException // Make sure to add pulling back of the dumping "arm" to the end of this method
	{
		dropperBase.setPosition(0.7);
		for(int i=0;i<10000;i++){}
		dropperBase.setPosition(0.25);
		displayTelemetry();
		waitOneFullHardwareCycle();
	}

	public void displayTelemetry()
	{
		step =+ 1;
		telemetry.addData("Encoder Pos", driveLeft.getCurrentPosition() + ", " + driveRight.getCurrentPosition() + ", " + step);
	}

	public void resetEncoders() throws InterruptedException
	{
		while(driveLeft.getCurrentPosition() != 0 && driveRight.getCurrentPosition() != 0)
		{
			telemetry.addData("tl: ", timeslooped);
			driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
			driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		}
		waitOneFullHardwareCycle();
		telemetry.addData("tl: ", timeslooped);
	}
}
