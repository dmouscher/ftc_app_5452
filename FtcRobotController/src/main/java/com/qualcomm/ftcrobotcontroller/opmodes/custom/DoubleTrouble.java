/*This is a teleop test mode designed to test the two motors/one port design*/

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class DoubleTrouble extends LinearOpMode
{
	//DcMotor left;
	DcMotor right;

	final float   DEADZONE              = 0.200f;
	final double  TRIGGER_THRESHOLD     = 0.700 ;
	final double  FORWARD_SPEED         = 0.900 ;
	final int     LEFT                  = 0     ;
	final int     RIGHT                 = 1     ; //poor man's enumeration

	double lastXLeft[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10
	double lastXRight[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10 //lastXLeft.length() should always equal lastXRight.length()

	@Override
	public void runOpMode() throws InterruptedException
	{
	//	left = hardwareMap.dcMotor.get("left");
		right = hardwareMap.dcMotor.get("right");


		gamepad1.setJoystickDeadzone(DEADZONE);
		gamepad2.setJoystickDeadzone(DEADZONE);

	//	left .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		right.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		waitForStart();

		while(opModeIsActive())
		{
	//		left .setPower(smoothLeft (gamepad1.left_stick_y ));
			right.setPower(smoothRight(gamepad1.right_stick_y));

			if(gamepad1.y ^ gamepad1.b) //gamepad1.y moves the robot straight and forwards, gamepad1.b moves it straight and backwards
				{ runAllMotors(FORWARD_SPEED * (gamepad1.y ? 1 : -1)); }

			telemetry.addData("Joysticks", gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);

			waitOneFullHardwareCycle();
		}
	}

	private void runAllMotors(double speed) //simply runs all drivetrain motors at the given speed
	{
	//	left .setPower(speed);
		right.setPower(speed);
	}


	public double smoothLeft(double input) //todo: fix scaling
	{                                      //currently takes the average of the last ten inputs
		double lastXAvg;                   //in the future will scale output so that the driver can drive the robot more precisely at slow speeds
		double sum = 0;

		/*double[] scaleArray =  //length 17
				{
						0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
						0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00
				};*/

		for(int i = lastXLeft.length-1; i >= 0; i--) { lastXLeft[i] = (i != 0) ? lastXLeft[i - 1] : input; }
		// Put the latest value into slot 0 and move all the values up a slot

		for(int i = 0; i <= lastXLeft.length-1; i++) { sum += lastXLeft[i]; }
		// Add all the values from the last ten array into one variable

		lastXAvg = sum/lastXLeft.length;
		// Take the average and store it into a variable

		return lastXAvg;
	}

	public double smoothRight(double input) //Also this should really be one method, I'm sorry.
	{                                       //todo: merge smoothLeft() and smoothRight()
		double lastXAvg;
		double sum = 0;

		/*double[] scaleArray =  //length 17
				{
						0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
						0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00
				};*/

		for(int i = lastXRight.length-1; i >= 0; i--) { lastXRight[i] = (i != 0) ? lastXRight[i - 1] : input; }
		// Put the latest value into slot 0 and move all the values up a slot

		for(int i = 0; i <= lastXRight.length-1; i++) { sum += lastXRight[i]; }
		// Add all the values from the last ten array into one variable

		lastXAvg = sum/lastXRight.length;
		// Take the average and store it into a variable

		return lastXAvg;
	}

	public boolean isTriggered(int gamepad, int dir) //returns true if the given trigger has been pressed past the threshold constant
	{                                                //otherwise returns false
		if(gamepad == 1) { return (dir == LEFT ? gamepad1.left_trigger : gamepad1.right_trigger) > TRIGGER_THRESHOLD; }
		else             { return (dir == LEFT ? gamepad2.left_trigger : gamepad2.right_trigger) > TRIGGER_THRESHOLD; }
	}
}