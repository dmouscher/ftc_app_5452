package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class OtherThreads implements Runnable
{
	DcMotor plow;

	private Thread t;
	private String threadName = "lol";

	final int PLOW_EXTEND_LENGTH_TICK = 9001;

	public OtherThreads(DcMotor plow)
	{ this.plow = plow; }

	public void run() // what gets run
	{
		plow.setPower(0.75);
		plow.setTargetPosition(PLOW_EXTEND_LENGTH_TICK);
		//plow.setPower(0); // Make sure this doesn't cause any problems and actually stops
	}

	public void start()
	{
		t = ((t==null)?(new Thread (this, threadName)):(t));
		plow.setMode((DcMotorController.RunMode.RUN_USING_ENCODERS));
		t.start ();
	}
}
