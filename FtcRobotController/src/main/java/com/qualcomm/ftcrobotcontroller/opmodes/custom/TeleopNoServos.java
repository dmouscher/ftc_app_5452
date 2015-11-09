package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class TeleopNoServos extends LinearOpMode
{
	DcMotor motorFL;
	DcMotor motorFR;
	DcMotor motorBL;
	DcMotor motorBR;

	DcMotor armRotate;
	DcMotor armExtend;
	DcMotor winch;

	//Servo dropperBase;
	//Servo dropperJoint;

	final float   DEADZONE              = 0.20f;
	final double  TRIGGER_THRESHOLD     = 0.70 ;
	final double  ROTATE_SPEED          = 0.85 ;
	final double  MOTOR_SLOW_MULTIPLIER = 0.50 ;
	final double  ARM_SLOW_MULTIPLIER   = 0.50 ;
	final double  EXTEND_SPEED          = 0.85 ; //needs to be done!
	final double  WINCH_SPEED           = 0.05 ;
	final double  FORWARD_SPEED         = 0.85 ;
	/*final double  BASE_SPEED            = 0.20 ;
	final double  JOINT_SPEED           = 0.20 ;*/
	final boolean TELEMETRY             = true ; //enables/disables telemetry
	final int     LEFT                  = 0    ;
	final int     RIGHT                 = 1    ; //poor man's enumeration

	double motorSlowMultiplier = 1;
	double armSlowMultiplier = 1;

	double lastX[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10

	@Override
	public void runOpMode() throws InterruptedException
	{
		motorFL = hardwareMap.dcMotor.get("fl");
		motorFR = hardwareMap.dcMotor.get("fr");
		motorBL = hardwareMap.dcMotor.get("bl");
		motorBR = hardwareMap.dcMotor.get("br");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");
		winch     = hardwareMap.dcMotor.get("winch" );

		/*dropperBase  = hardwareMap.servo.get("base" );
		dropperJoint = hardwareMap.servo.get("joint");*/

		motorFR.setDirection(DcMotor.Direction.REVERSE);
		motorBR.setDirection(DcMotor.Direction.REVERSE);
		winch  .setDirection(DcMotor.Direction.REVERSE);

		gamepad1.setJoystickDeadzone(DEADZONE);
		gamepad2.setJoystickDeadzone(DEADZONE);

		waitForStart();

		while(opModeIsActive())
		{
			motorSlowMultiplier = gamepad1.left_bumper ? MOTOR_SLOW_MULTIPLIER : 1;
			armSlowMultiplier   = gamepad2.a           ? ARM_SLOW_MULTIPLIER   : 1;

			motorFL.setPower(gamepad1.left_stick_y  * motorSlowMultiplier);
			motorBL.setPower(gamepad1.left_stick_y  * motorSlowMultiplier);
			motorFR.setPower(gamepad1.right_stick_y * motorSlowMultiplier);
			motorBR.setPower(gamepad1.right_stick_y * motorSlowMultiplier);

			if     (gamepad1.dpad_up  ) { armRotate.setPower( ROTATE_SPEED * armSlowMultiplier); }
			else if(gamepad1.dpad_down) { armRotate.setPower(-ROTATE_SPEED * armSlowMultiplier); }
			else                        { armRotate.setPower( 0                               ); }

			if(gamepad2.y ^ gamepad2.b)
			{
				armExtend.setPower(FORWARD_SPEED * (gamepad2.y ? 1 : -1));
				winch    .setPower(WINCH_SPEED   * (gamepad2.y ? 1 : -1));
			}

			if((isTriggered(LEFT) || isTriggered(RIGHT)) && (gamepad1.y ^ gamepad1.b))
				{ runAllMotors(FORWARD_SPEED * motorSlowMultiplier * (gamepad1.y ? 1 : -1)); }

			/*if(gamepad2.left_bumper ^ isTriggered(LEFT))
				{ dropperBase.setPosition(BASE_SPEED * (gamepad2.left_bumper ? 1 : -1)); }

			if(gamepad2.right_bumper ^ isTriggered(RIGHT))
				{ dropperJoint.setPosition(JOINT_SPEED * (gamepad2.right_bumper ? 1 : -1)); }*/

			if(TELEMETRY)
			{
				telemetry.addData("Joysticks", gamepad1.left_stick_y+", "+gamepad1.right_stick_y+", "+gamepad2.left_stick_y+", "+gamepad2.right_stick_y);
				telemetry.addData("Buttons 1", (gamepad1.y?"[1Y] ":"")+(gamepad1.b?"[1B] ":"")+(gamepad1.dpad_up?"[1DPU] ":"")+(gamepad1.dpad_down?"[1DPD] ":"")+(gamepad1.left_bumper?"[1LB] ":"")); //Update when button usage changes
				telemetry.addData("Buttons 2", (gamepad2.a?"[2A] ":"")+(gamepad2.b?"[2B] ":"")+(gamepad2.y?"[2Y] ":"")+(gamepad2.left_bumper?"[2LB] ":"")+(gamepad2.right_bumper?"[2RB] ":"")+(isTriggered(LEFT)?"[2LT] ":"")+(isTriggered(RIGHT)?"[2RT] ":"")); //Update when button usage changes
			}

			waitOneFullHardwareCycle();
		}
	}

	private void runAllMotors(double speed)
	{
		motorFL.setPower(speed);
		motorBL.setPower(speed);
		motorFR.setPower(speed);
		motorBR.setPower(speed);
	}

	public double smooth(double input)
	{
		double lastXAvg;
		double sum = 0;

		double[] scaleArray =  //length 17
		{
			0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
			0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00
		};

		for(int i = lastX.length-1; i >= 0; i--) { lastX[i] = i != 0 ? lastX[i - 1] : input; }

		for(int i = 0; i <= lastX.length-1; i++) { sum += lastX[i]; }

		lastXAvg = sum/lastX.length;

		return scaleArray[(int)lastXAvg*(scaleArray.length-1)] * (lastXAvg >= 0 ? 1 : -1);
	}

	public boolean isTriggered(int dir) { return (dir == LEFT ? gamepad1.left_trigger : gamepad1.right_trigger) > TRIGGER_THRESHOLD; }
}