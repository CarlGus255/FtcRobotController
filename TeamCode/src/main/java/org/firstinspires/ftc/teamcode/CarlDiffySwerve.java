package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="CarlDiffySwerve", group="CarlDiffySwervePlan")
public class CarlDiffySwerve extends OpMode {

    double translation = 0;
    double rotation = 0;
    double heading = 0;



    MotorsCarlDiffySwerve motors = new MotorsCarlDiffySwerve();

    @Override
    public void init() {
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {

        translation = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_y;

        if (gamepad1.x) {
            rotation =  0.2;
        }
        telemetry.addData("Translation is:", translation);
        telemetry.addData("Rotation is:", rotation);

        motors.setBottomMotor(translation + rotation);
        motors.setTopMotor(-translation + rotation);

        heading = (motors.getTopMotorPos() + motors.getBottomMotorPos()) * 2 * 360;

        telemetry.addData("Heading is:", heading);

        telemetry.addData("Switch is off:", motors.getHomingSwitchState());



    }

}





