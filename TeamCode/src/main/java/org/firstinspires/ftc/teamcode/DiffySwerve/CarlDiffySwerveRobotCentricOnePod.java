package org.firstinspires.ftc.teamcode.DiffySwerve;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp(name="CarlDiffySwerveRobotCentricOnePod", group="CarlDiffySwervePlan")
public class CarlDiffySwerveRobotCentricOnePod extends OpMode {

    double translation = 0;
    double rotation = 0;
    double heading = 0;
    double wheelVelocity = 0;
    boolean isHoming = false;
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

    MotorsCarlDiffySwerve motors = new MotorsCarlDiffySwerve();

    @Override
    public void init() {
        motors.init(hardwareMap);
        motors.resetTopMotor();
        motors.resetBottomMotor();
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
        seekHeading = lastDirection;

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
        heading = (motors.getTopMotorPos() + motors.getBottomMotorPos()) * 2 * 360;
        //print heading value
        telemetry.addData("Heading is:", heading);

        //testing switch
        telemetry.addData("Switch is off:", motors.getHomingSwitchState());

        //get the forward velocity of the wheel
        wheelVelocity = motors.getTopMotorVelocity()- motors.getBottomMotorVelocity();
        telemetry.addData("Wheel Velocity is:", wheelVelocity);


        //We need to have a function that when the y button is pressed, it rotates the pod until it hits the switch.
        if (gamepad1.y) {
            isHoming = true;
        }

        if (isHoming && motors.getHomingSwitchState()) {
            //starts the homing sequence if we are in homing mode and switch is not depressed (Not at home yet)
            rotation = 0.1;
            telemetry.addData("We are:", "Homing!");
        }
        if (isHoming && !motors.getHomingSwitchState()){
            //stops the homing sequence if we are in homing mode but the switch gets depressed (Now at home)
            //stop rotation
            rotation = 0;
            isHoming = false;

            //reset motors
            motors.resetBottomMotor();
            motors.resetTopMotor();
            //reset heading so it doesn't snap to old target heading
            seekHeading = 0;
            lastDirection = 0;

            //telemetry for sanity's sake
            telemetry.addData("Motors are:", "Reset!");
            telemetry.addData("We are:", "Not Homing!");
        }



        //tells if you are in the homing sequence or not on gamepad
        telemetry.addData("isHoming is", isHoming);

        //set motor power based on translation and rotation values
        motors.setTopMotor((translation * wheelPolarity) + rotation);
        motors.setBottomMotor(-(translation * wheelPolarity) + rotation);
    }

}