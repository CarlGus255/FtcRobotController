package org.firstinspires.ftc.teamcode.teamcode;

public class DiffySwervePodDriver {
    PIDFImplement leftRotationPID;
    ActuatorsSensorsDiffySwerve motors;

    //Translation Power Values
    double leftTranslation;
    double rightTranslation;

    //Rotation Power Values
    double leftRotation;
    double rightRotation;

    //Heading Values
    double leftHeading;
    double rightHeading;
    double headingScaleFactor = ((double) 40 /60)*360/2; //Converts from motor encoders to degrees for heading

    //Seek Heading Values
    double seekLeftHeading;
    double seekRightHeading;
    //Homing Values
    boolean leftHomingSwitch;
    boolean leftHomedOnce;
    boolean leftHasHomedOnce;
    boolean leftHomedTwice;
    boolean leftHeadingFactor;


    public void init (ActuatorsSensorsDiffySwerve motors, PIDFImplement leftRotationPID) {
        this.motors = motors;
        this.leftRotationPID = leftRotationPID;
        leftRotationPID.setConstants(0.005,0,0, 0,0.001);
    }
    //Experimental setting translation and rotation
    public void setTranslation (double translation) {
        leftTranslation = translation;
        rightTranslation = leftTranslation;
    }
    public double getLeftTranslation() {
        return leftTranslation;
    }
    public void setRotation (double rotation) {
        leftRotation = rotation;
        rightRotation = leftRotation;
    }
    public double getLeftRotation() {
        return leftRotation;
    }
    public void readHeading() {
        leftHeading = (motors.readLeftTopMotorEncoder() + motors.readLeftBottomMotorEncoder())*headingScaleFactor;
        rightHeading = (motors.readRightTopMotorEncoder() + motors.readRightBottomMotorEncoder())*headingScaleFactor;
    }
    public void setHeading(double deltaTime) {
        leftRotation = Math.max(-0.5, Math.min(0.5, leftRotationPID.PIDFOutPut(seekLeftHeading - leftHeading, deltaTime)));
    }
    public double manualChangeLeftRotation (double change) {
        seekLeftHeading += change;
        return seekLeftHeading;
    }
    public void driveMotors () {
        motors.driveLeftTopMotor(leftTranslation + leftRotation);
        motors.driveLeftBottomMotor(-leftTranslation + leftRotation);
        motors.driveRightTopMotor(rightTranslation + rightRotation);
        motors.driveRightBottomMotor(-rightTranslation + rightRotation);
    }
    public double getLeftHeading () {
        return leftHeading;
    }
    public double getRightHeading () {
        return rightHeading;
    }

    public double getSeekLeftHeading() {
        return seekLeftHeading;
    }
    public double getSeekRightHeading() {
        return seekRightHeading;
    }
    double specRatio;
    public void homeLeftPodTwoStep(boolean homingSwitch) {
        leftHomingSwitch = !homingSwitch;
        if (leftHomingSwitch && !leftHomedOnce) {
            leftRotation = 0.1;
        }
        if (!leftHomingSwitch && !leftHomedOnce) {
            leftRotation = 0;
            leftHomedTwice = false;
            leftHomedOnce = true;
            motors.resetLeftEncoders();
        }
        if (leftHomedTwice && !leftHomedTwice) {
            leftRotation = 0.1;
        }
        if (!leftHomedTwice && !leftHomedTwice) {
            leftRotation = 0;
            specRatio = getLeftHeading()/360;
        }
    }
    public double getSpecRatio () {
        return specRatio;
    }
}
