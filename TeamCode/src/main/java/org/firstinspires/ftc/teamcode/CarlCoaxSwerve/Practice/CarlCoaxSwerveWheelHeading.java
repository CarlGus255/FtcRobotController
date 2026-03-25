package org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.MotorsCarlCoaxSwervePractice;

public class CarlCoaxSwerveWheelHeading {
    MotorsCarlCoaxSwervePractice motors;

    public CarlCoaxSwerveWheelHeading(MotorsCarlCoaxSwervePractice motors) {
        this.motors = motors;
    }
    double wheelHeadingRight;
    double wheelHeadingLeft;
    int leftWheelWrapInc;
    int rightWheelWrapInc;
    final double wrapAngle = 270;
    double lastLeftWheelHeading;
    double lastRightWheelHeading;
    final double potToDegrees = wrapAngle/3.13;
    double heading;
    double rawLeft;
    double lastRawLeft;
    double deltaLeft;
    double rawRight;
    double lastRawRight;
    double deltaRight;
    public double getLeftWheelHeading () {
        rawLeft = motors.getLeftPot();
        deltaLeft = rawLeft - lastRawLeft;
        if (deltaLeft > wrapAngle/2) {
            leftWheelWrapInc--;
        } else if (deltaLeft < -wrapAngle/2) {
            leftWheelWrapInc++;
        }
        wheelHeadingLeft = (rawLeft + (leftWheelWrapInc * wrapAngle));
        lastRawLeft = rawLeft;
        
        return  wheelHeadingLeft;
    }
    public double getRightWheelHeading () {
        rawRight = motors.getRightPot();
        deltaRight = rawRight - lastRawRight;
        if (deltaRight > wrapAngle/2) {
            rightWheelWrapInc--;
        } else if (deltaRight < -wrapAngle/2) {
            rightWheelWrapInc++;
        }
        wheelHeadingRight = rawRight + (rightWheelWrapInc * wrapAngle);
        lastRawRight = rawRight;

        return  wheelHeadingRight;
    }

    //For quick implementation; this just averages headings. For actual use, power left and right servos independently.
    public double getOverallWheelHeading () {
        heading = (getLeftWheelHeading() + getRightWheelHeading())/2;
        return heading;
    }
}
