// Sets Global.ranAutonomous to true in case a manual change is needed

package com.qualcomm.ftcrobotcontroller.opmodes;

public class RanAutoTrue extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		waitForStart();
		com.qualcomm.ftcrobotcontroller.opmodes.Global.ranAutonomous = true;
	}
}