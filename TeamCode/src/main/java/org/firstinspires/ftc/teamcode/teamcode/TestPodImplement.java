package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Disabled
public class TestPodImplement extends OpMode {

    DiffySwervePodImplement leftPod;
    ActuatorsSensorsDiffySwerve motors;
    PIDFImplement leftRotationPIDF;

    @Override
    public void init() {
        leftRotationPIDF = new PIDFImplement();
        leftRotationPIDF.setConstants(0.005, 0, 0, 0, 0, 0.001);

        motors = new ActuatorsSensorsDiffySwerve();
        motors.init(hardwareMap);

        leftPod = new DiffySwervePodImplement(motors.leftTopMotor, motors.leftBottomMotor, leftRotationPIDF);
    }

    @Override
    public void loop() {
        leftPod.update(getRuntime());

        leftPod.manualChangeRotation(gamepad1.right_stick_x*20);
        leftPod.setTranslation(-gamepad1.left_stick_y);


        telemetry.addData("Left Pod Heading is:", leftPod.getHeading());
        telemetry.addData("Left Pod Seek Heading is:", leftPod.getSeekHeading());
    }
}
