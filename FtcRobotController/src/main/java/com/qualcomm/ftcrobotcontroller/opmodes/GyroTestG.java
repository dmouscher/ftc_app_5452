package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by RoboticsClub on 2/16/2016.
 */
public class GyroTestG extends LinearBase
{
	public void runOpmode() throws InterruptedException
	{
		initialize();
		waitForStart();

		turnG(90, 0.8);

		Thread.sleep(1000);

		turnG(-180, 0.8);
	}
}
