//Experimental autonomous where RUN_TO_POSITION mode was not used
//More often than not it just caused robot controller to crash

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutonomousTest extends LinearOpMode
{
	ElapsedTime clock = new ElapsedTime();

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

	int initLeft  = 0;
	int initRight = 0;

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

		telemetry.addData("Flag: ", "0");
		waitForStart();

		dropperBase.setPosition(0.25);
		dropperJoint.setPosition(1.00);
		telemetry.addData("Flag: ", "1");
		//displayTelemetry(); // Step (the third value) should be 1
		//movePlow(true);
		moveAll(0.85, 101.82); // Step 2
		telemetry.addData("Flag: ", "2");
		double time1 = clock.time();

		while(clock.time() < time1 + 5){}
		telemetry.addData("Flag: ", "3");
		//turnRight(0.85, 45); // Step 3
		telemetry.addData("Flag: ", "4");
		moveAll(0.85, 24); // Step 4
		telemetry.addData("Flag: ", "5");
		while(opModeIsActive()){}
		//wait(1000);
		//moveAll(-0.85, 24);
		//dump(); // Step 5
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
		initLeft  = driveLeft .getCurrentPosition();
		initRight = driveRight.getCurrentPosition();

		telemetry.addData("Flag: ", "2.1");

		while(initLeft + (int)(in * TICKS_PER_INCH) > driveLeft.getCurrentPosition() || initRight + (int)(in * TICKS_PER_INCH) > driveRight.getCurrentPosition())
		{
			driveLeft .setPower(speed);
			driveRight.setPower(speed);

			telemetry.addData("Flag: ", "2.2"); //The robot was stock in this loop in this op mode did run
		}

		telemetry.addData("Flag: ", "2.3");

		driveLeft.setPower(0);
		driveRight.setPower(0);

		telemetry.addData("Flag: ", "2.4");
	}

	public void turnLeft(double speed, double deg) // Negative values are left, positive are right.
	{
		driveLeft .setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

		driveLeft .setTargetPosition((int)( deg*TICKS_PER_DEGREE)+(driveLeft.getCurrentPosition()));
		driveRight.setTargetPosition((int)(-deg*TICKS_PER_DEGREE)+(driveRight.getCurrentPosition()));

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while((driveLeft.getTargetPosition() > driveLeft.getCurrentPosition()) || (driveRight.getTargetPosition() > driveRight.getCurrentPosition())){}
		//displayTelemetry();
	}

	public void turnRight(double speed, double deg) // Negative values are left, positive are right.
	{
		initLeft  = driveLeft .getCurrentPosition() + ((int) (deg * TICKS_PER_DEGREE) + (driveLeft .getCurrentPosition()));

		while(initLeft > driveLeft.getCurrentPosition())
		{
			driveLeft .setPower( speed);
			driveRight.setPower(-speed);
		}

		driveLeft .setPower(0);
		driveRight.setPower(0);
	}

	public void movePlow(boolean extend)
	{
		plow.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		plow.setTargetPosition((10000 * (extend ? -1 : 1)) + (plow.getCurrentPosition()));
		plow.setPower(0.85 * (extend ? -1 : 1));
		while(plow.getTargetPosition() > plow.getCurrentPosition()){}
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
		telemetry.addData("Encoder Pos", driveLeft.getCurrentPosition() + ", " + driveRight.getCurrentPosition()+", ");
	}
}
