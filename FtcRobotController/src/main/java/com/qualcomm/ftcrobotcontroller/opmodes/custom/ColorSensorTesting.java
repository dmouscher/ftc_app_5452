//This op mode has been and is used to test the Modern Robotics color sensor

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by mlowery2 on 10/20/2015.
 */
public class ColorSensorTesting extends LinearOpMode
{
    ColorSensor colorSensor;
    TouchSensor touch;

    @Override
    public void runOpMode() throws InterruptedException
    {
        hardwareMap.logDevices();
        colorSensor = hardwareMap.colorSensor.get("mr");
        touch = hardwareMap.touchSensor.get("btn");

        waitForStart();

        float hsvValues[] = {0, 0, 0};
        final float values[] = hsvValues;
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        while (opModeIsActive())
        {

            colorSensor.enableLed(!touch.isPressed());

            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

            //telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            //telemetry.addData("Hue", hsvValues[0]);

            relativeLayout.post(new Runnable()
            {
                public void run()
                {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            //waitOneFullHardwareCycle();
        }
    }
}
