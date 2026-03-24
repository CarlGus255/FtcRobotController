package org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.MotorsCarlCoaxSwervePractice;

public class CarlCoaxSwerveWheelHeading {
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();
    double wheelHeadingRight;
    double wheelHeadingLeft;
    double leftWheelWrapInc;
    double rightWheelWrapInc;
    final double wrapAngle = 270;
    double lastLeftWheelHeading;
    double lastRightWheelHeading;
    final double potToDegrees = wrapAngle/3.13;
    double heading;



    public double getLeftWheelHeading () {
        wheelHeadingLeft = (motors.getLeftPot() * potToDegrees) + (leftWheelWrapInc * wrapAngle);
        if (wheelHeadingLeft - lastLeftWheelHeading < -(wrapAngle/2)) {
            leftWheelWrapInc --;
        } else if (wheelHeadingLeft - lastLeftWheelHeading > (wrapAngle/2)) {
            leftWheelWrapInc ++;
        }
        lastLeftWheelHeading = wheelHeadingLeft;
        return  wheelHeadingLeft;
    }
    public double getRightWheelHeading () {
        wheelHeadingRight = (motors.getRightPot() * potToDegrees) + (rightWheelWrapInc * wrapAngle);
        if (wheelHeadingRight - lastRightWheelHeading < -(wrapAngle/2)) {
            rightWheelWrapInc --;
        } else if (wheelHeadingRight - lastRightWheelHeading > (wrapAngle/2)) {
            rightWheelWrapInc ++;
        }
        lastRightWheelHeading = wheelHeadingRight;
        return  wheelHeadingRight;
    }

    //For quick implementation; this just averages headings. For actual use, power left and right servos independently.
    public double getOverallWheelHeading () {
        heading = (getLeftWheelHeading() + getRightWheelHeading())/2;
        return heading;
    }
}
