package org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.MotorsCarlCoaxSwervePractice;

public class CarlCoaxSwerveFieldCentricPos {
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();

    //Defines translation (Wheel movement) and rotation (Wheel swivel) variables.
    double translation = 0;
    double translationLeft = 0;
    double translationRight = 0;
    double rotationLeft = 0;
    double rotationRight = 0;

    //Set in degrees to use properly with the proportional factor.
    double fieldHeadingLeft = 0;
    double fieldHeadingRight = 0;
    double seekHeading = 0;
    double proportionalfieldHeadingLeft = 0;
    double proportionalfieldHeadingRight = 0;
    double rotationalAcceleration = 1;
    //For testing purposes with button
    boolean lastY = false;
    //Vector input
    double magnitude;
    double direction;
    //to eliminate snapping when letting go of joystick
    double lastDirection;
    //Use to normalize servo potentiometer values
    double transformLeft;
    double lastTransformLeft;
    int incrementLeft;

    double transformRight;
    double lastTransformRight;
    int incrementRight;

    double robotYaw = 0;
    double seekYaw = 0;
    double proportionalYaw = 0;
    double yawAcceleration = 0;
    double yawInput = 0;

    public double runRobotToDirection (double changeX, double changeY) {
        //based on input to change the XY, finds the vector to runTo.
        direction = Math.atan2(changeX, changeY);
        magnitude = Math.min(1.0, (Math.max(0, Math.sqrt((changeX*changeX)+(changeY*changeY))/24)));

        //implements vector
        seekHeading = direction;
        translation = magnitude;
        return seekHeading;
    }

    public double runRobotToTranslation (double changeX, double changeY) {
        //based on input to change the XY, finds the vector to runTo.
        direction = Math.atan2(changeX, changeY);
        magnitude = Math.min(1.0, (Math.max(0, Math.sqrt((changeX*changeX)+(changeY*changeY))/24)));

        //implements vector
        seekHeading = direction;
        translation = magnitude;
        return translation;

    }

}
