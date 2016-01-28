// Sets Global.ranAutonomous to false in case a manual change is needed

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

public class RanAutoFalse extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		waitForStart();
		Global.ranAutonomous = false;
	}
}