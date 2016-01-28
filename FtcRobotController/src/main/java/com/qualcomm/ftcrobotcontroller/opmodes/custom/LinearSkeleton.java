// To start a new op mode off of LinearBase, copy this

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

public class LinearSkeleton extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initalize();
		waitForStart();
	}
}
