// To start a new op mode off of LinearBase, copy this

package com.qualcomm.ftcrobotcontroller.opmodes;

public class LinearSkeleton extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initalize();
		waitForStart();
	}
}
