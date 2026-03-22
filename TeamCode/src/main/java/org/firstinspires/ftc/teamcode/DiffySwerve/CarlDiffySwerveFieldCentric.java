package org.firstinspires.ftc.teamcode.DiffySwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CarlDiffySwerveFieldCentric extends OpMode {
        double translation = 0;
        double rotation = 0;
        double heading = 0;
        double wheelVelocity = 0;
        boolean isHomingLeft = false;
        boolean isHomingLeftComplete = false;
        boolean isHomingRight = false;
        boolean isHomingRightComplete = false;
        //proportional heading, seek heading, and rotation acceleration allow
        double seekHeading = 0;
        double proportionalHeading = 0;
        double rotationAcceleration = 2;
        //direction and magnitude are vector inputs for wheel control.
        double direction = 0;
        double magnitude = 0;
        //last direction keeps it from jumping back to 0 degrees if joystick is let go
        double lastDirection = 0;
        //wheel polarity is used if we want to flip wheel direction in order to minimize necessary rotation change
        int wheelPolarity = 1;

        //for changing yaw, used for 2 pods
        double robotYaw = 0;
        double seekYaw = 0;
        double translationLeft = 0;
        double translationRight = 0;
        double proportionalYaw = 0;
        double yawAcceleration = 0.2;
        double distanceLeftWheel = 0;
        double distanceRightWheel = 0;
        double rotationRight = 0;
        double rotationLeft = 0;
        boolean lastX = false;
        double fieldHeading = 0;
        boolean lastB = false;
        //for testing field centric with only one pod
        double yawInc;


        MotorsCarlDiffySwerve2Pod motors = new MotorsCarlDiffySwerve2Pod();

        @Override
        public void init() {
            motors.init(hardwareMap);
            motors.resetLeftTopMotor();
            motors.resetLeftBottomMotor();
            telemetry.addData("Ticks per rev LT:", motors.LeftTopMotorTicksPerRev);
            telemetry.addData("Ticks per Rev LB:", motors.LeftBottomMotorTicksPerRev);
            telemetry.addData("Ticks per rev RT:", motors.RightTopMotorTicksPerRev);
            telemetry.addData("Ticks per rev RB:", motors.RightBottomMotorTicksPerRev);
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

            //prints the direction and magnitude
            telemetry.addData("direciton is", direction);
            telemetry.addData("magnitude is:", magnitude);
            telemetry.addData("Last Direction is:", lastDirection);

            //inputs the vector to the rest of the program
            translation = magnitude;

            //seekHeading is what steers the wheel, lastDirection is the direction variable
            fieldHeading = lastDirection + robotYaw;
            seekHeading = fieldHeading;

            //proportionally seeks the direction
            proportionalHeading = seekHeading - heading;



            //Seek the shortest path
            while (proportionalHeading > 180) {
                proportionalHeading -= 360;
            }
            while (proportionalHeading < -180) {
                proportionalHeading += 360;
            }

            wheelPolarity = 1;
/*
        //might want to comment this out btw, it makes it less smooth in reality. Essentially detects when it would be better to flip wheel, but it's not good for continous wheel rotation at all.
        if (proportionalHeading > 90) {
            proportionalHeading -= 180;
            wheelPolarity = -1;
            //flips wheel spin direction instead of having to rotate pod every time
        } else if (proportionalHeading < -90) {
            proportionalHeading += 180;
            wheelPolarity = -1;
            //flips wheel spin direction instead of having to rotate pod every time
        }
*/

            //prints the wheel polarity
            telemetry.addData("Wheel Polarity is:", wheelPolarity);

            //implements heading seeking
            rotation = (proportionalHeading * rotationAcceleration)/360;

            telemetry.addData("Seek heading is:", seekHeading);
            telemetry.addData("proportional gain is:", proportionalHeading);

            //print rotation and translation values
            telemetry.addData("Translation is:", translation);
            telemetry.addData("Rotation is:", rotation);

            //get the current wheel heading in degrees
            heading = (motors.getLeftTopMotorPos() + motors.getLeftBottomMotorPos()) * 2 * 360;
            //print heading value
            telemetry.addData("Heading is:", heading);

            //get the forward velocity of the wheel
            wheelVelocity = motors.getLeftTopMotorVelocity()- motors.getLeftBottomMotorVelocity();
            telemetry.addData("Wheel Velocity is:", wheelVelocity);

            //Set each wheel's rotation
            rotationLeft = rotation;
            rotationRight = rotation;


            //We need to have a function that when the y button is pressed, it rotates the pod until it hits the switch.
            if (gamepad1.y) {
                isHomingLeft = true;
                isHomingRight = true;
            }

            if (isHomingLeft && motors.getLeftHomingSwitchState()) {
                //starts the homing sequence if we are in homing mode and switch is not depressed (Not at home yet)
                rotationLeft = 0.1;
            }
            if (isHomingLeft && !motors.getLeftHomingSwitchState()){
                //stops the homing sequence if we are in homing mode but the switch gets depressed (Now at home)
                //stop rotation
                rotationLeft = 0;
                isHomingLeft = false;
                isHomingLeftComplete = true;

                //reset motors
                motors.resetLeftBottomMotor();
                motors.resetLeftTopMotor();
            }

        /*
        if (isHomingRight && motors.getRightHomingSwitchState()){
            //starts the homing sequence if we are in homing mode and switch is not depressed (Not at home yet)
            rotationRight = 0.1;
        }
        if (isHomingRight && !motors.getRightHomingSwitchState()) {
            //stops the homing sequence if we are in homing mode but the switch gets depressed (Now at home)
            //stop rotation
            rotationRight = 0;
            isHomingRight = false;
            isHomingRightComplete = true;

            //reset motors
            motors.resetRightBottomMotor();
            motors.resetRightTopMotor();

        }
        */
            if (isHomingRight){
                //starts the homing sequence if we are in homing mode and switch is not depressed (Not at home yet)
                rotationRight = 0.1;
            }

            //Use this until we have a second pod
            if (gamepad1.x && !lastX) {
                //stops the homing sequence if we are in homing mode but the switch gets depressed (Now at home)
                //stop rotation
                rotationRight = 0;
                isHomingRight = false;
                isHomingRightComplete = true;

                //reset motors
                motors.resetRightBottomMotor();
                motors.resetRightTopMotor();

            }
            lastX = gamepad1.x;
            if (isHomingLeftComplete && isHomingRightComplete) {
                //reset heading so it doesn't snap to old target heading
                seekHeading = 0;
                lastDirection = 0;
                isHomingLeftComplete = false;
                isHomingRightComplete = false;
            }

            if (!lastB && gamepad1.b) {
                yawInc --;
            }
            lastB = gamepad1.b;
            //Implements robot yaw changes
            proportionalYaw = seekYaw - robotYaw;
            translationLeft = translation;// + (proportionalYaw * yawAcceleration);
            translationRight = translation;// - (proportionalYaw * yawAcceleration);

            //Tracks robot yaw
            //Note: in the motors class, the ticks per rev was wrong. it was set at  2786 ticks per rev instaed of 384.5, so as a
            //temporary solution, I am normalizing just these values. A bigger to do is to correct this and retune the rest of it.
            distanceLeftWheel = (motors.getLeftTopMotorPos() - motors.getLeftBottomMotorPos())*2786/384.5;
            distanceRightWheel = (motors.getRightTopMotorPos() - motors.getRightBottomMotorPos())*2786/384.5;
            //Use the following to run robot and comment out the actual distanceRightWheel for testing until we have a second pod
            //distanceRightWheel = distanceLeftWheel;

            //distancewheel variables are in revolutions. They are 72mm wheels. Assuming a wheel distace of 14in, or 355.6mm, that would
            //mean that one wheel revolution difference is a change in angle, using arctan, of 11.4 degrees.
            robotYaw =  (distanceLeftWheel - distanceRightWheel + yawInc)*11.4;

            telemetry.addData(("Left Wheel distance is:"), distanceLeftWheel);
            telemetry.addData("Right Wheel distance is:", distanceRightWheel);

            //prints yaw data
            telemetry.addData("Seek Yaw is:", seekYaw);
            telemetry.addData("Proportional Yaw is:", proportionalYaw);
            telemetry.addData("Robot Yaw is:", robotYaw);
            telemetry.addData("Left Translation is:", translationLeft);



            //set motor power based on translation and rotation values
            motors.setLeftTopMotor((translationLeft * wheelPolarity) + rotationLeft);
            motors.setLeftBottomMotor(-(translationLeft * wheelPolarity) + rotationLeft);
            motors.setRightTopMotor((translationRight * wheelPolarity)+ rotationRight);
            motors.setRightBottomMotor((translationRight * wheelPolarity) + rotationRight);
        }

    }