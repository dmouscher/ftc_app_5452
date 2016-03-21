/*The Teleop op mode is our only teleop op mode
Controls are stated in the comments below*/

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

public class Teleop extends LinearBase
{
	final float   DEADZONE              = 0.200f;
	final double  TRIGGER_THRESHOLD     = 0.700 ;
	final double  ROTATE_SPEED          = 0.850 ;
	final double  DRIVE_SLOW_MULTIPLIER = 0.200 ;
	final double  DRIVE_MED_MULTIPLIER  = 0.500 ;
	final double  EXTEND_SPEED          = 0.990 ;
	final double  FORWARD_SPEED         = 0.900 ;
	final double  BASE_SPEED            = 0.005 ;
	final double  PLOW_SPEED            = 0.500 ;
	final double  WINCH_SPEED           = 1.000 ;
	final double  HOOK_SPEED            = 0.050 ;
	final boolean TELEMETRY             = true  ; //toggles telemetry

	double driveSlowMultiplier = 1;

	boolean isDpadLeftPrimed    = true ;
	boolean isRescueLeftActive  = false;
	boolean isDpadRightPrimed   = true ;
	boolean isRescueRightActive = false;

	boolean driveForwards = true;

	@Override
	public void runOpMode() throws InterruptedException
	{
		initialize();

		gamepad1.setJoystickDeadzone(DEADZONE);
		gamepad2.setJoystickDeadzone(DEADZONE);

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		dropperBase.setPosition(BASE_VERTICAL);

		waitForStart();

		while(opModeIsActive())
		{
			driveSlowMultiplier = gamepad1.left_bumper ? DRIVE_SLOW_MULTIPLIER : gamepad1.right_bumper ? DRIVE_MED_MULTIPLIER : 1;
			driveForwards = !isTriggered(1, Direction.LEFT); //the gamepad1 bumpers limit the speed
			                                                 //of the robot, while gamepad1.left_trigger reverses the drivetrain direction
			driveLeft .setPower((driveForwards ? gamepad1.left_stick_y  : -gamepad1.right_stick_y) * driveSlowMultiplier); //basic tank drive control
			driveRight.setPower((driveForwards ? gamepad1.right_stick_y : -gamepad1.left_stick_y ) * driveSlowMultiplier);

			armRotate.setPower(gamepad2.dpad_up ? ROTATE_SPEED : gamepad2.dpad_down ? -ROTATE_SPEED : 0); //gamepad2.dpad_up/down angles the arm up/down

			if(gamepad2.y ^ gamepad2.b) { armExtend.setPower(EXTEND_SPEED * (gamepad2.y ? 1 : -1)); } // ^ = XOR operator
			else                        { armExtend.setPower(0                                   ); } //todo: add an encoder limit for this conditional

			if(gamepad2.x ^ gamepad2.a) { plow.setPower(PLOW_SPEED * (gamepad2.x ? 1 : -1)); }
			else                        { plow.setPower(0                                 ); } //todo: add an encoder limit for this conditional

			if (gamepad1.y ^ gamepad1.b) //gamepad1.y moves the robot straight and forwards, gamepad1.b moves it straight and backwards
				runAllMotors(FORWARD_SPEED * driveSlowMultiplier * (gamepad1.b ? 1 : -1));

			if(gamepad2.left_bumper ^ isTriggered(2, Direction.LEFT)) //gamepad2.left_bumper extends the base servo, left_trigger retracts it
				dropperBase.setPosition(Range.clip(dropperBase.getPosition() + BASE_SPEED * (gamepad2.left_bumper ? 1 : -1), 0.01, 1));

			if(gamepad2.dpad_left && isDpadLeftPrimed) //Makes it so that trigger happens only on button press, not continuously
			{
				isDpadLeftPrimed = false;
				isRescueLeftActive ^= true; //toggles isRescueLeftActive
			}
			else if(!gamepad2.dpad_left) { isDpadLeftPrimed = true; }

			if(gamepad2.dpad_right && isDpadRightPrimed)
			{
				isDpadRightPrimed = false;
				isRescueRightActive ^= true; //toggles isRescueLeftActive
			}
			else if(!gamepad2.dpad_right) { isDpadRightPrimed = true; }

			rescueLeft .setPosition(isRescueLeftActive  ? RESCUELEFT_OUT : RESCUELEFT_IN);
			rescueRight.setPosition(isRescueRightActive ? RESCUERIGHT_OUT : RESCUERIGHT_IN);

			if     (gamepad2.right_stick_y < -DEADZONE) winch.setPower( WINCH_SPEED);
			else if(gamepad2.right_stick_y >  DEADZONE) winch.setPower(-WINCH_SPEED);
			else                                        winch.setPower( 0          );


			if(gamepad2.left_stick_y < -DEADZONE) { hook.setPosition(Range.clip(hook.getPosition() + HOOK_SPEED, 0, 1)); }
			if(gamepad2.left_stick_y >  DEADZONE) { hook.setPosition(Range.clip(hook.getPosition() - HOOK_SPEED, 0, 1)); }

			if(TELEMETRY) //Shows which buttons are being used currently and which are not being used
			{
				telemetry.addData("Base servo", dropperBase.getPosition());
				telemetry.addData("Joysticks", gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);

				telemetry.addData("Buttons 1", (gamepad1.y                      ? "[1Y] "  : "") + (gamepad1.b            ? "[1B] "  : "") +
					                           (isTriggered(1, Direction.LEFT ) ? "[1LT] " : "")); //Update when button usage changes

				telemetry.addData("Buttons 2", (gamepad2.a                      ? "[2A] "  : "") + (gamepad2.b            ? "[2B] "  : "") +
											   (gamepad2.x                      ? "[2A] "  : "") + (gamepad2.y            ? "[2Y] "  : "") +
											   (gamepad2.left_bumper            ? "[2LB] " : "") + (gamepad2.right_bumper ? "[2RB] " : "") +
											   (gamepad1.dpad_up                ? "[1DU] " : "") + (gamepad1.dpad_down    ? "[1DD] " : "") +
											   (gamepad1.dpad_left              ? "[1DL] " : "") + (gamepad1.dpad_right   ? "[1DR] " : "") +
											   (isTriggered(2, Direction.LEFT ) ? "[2LT] " : "") +
											   (isTriggered(2, Direction.RIGHT) ? "[2RT] " : "")); //Update when button usage changes
			}
			waitOneFullHardwareCycle();
		}
	}

	private void runAllMotors(double speed) // simply runs all drivetrain motors at the given speed
	{
		driveLeft .setPower(speed);
		driveRight.setPower(speed);
	}

	private boolean isTriggered(int gamepad, Direction dir) // returns true if the given trigger has been pressed past the threshold constant
	{                                                       // otherwise returns false
		if (gamepad == 1) { return (dir == Direction.LEFT ? gamepad1.left_trigger : gamepad1.right_trigger) > TRIGGER_THRESHOLD; }
		else              { return (dir == Direction.LEFT ? gamepad2.left_trigger : gamepad2.right_trigger) > TRIGGER_THRESHOLD; }
	}
}