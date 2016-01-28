// This class can be used to store information in between op modes
// This isn't ideal Java, but it's the easiest implementation of a pseudo-global variable
// Currently the only information stored in Global is whether or not an autonomous program has ran

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

public class Global
{
	public static boolean ranAutonomous = false;
}
