package org.firstinspires.ftc.teamcode.DiffySwerve.OldPractice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DiffySwerve.MotorsCarlDiffySwerve;

@TeleOp(name="CarlDiffySwervePod", group="CarlDiffySwerve")
public class CarlDiffySwervePod extends OpMode {

    double translation = 0;
    double rotation = 0;
    double heading = 0;
    double wheelVelocity = 0;
    boolean isHoming = false;



    MotorsCarlDiffySwerve motors = new MotorsCarlDiffySwerve();

    @Override
    public void init() {
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {
        //link translation and rotation variables to gamepad
        translation = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_y;

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