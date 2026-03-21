package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="CarlDiffySwerve", group="CarlDiffySwervePlan")
public class CarlDiffySwerve extends OpMode {

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

        translation = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_y;
        
        telemetry.addData("Translation is:", translation);
        telemetry.addData("Rotation is:", rotation);


        heading = (motors.getTopMotorPos() + motors.getBottomMotorPos()) * 2 * 360;

        telemetry.addData("Heading is:", heading);

        telemetry.addData("Switch is off:", motors.getHomingSwitchState());
        wheelVelocity = motors.getTopMotorVelocity()- motors.getBottomMotorVelocity();
        telemetry.addData("Wheel Velocity is:", wheelVelocity);

        if (!motors.getHomingSwitchState()) {
            motors.resetBottomMotor();
            motors.resetTopMotor();
            telemetry.addData("Motors are:", "Reset!");
        }

        //We need to have a function that when the y button is pressed, it rotates the pod until it hits the switch.
        if (gamepad1.y) {
            isHoming = true;
        }

        if (isHoming && motors.getHomingSwitchState()) {
            rotation = 0.2;
            telemetry.addData("We are:", "Homing!");
        }
        if (isHoming && !motors.getHomingSwitchState()){
            rotation = 0;
            isHoming = false;
            telemetry.addData("We are:", "Not Homing!");
        }

        telemetry.addData("isHoming is", isHoming);

        motors.setTopMotor(translation + rotation);
        motors.setBottomMotor(-translation + rotation);
    }

}





