package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;


import java.io.IOException;

@TeleOp
public class ShooterTest extends OpMode {
    DcMotorEx motor1;
    //DcMotorEx motor2;
    CSVLogger logger;
    TelemetryManager telemetryM;
    TimerImplement timer;
    double setVelRPM = 600;
    double setVelRPS;
    double setVelTPS; // Ticks per second
    double errorRPM;
    boolean run;
    final double ticksPerRev = 28;
    double currentVelRPM;
    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        try {
            logger = new CSVLogger(
                    "Shooter Test Motors",
                    "Raw Error RPM"
            );
        } catch (IOException e) {
            telemetry.addData("Logger Error", e.getMessage());
        }
        timer = new TimerImplement();
        motor1 = hardwareMap.get(DcMotorEx.class, "shooter");
        //motor2 = hardwareMap.get(DcMotorEx.class, "motor1");

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        setVelTPS = setVelRPM/60 * ticksPerRev;
        if (gamepad1.xWasPressed()) {
            run = !run;
        }
        if (gamepad1.dpadUpWasPressed()) {
            setVelRPM += 50;
        }
        if (gamepad1.dpadDownWasPressed()) {
            setVelRPM -= 50;
        }
        if (run) {
            //Ticks per second
            motor1.setVelocity(setVelTPS);
            //motor2.setVelocity(-setVelTPS);
            currentVelRPM = motor1.getVelocity()/ticksPerRev;
            errorRPM = (setVelTPS-currentVelRPM);
            telemetryM.addData("RPM is", currentVelRPM);
        }
        if (timer.setTimer(getRuntime(), 0.1)) {
            logger.log(errorRPM);
        }
        logger.log(errorRPM);
        telemetryM.addData("Error is", errorRPM);
        telemetry.addData("SetVelRPM is", setVelRPM);
        telemetry.addData("SetVeloTPS is", setVelTPS);
        telemetry.addData("CurrentVel Rpm is", currentVelRPM);
    }

    @Override
    public void stop() {
        logger.close();
        super.stop();
    }
}