// Sets Global.ranAutonomous to true in case a manual change is needed

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

public class RanAutoTrue extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		waitForStart();
		Global.ranAutonomous = true;
	}
}