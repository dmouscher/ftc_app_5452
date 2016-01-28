package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderSpeed extends Thread
{
	public DcMotor driveLeft, driveRight;
	public double speedLeft, speedRight = 0;
	public double lastSpeedLeft, lastSpeedRight = 0;

	public enum motorList { DRIVELEFT, DRIVERIGHT; }
	public boolean dontStop = true;

	public EncoderSpeed(DcMotor driveLeft, DcMotor driveRight) // parametrized constructor
	{
		this.driveLeft = driveLeft;
		this.driveRight = driveRight;
	}

	public void terminate() { dontStop = false; } // stops the thread

	public double getRealSpeed(motorList motor)
	{
		switch (motor)
		{
			case DRIVELEFT:
				return speedLeft;
			case DRIVERIGHT:
				return speedRight;
		}

		return 1.1;
		/**
		 * It returns this for two reasons:
		 * 1 Java is stupid and needs a return statement because the switch statement might not have returned something (but it always will unless someone messed up the code)
		 * 2 If someone messed up in the code, the value will be above 1 and motor.setSpeed() will through an error.
		 */
	}

	public void run() // The actual function were motor speed is calculated
	{
		long lastNum = System.currentTimeMillis();

		while (dontStop)
		{
			while (System.currentTimeMillis() == lastNum){} // wait one second
			lastNum = System.currentTimeMillis();
			speedLeft  = (driveLeft .getDirection() == DcMotor.Direction.FORWARD) ? (1) : (-1) * (Math.abs(driveLeft .getCurrentPosition()) - Math.abs(lastSpeedLeft )) / 1000; // now speed is distance/time, but this is just one second so dividing my 1 is a waste of time
			speedRight = (driveRight.getDirection() == DcMotor.Direction.FORWARD) ? (1) : (-1) * (Math.abs(driveRight.getCurrentPosition()) - Math.abs(lastSpeedRight)) / 1000;

			lastSpeedLeft  = speedLeft;
			lastSpeedRight = speedRight;
		}
	}
}
