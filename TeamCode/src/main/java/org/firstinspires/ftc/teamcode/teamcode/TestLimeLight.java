package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestLimeLight extends OpMode {
    ActuatorsSensorsDiffySwerve motors;

    @Override
    public void init() {
        motors = new ActuatorsSensorsDiffySwerve();
        motors.init(hardwareMap);
        motors.limeLight.start();
        motors.limeLight.pipelineSwitch(1);
    }

    @Override
    public void loop() {
        motors.LimeLightResult();
        telemetry.addData("LimeLight Target X is:", motors.getTarget(0));
        telemetry.addData("LimeLight Target Y is:", motors.getTarget(1));
        telemetry.addData("LimeLight Target Area is:", motors.getTarget(2));

        telemetry.addData("Ball Count is:", motors.getBallCount());
    }
}
