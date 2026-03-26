package org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.CarlHoodShoot;
import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.CarlOdometryExampleImplementation;
import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.MotorsCarlCoaxSwervePractice;
import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.WebCamCarlCoaxSwerve;
@TeleOp

public class PracticeHoodShooterOpModeImp extends OpMode {

    CarlHoodShoot hood;
    MotorsCarlCoaxSwervePractice motors;
    CarlOdometryExampleImplementation odo;
    WebCamCarlCoaxSwerve cam;

    double seekHoodAngle;

    @Override
    public void init() {
        motors = new MotorsCarlCoaxSwervePractice();
        motors.init(hardwareMap);

        cam = new WebCamCarlCoaxSwerve();
        cam.init(hardwareMap);

        odo = new CarlOdometryExampleImplementation(cam);
        odo.init(hardwareMap);

        hood = new CarlHoodShoot(motors, odo);
    }

    @Override
    public void loop() {
        seekHoodAngle = gamepad1.left_stick_x * 45;
        hood.setHoodAngle(seekHoodAngle);


        telemetry.addData("Seek Hood Angle Is:", seekHoodAngle);
        telemetry.addData("Hood Angle Is:", hood.getHoodAngle());
        telemetry.addData("Angle Error Is:", hood.getAngleError());

        //Set hood power using PID. Input values are the distance from april tag in X and Y, as well as flywheel velocity
        hood.setHoodAngle(hood.getLaunchAngle((odo.getShootingDistance()*10), (46*25.4), motors.getFlyLinVel()));
    }
}
