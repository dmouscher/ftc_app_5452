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

	final int     SMOOTH_LENGTH         = 10    ;
	final float   DEADZONE              = 0.200f;
	final double  TRIGGER_THRESHOLD     = 0.700 ;
	final double  ROTATE_SPEED          = 0.850 ;
	final double  DRIVE_SLOW_MULTIPLIER = 0.500 ;
	final double  ARM_SLOW_MULTIPLIER   = 0.500 ;
	final double  EXTEND_SPEED          = 0.990 ;
	final double  FORWARD_SPEED         = 0.900 ;
	final double  BASE_SPEED            = 0.005 ;
	final double  JOINT_SPEED           = 0.010 ;
	final double  PLOW_SPEED            = 0.500 ;
	final boolean TELEMETRY             = true  ; //enables/disables telemetry

	double driveSlowMultiplier = 1;
	double armSlowMultiplier   = 1;

	boolean isBumperPrimed      = true ;
	boolean isRescueLeftActive  = false;
	boolean isTriggerPrimed     = true ;
	boolean isRescueRightActive = false;

	double lastXLeft [] = new double[SMOOTH_LENGTH];
	double lastXRight[] = new double[SMOOTH_LENGTH];

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

		gamepad1.setJoystickDeadzone(DEADZONE);
		gamepad2.setJoystickDeadzone(DEADZONE);

		driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		EncoderSpeed ES = new EncoderSpeed(driveLeft, driveRight);

        waitForStart();

        ES.start();

		dropperBase .setPosition(0.25);
		dropperJoint.setPosition(1.00);

		while(opModeIsActive())
		{
			driveSlowMultiplier = gamepad1.left_bumper ? DRIVE_SLOW_MULTIPLIER : 1; //gamepad1.left_bumper triggers slow mode for motors
			armSlowMultiplier   = gamepad2.a           ? ARM_SLOW_MULTIPLIER   : 1; //gamepad2.a does the same for the arm

			driveLeft .setPower(smooth(gamepad1.left_stick_y  * driveSlowMultiplier, lastXLeft )); //basic tank drive control
			driveRight.setPower(smooth(gamepad1.right_stick_y * driveSlowMultiplier, lastXRight));

			if     (gamepad2.dpad_up  ) { armRotate.setPower( ROTATE_SPEED * armSlowMultiplier); } //gamepad2.dpad_up/down angles the arm up/down
			else if(gamepad2.dpad_down) { armRotate.setPower(-ROTATE_SPEED * armSlowMultiplier); }
			else                        { armRotate.setPower( 0                               ); }

			if(gamepad2.y ^ gamepad2.b) { armExtend.setPower(EXTEND_SPEED * (gamepad2.y ? 1 : -1)); } //todo: readd comments for printout
			else { armExtend.setPower(0); } //todo: add an encoder limit for this conditional

			if(gamepad2.x ^ gamepad2.a) { plow.setPower(PLOW_SPEED * (gamepad2.a ? 1 : -1)); } //todo: add comments for printout
			else { plow.setPower(0); } //todo: add an encoder limit for this conditional

			if(gamepad1.y ^ gamepad1.b) //gamepad1.y moves the robot straight and forwards, gamepad1.b moves it straight and backwards
				runAllMotors(FORWARD_SPEED * driveSlowMultiplier * (gamepad1.b ? 1 : -1));

			if(gamepad2.left_bumper ^ isTriggered(2, Direction.LEFT)) //gamepad2.left_bumper extends the base servo, gamepad2.left_trigger retracts it
				dropperBase.setPosition(Range.clip(dropperBase.getPosition() + BASE_SPEED * (gamepad2.left_bumper ? 1 : -1), 0.125, 1));

			if(gamepad2.right_bumper ^ isTriggered(2, Direction.RIGHT)) //gamepad2.right_bumper extends the joint servo, gamepad2.right_trigger retracts it
				dropperJoint.setPosition(Range.clip(dropperJoint.getPosition() + JOINT_SPEED * (gamepad2.right_bumper ? 1 : -1), 0, 1));

			if(gamepad1.right_bumper && isBumperPrimed)
			{
				isBumperPrimed = false;
				isRescueLeftActive ^= true;
			}
			else if(!gamepad1.right_bumper) { isBumperPrimed = true; }

			if(isTriggered(1, Direction.RIGHT) && isTriggerPrimed)
			{
				isTriggerPrimed = false;
				isRescueRightActive ^= true;
			}
			else if(!isTriggered(1, Direction.RIGHT)) { isTriggerPrimed = true; }

			rescueLeft .setPosition(isRescueLeftActive  ? 0.5 : 0.0);
			rescueRight.setPosition(isRescueRightActive ? 1.0 : 0.5);

			if(TELEMETRY) //Shows which buttons are being used currently and which are not being used
			{
				telemetry.addData("Joysticks", gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);

				telemetry.addData("Buttons 1", (gamepad1.y            ? "[1Y] "  : "") + (gamepad1.b            ? "[1B] "  : "") +
						                       (gamepad1.left_bumper  ? "[1LB] " : "") + (gamepad1.right_bumper ? "[1RB] " : "") +
						                       (isTriggered(1, Direction.RIGHT) ? "[1RT] " : "")); //Update when button usage changes

				telemetry.addData("Buttons 2", (gamepad2.a            ? "[2A] "  : "") + (gamepad2.b           ? "[2B] "  : "") +
						                       (gamepad2.y            ? "[2Y] "  : "") + (gamepad2.left_bumper ? "[2LB] " : "") +
						                       (gamepad2.right_bumper ? "[2RB] " : "") + (gamepad1.dpad_up     ? "[1DU] " : "") +
						                       (gamepad1.dpad_down    ? "[1DD] " : "") +
						                       (isTriggered(2, Direction.LEFT ) ? "[2LT] " : "") +
						                       (isTriggered(2, Direction.RIGHT) ? "[2RT] " : "")); //Update when button usage changes

                telemetry.addData("Real Speed (clicks per second)", " R:" + ES.getRealSpeed(EncoderSpeed.motorList.DRIVERIGHT) +
						                                            " L:" + ES.getRealSpeed(EncoderSpeed.motorList.DRIVELEFT ));
			}

            waitOneFullHardwareCycle();
		}

        ES.terminate();
	}

	private void runAllMotors(double speed) //simply runs all drivetrain motors at the given speed
	{
		driveLeft.setPower(speed);
		driveRight.setPower(speed);
	}

	public double smooth(double input, double lastX[]) //todo: implement PI/PID
	{                                                  //currently takes the average of the last ten input
		double sum = 0;                                // in the future will scale output so that the driver can drive the robot more precisely at slow speeds

		for(int i = lastX.length-1; i >= 0; i--) { lastX[i] = (i != 0) ? lastX[i - 1] : input; }
		// Put the latest value into slot 0 and move all the values up a slot

		for(int i = 0; i <= lastX.length-1; i++) { sum += lastX[i]; }
		// Add all the values from the last ten array into one variable

		return sum/lastX.length;
	}

	public boolean isTriggered(int gamepad, Direction dir) //returns true if the given trigger has been pressed past the threshold constant
	{                                                //otherwise returns false
		if(gamepad == 1) { return (dir == Direction.LEFT ? gamepad1.left_trigger : gamepad1.right_trigger) > TRIGGER_THRESHOLD; }
		else             { return (dir == Direction.LEFT ? gamepad2.left_trigger : gamepad2.right_trigger) > TRIGGER_THRESHOLD; }
	}
}