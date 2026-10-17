package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class DiffySwerveSinglePodTest extends OpMode {
    ActuatorsSensorsDiffySwerve motors;
    double scale = 0.05;
    @Override
    public void init() {
        motors = new ActuatorsSensorsDiffySwerve();
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {
        motors.leftTopMotor.setPower(-gamepad1.left_stick_y * scale);
        motors.leftBottomMotor.setPower(-gamepad1.right_stick_y * scale);
        if (gamepad1.dpadUpWasPressed()) {
            scale += 0.05;
        }
        if (gamepad1.dpadDownWasPressed()) {
            scale -= 0.05;
        }
        telemetry.addData("Scale is:", scale);
    }
}
