package com.qualcomm.ftcrobotcontroller.opmodes;

public class GyroTest extends LinearBase
{
	public void moveEn(int dist, double speed) throws InterruptedException // TODO: Find a way to move forward when the motors are set to DcMotorController.RunMode.RUN_USING_ENCODERS motor mode
	{
		int startingLeft  = driveLeft.getCurrentPosition();
		int startingRight = driveRight.getCurrentPosition();

		driveLeft .setPower(speed);
		driveRight.setPower(speed);

		while (driveLeft.getCurrentPosition() - startingLeft < dist || driveRight.getCurrentPosition() - startingRight < dist)
		{
			if(verbose) telemetry.addData("M: ", driveLeft.getCurrentPosition() + ", " + driveRight.getCurrentPosition());

			waitOneFullHardwareCycle();
		}
	}

	public void turnGyro(int deg, double speed) throws InterruptedException // Pos Values, turn right
	{
		int gyroinit = gyro.getIntegratedZValue();
		int loopnum = 0;
		int degtrue = (int)(-DEGTRUE_MULTIPLIER * deg);

		driveLeft .setPower(speed * (degtrue > 0 ? 1 : -1));
		driveRight.setPower(speed * (degtrue < 0 ? 1 : -1));

		while(degtrue > 0 ? gyroinit + degtrue > gyro.getIntegratedZValue() : gyroinit + degtrue < gyro.getIntegratedZValue())
		{
			telemetry.addData("Gyroinit: ", gyroinit);
			telemetry.addData("IZ-Value", gyro.getIntegratedZValue());
			telemetry.addData("Loop #: ", loopnum);
			loopnum++;
			waitOneFullHardwareCycle();
		}

		halt();
	}

	public int gyroDelta() { return gyroDistance - gyro.getIntegratedZValue(); }
	public void resetDelta() { gyroDistance = gyro.getIntegratedZValue(); }

	public void runOpMode() throws InterruptedException
	{
		verbose = true;
		initialize();
		telemetry.addData("Stage: ", 0);
		waitForStart();
		telemetry.addData("Stage: ", 1);
		turnGyro(30, 0.25); // turn right 30 degrees
		telemetry.addData("Stage: ", 2);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 3);
		turnGyro(90, 0.25); // turn right 90 degrees
		telemetry.addData("Stage: ", 4);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 5);
		turnGyro(270, 0.25); // turn right 270 degrees
		telemetry.addData("Stage: ", 6);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 7);
		turnGyro(90, 0.25); // turn right 90 degrees
		telemetry.addData("Stage: ", 8);
		Thread.sleep(1000);
		telemetry.addData("Stage: ", 9);
	}
}
