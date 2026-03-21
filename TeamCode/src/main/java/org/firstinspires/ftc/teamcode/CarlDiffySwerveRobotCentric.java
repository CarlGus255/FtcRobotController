package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="CarlDiffySwerveRobotCentric", group="CarlDiffySwervePlan")
public class CarlDiffySwerveRobotCentric extends OpMode {

    double translation = 0;
    double rotation = 0;
    double heading = 0;
    double wheelVelocity = 0;
    boolean isHoming = false;
    double seekHeading = 0;
    double proportionalHeading = 0;
    double rotationAcceleration = 1;
    double headingDifference = 0;
    //direction and magnitude are vector inputs for wheel control.
    double direction = 0;
    double magnitude = 0;





    MotorsCarlDiffySwerve motors = new MotorsCarlDiffySwerve();

    @Override
    public void init() {
        motors.init(hardwareMap);
        motors.resetTopMotor();
        motors.resetBottomMotor();
    }

    @Override
    public void loop() {

        //turns left joystick into vector input (Something goofy is going on with magnitude but oh well)
        if (gamepad1.left_stick_y <= 0){
            direction = (Math.abs(Math.atan2((gamepad1.left_stick_y),(gamepad1.left_stick_x))))*180/3.14;
        } else {
            direction = (6.28 - (Math.abs(Math.atan2((gamepad1.left_stick_y),(gamepad1.left_stick_x)))))*180/3.14;
        }
        magnitude = Math.sqrt((gamepad1.left_stick_y*gamepad1.left_stick_y)+(gamepad1.left_stick_x*gamepad1.left_stick_x));

        //prints the direction and magnitude
        telemetry.addData("direciton is", direction);
        telemetry.addData("magnitude is:", magnitude);

        //inputs the vector to the rest of the program
        translation = magnitude;
        seekHeading = direction;

        proportionalHeading = seekHeading - heading;
        //seeks towards the smaller difference in wheel heading
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
            seekHeading=0;

            //telemetry for sanity's sake
            telemetry.addData("Motors are:", "Reset!");
            telemetry.addData("We are:", "Not Homing!");
        }



        //tells if you are in the homing sequence or not on gamepad
        telemetry.addData("isHoming is", isHoming);

        //set motor power based on translation and rotation values
        motors.setTopMotor(translation + rotation);
        motors.setBottomMotor(-translation + rotation);
    }

}