// Retracts the plow until it is manually stopped
// TODO: Refactor the code; most of it is unnecessary

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class RetractPlow extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		driveLeft  = hardwareMap.dcMotor.get("left");
		driveRight = hardwareMap.dcMotor.get("right");

		armRotate = hardwareMap.dcMotor.get("rotate");
		armExtend = hardwareMap.dcMotor.get("extend");

		plow = hardwareMap.dcMotor.get("plow");

		dropperBase = hardwareMap.servo.get("base");

		rescueLeft  = hardwareMap.servo.get("rql");
		rescueRight = hardwareMap.servo.get("rqr");

		driveRight .setDirection(DcMotor.Direction.REVERSE);
		rescueRight.setDirection(Servo  .Direction.REVERSE);

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);

		dropperBase.setPosition(0.25);

		rescueLeft .setPosition(0);
		rescueRight.setPosition(0);

		plow.setDirection(DcMotor.Direction.REVERSE);
		waitForStart();
		movePlow(-5000, -0.7, 10000);
	}

	public void movePlow(int target, double speed, int waitTime) throws InterruptedException
	{
		plow.setTargetPosition(plow.getCurrentPosition() + target);
		plow.setPower(speed);
		Thread.sleep(waitTime);
	}
}
