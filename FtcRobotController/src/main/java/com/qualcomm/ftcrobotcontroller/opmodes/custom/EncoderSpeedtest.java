package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class EncoderSpeedtest extends LinearOpMode
{
	DcMotor motor;
	DcMotorController dmc;

	double lastDelta;
	double lastDeltas[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //length 10

	@Override
	public void runOpMode() throws InterruptedException
	{
		dmc.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
		motor.setPower(0.5);

		while(opModeIsActive())
		{
			lastDelta = lastDeltas[0] - dmc.getMotorCurrentPosition(1);
			for(int i = lastDeltas.length-1; i >= 0; i--) { lastDeltas[i] = (i != 0) ? lastDeltas[i - 1] : lastDelta; }
			dmc.getMotorCurrentPosition(1);
		}
	}
}
