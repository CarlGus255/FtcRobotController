package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CarlCoaxSwervePracticeRobotCentric extends OpMode {

    //Defines translation (Wheel movement) and rotation (Wheel swivel) variables.
    double translation = 0;
    double translationLeft = 0;
    double translationRight = 0;
    double rotationLeft = 0;
    double rotationRight = 0;

    //Set in degrees to use properly with the proportional factor.
    double headingLeft = 0;
    double headingRight = 0;
    double seekHeading = 0;
    double proportionalHeadingLeft = 0;
    double proportionalHeadingRight = 0;
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


    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();

    @Override
    public void init() {
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {
        /*
        turns left joystick into vector input. This is the direction value. It is normalized so straight up on the gamepad is 0
        degrees, then increases going counter-clockwise up to 360.
         */
        if (gamepad1.left_stick_x < 0){
            direction = ((Math.atan2((-gamepad1.left_stick_x), -((gamepad1.left_stick_y)))))*180/3.14;
        } else {
            direction = (3.14 - Math.atan2((-gamepad1.left_stick_x),(gamepad1.left_stick_y)))*180/3.14;
        }

        /*
        turns the joystick into vector input. This is the magnitude value, and is derived from how far forward the joystick is moved,
        and limited from 0 to 1, as there were troubles with having it go very slightly more than one, by 0.01 or so. We don't actually
        ever have a case where this would be negative, as that is impossible with this formula because of the square root.
         */

        magnitude = Math.min(1.0, Math.max(0, Math.sqrt((gamepad1.left_stick_y*gamepad1.left_stick_y)+(gamepad1.left_stick_x*gamepad1.left_stick_x))));

        if (magnitude > 0.1) {
            lastDirection = direction;
        }

        translation = magnitude;
        seekHeading = lastDirection;



        //normalizes potentiometer values
        transformLeft = motors.getLeftPot();
        if (lastTransformLeft - transformLeft > (270/2)){
            // if it shifted half a rotation positive in one go, it will increment the transform counter(increment)
            incrementLeft++;
        } else if (lastTransformLeft - transformLeft < -(270/2)) {
            // if it shifted half a rotation negative in one go, it will decrement the transform counter(increment)
            incrementLeft --;
        }
        lastTransformLeft = motors.getLeftPot();

        transformRight = motors.getRightPot();
        if (lastTransformRight - transformRight > (270/2)){
            // if it shifted half a rotation positive in one go, it will increment the transform counter(increment)
            incrementRight++;
        } else if (lastTransformRight - transformRight < -(270/2)) {
            // if it shifted half a rotation negative in one go, it will decrement the transform counter(increment)
            incrementRight --;
        }
        lastTransformRight = motors.getRightPot();
        
        //tracks heading
        headingLeft = motors.getLeftPot() + (270*incrementLeft);
        headingRight = motors.getRightPot() + (270*incrementRight);
        //defines and tracks proportionalHeading
        proportionalHeadingLeft = seekHeading - headingLeft;
        proportionalHeadingRight = seekHeading - headingRight;
        //implements proportionalHeading with rotation
        rotationLeft = proportionalHeadingLeft*rotationalAcceleration/360;
        rotationRight = proportionalHeadingRight*rotationalAcceleration/360;

        //Seek the shortest path
        while (proportionalHeadingLeft > 180) {
            proportionalHeadingLeft -= 360;
        }
        while (proportionalHeadingLeft < -180) {
            proportionalHeadingLeft += 360;
        }

        while (proportionalHeadingRight > 180) {
            proportionalHeadingRight -= 360;
        }
        while (proportionalHeadingRight < -180) {
            proportionalHeadingRight += 180;
        }

        //Change robot yaw using gamepad
        yawInput = -gamepad1.right_stick_y;
        //make around zero more stable
        if (Math.abs(yawInput) < 0.05) {
            yawInput = 0;
        }
        //implement yaw change (Once per loop)
        seekYaw = seekYaw + (yawInput*3);
        //Need to use odometry to get robotYaw!
        proportionalYaw = seekYaw - robotYaw;
        //implement changes to robot yaw
        translationLeft = translation - (proportionalYaw*yawAcceleration)/360;
        translationRight = translation + (proportionalYaw*yawAcceleration)/360;

        //powers the module
        motors.runLeftMotor(translationLeft);
        motors.runLeftServo(rotationLeft);
        motors.runRightMotor(translationRight);
        motors.runRightServo(rotationRight);
    }
}
