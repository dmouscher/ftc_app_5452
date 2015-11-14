/*The Teleop op mode is our only teleop op mode
Controls are stated in the comments below*/

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Teleop extends LinearOpMode
{
	DcMotor motorFL;
	DcMotor motorFR;
	DcMotor motorBL;
	DcMotor motorBR;

	DcMotor armRotate;
	DcMotor armExtend;
	//DcMotor winch; //No winch for this tournament

	Servo dropperBase;
	Servo dropperJoint;
	Servo rescueLeft;
	Servo rescueRight;

	final float   DEADZONE              = 0.200f;
	final double  TRIGGER_THRESHOLD     = 0.700 ;
	final double  ROTATE_SPEED          = 0.850 ;
	final double  MOTOR_SLOW_MULTIPLIER = 0.500 ;
	final double  ARM_SLOW_MULTIPLIER   = 0.500 ;
	final double  EXTEND_SPEED          = 0.990 ;
	//final double  WINCH_SPEED           = 0.850 ;
	final double  FORWARD_SPEED         = 0.900 ;
	final double  BASE_SPEED            = 0.005 ;
	final double  JOINT_SPEED           = 0.010 ;
	final boolean TELEMETRY             = true  ; //enables/disables telemetry
	final int     LEFT                  = 0     ;
	final int     RIGHT                 = 1     ; //poor man's enumeration

	double motorSlowMultiplier = 1;
	double armSlowMultiplier = 1;

	boolean isBumperPrimed      = true ;
	boolean isRescueLeftActive  = false;
	boolean isTriggerPrimed     = true ;
	boolean isRescueRightActive = false;

	double lastXLeft[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10
	double lastXRight[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10 //lastXLeft.length() should always equal lastXRight.length()

	@Override
	public void runOpMode() throws InterruptedException
	{
		motorFL = hardwareMap.dcMotor.get("fl");
		motorFR = hardwareMap.dcMotor.get("fr");
		motorBL = hardwareMap.dcMotor.get("bl");
		motorBR = hardwareMap.dcMotor.get("br");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");
		//winch     = hardwareMap.dcMotor.get("winch" );

		dropperBase  = hardwareMap.servo.get("base" );
		dropperJoint = hardwareMap.servo.get("joint");
		rescueLeft   = hardwareMap.servo.get("rql"  );
		rescueRight  = hardwareMap.servo.get("rqr"  );

		motorFR.setDirection(DcMotor.Direction.REVERSE);
		motorBR.setDirection(DcMotor.Direction.REVERSE);
		//	winch  .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo.Direction.REVERSE);

		gamepad1.setJoystickDeadzone(DEADZONE);
		gamepad2.setJoystickDeadzone(DEADZONE);

		motorFL.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFR.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBL.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBR.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		waitForStart();

		dropperBase.setPosition(0.25);
		dropperJoint.setPosition(1);

		while(opModeIsActive())
		{
			motorSlowMultiplier = gamepad1.left_bumper ? MOTOR_SLOW_MULTIPLIER : 1; //gamepad1.left_bumper triggers slow mode for motors
			armSlowMultiplier   = gamepad2.a           ? ARM_SLOW_MULTIPLIER   : 1; //gamepad2.a does the same for the arm

			motorFL.setPower(smoothLeft (gamepad1.left_stick_y  * motorSlowMultiplier)); //basic tank drive control
			motorBL.setPower(smoothLeft (gamepad1.left_stick_y  * motorSlowMultiplier));
			motorFR.setPower(smoothRight(gamepad1.right_stick_y * motorSlowMultiplier));
			motorBR.setPower(smoothRight(gamepad1.right_stick_y * motorSlowMultiplier));

			if     (gamepad2.dpad_up  ) { armRotate.setPower( ROTATE_SPEED * armSlowMultiplier); } //gamepad2.dpad_up/down angles the arm up/down
			else if(gamepad2.dpad_down) { armRotate.setPower(-ROTATE_SPEED * armSlowMultiplier); }
			else                        { armRotate.setPower( 0                               ); }

			if(gamepad2.y ^ gamepad2.b) //xor functions are used so nothing funky happens when both buttons are pressed at the same time
			{
				armExtend.setPower(EXTEND_SPEED * (gamepad2.y ? 1 : -1)); //gamepad2.y extends the arm, gamepad2.b retracts it
				//winch    .setPower(WINCH_SPEED  * (gamepad2.y ? 1 : -1));
			}
			else { armExtend.setPower(0); } //todo: add an encoder limit for this conditional

			if(gamepad1.y ^ gamepad1.b) //gamepad1.y moves the robot straight and forwards, gamepad1.b moves it straight and backwards
				{ runAllMotors(FORWARD_SPEED * motorSlowMultiplier * (gamepad1.y ? 1 : -1)); }

			if(gamepad2.left_bumper ^ isTriggered(2, LEFT)) //gamepad2.left_bumper extends the base servo, gamepad2.left_trigger retracts it
				{ dropperBase.setPosition(Range.clip(dropperBase.getPosition() + BASE_SPEED * (gamepad2.left_bumper ? 1 : -1), 0.125, 1)); }

			if(gamepad2.right_bumper ^ isTriggered(2, RIGHT)) //gamepad2.right_bumper extends the joint servo, gamepad2.right_trigger retracts it
				{ dropperJoint.setPosition(Range.clip(dropperJoint.getPosition() + JOINT_SPEED * (gamepad2.right_bumper ? 1 : -1), 0, 1)); }

			if(gamepad1.right_bumper && isBumperPrimed)
			{
				isBumperPrimed = false;
				isRescueLeftActive ^= true;
			}
			else if(!gamepad1.right_bumper) { isBumperPrimed = true; }

			if(isTriggered(1, RIGHT) && isTriggerPrimed)
			{
				isTriggerPrimed = false;
				isRescueRightActive ^= true;
			}
			else if(!isTriggered(1, RIGHT)) { isTriggerPrimed = true; }

			rescueLeft .setPosition(isRescueLeftActive  ? 0.5 : 0.0);
			rescueRight.setPosition(isRescueRightActive ? 1.0 : 0.5);

			if(TELEMETRY) //Shows which buttons are being used currently and which are not being used
			{
				telemetry.addData("Joysticks", gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);

				telemetry.addData("Buttons 1", (gamepad1.y            ? "[1Y] "  : "") + (gamepad1.b            ? "[1B] "  : "") +
						                       (gamepad1.left_bumper  ? "[1LB] " : "") + (gamepad1.right_bumper ? "[1RB] " : "") +
						                       (isTriggered(1, RIGHT) ? "[1RT] " : "")); //Update when button usage changes

				telemetry.addData("Buttons 2", (gamepad2.a            ? "[2A] "  : "") + (gamepad2.b           ? "[2B] "  : "") +
						                       (gamepad2.y            ? "[2Y] "  : "") + (gamepad2.left_bumper ? "[2LB] " : "") +
	                  					       (gamepad2.right_bumper ? "[2RB] " : "") + (isTriggered(2, LEFT) ? "[2LT] " : "") +
						                       (isTriggered(2, RIGHT) ? "[2RT] " : "") + (gamepad1.dpad_up     ? "[1DU] " : "") +
						                       (gamepad1.dpad_down    ? "[1DD] " : "")); //Update when button usage changes

				//todo: add actual motor values to telemetry
			}

			waitOneFullHardwareCycle();
		}
	}

	private void runAllMotors(double speed) //simply runs all drivetrain motors at the given speed
	{
		motorFL.setPower(speed);
		motorBL.setPower(speed);
		motorFR.setPower(speed);
		motorBR.setPower(speed);
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