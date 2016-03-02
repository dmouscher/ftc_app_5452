// SSSS: Stay still; salute servo    // Our naming conventions are really unwise
//TODO: Decide if we need this program
package com.qualcomm.ftcrobotcontroller.opmodes;

public class SSSS extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initialize();
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		Thread.sleep(5000);
	}
}