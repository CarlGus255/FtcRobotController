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
    double flyWheelPower;
    double setVel;
    boolean isShooting = false;

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
        /*
        seekHoodAngle = gamepad1.left_stick_x * 45;
        hood.setHoodAngle(seekHoodAngle);
         */

        if (gamepad2.y) {
            isShooting = true;
        }
        if (gamepad2.x) {
            isShooting = false;
        }
        if (isShooting) {
            motors.setGate(1);
            motors.setIntakePower(1);
        } else if (!isShooting) {
            motors.setGate(0.5);
        }
        motors.setIntakePower(-gamepad1.left_stick_y);


        if (gamepad2.left_stick_y != 0) {
            flyWheelPower = Math.abs(gamepad2.left_stick_y);
        } else if (gamepad2.dpad_up) {
            setVel = 1500;
        }  else if (gamepad2.dpad_down) {
            setVel = 0;
        }
        if (gamepad2.left_stick_y == 0) {
            motors.setFlyWheelVelocity(setVel);
        }

        if (gamepad2.dpad_right) {
            hood.varyFlywheel();
        }

        motors.setFlyWheelPower(flyWheelPower);
        telemetry.addData("Seek Hood Angle Is:", seekHoodAngle);
        telemetry.addData("Hood Angle Is:", hood.getHoodAngle());
        telemetry.addData("Angle Error Is:", hood.getAngleError());

        telemetry.addData("Flywheel Velocity is:", motors.getFlyVel());
        telemetry.addData("Flywheel Linear Velocity (mm) is:", motors.getFlyLinVel());

        //Set hood power using PID. Input values are the distance from april tag in X and Y, as well as flywheel velocity
        //hood.setHoodAngle(hood.getLaunchAngle((odo.getShootingDistance()*10), (46*25.4), motors.getFlyLinVel()));
    }
}
