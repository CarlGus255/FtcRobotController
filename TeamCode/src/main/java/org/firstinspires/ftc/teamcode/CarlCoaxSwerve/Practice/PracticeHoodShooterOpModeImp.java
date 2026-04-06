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
    boolean autoSeekHood = false;
    boolean isTheta1 = true;
    boolean isHoming = false;



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
        if (gamepad1.dpad_right) {
            autoSeekHood = true;
        }
        if (gamepad1.dpad_left) {
            autoSeekHood = false;
        }

        if (gamepad1.dpad_up) {
            seekHoodAngle = seekHoodAngle + 0.5;
        }
        if (gamepad1.dpad_down) {
            seekHoodAngle = seekHoodAngle - 0.5;
        }
        if (gamepad1.a) {
            seekHoodAngle = 20;
        }
        if (gamepad1.x) {
            seekHoodAngle = 0;
        }
        if (gamepad2.left_bumper) {
            isTheta1 = false;
        } else if (gamepad2.right_bumper) {
            isTheta1 = true;
        }
        double checkHoodAngle;
        if (autoSeekHood) {
            //double checkHoodAngle = hood.getLaunchAngle(1.5, 1.15, hood.getBallVelFromFly(motors.getFlyVel())/100, isTheta1);
            //hood.getBallVelFromFinalFly(motors.getFlyVel())
            //checkHoodAngle = hood.getLaunchAngle(1.5, 1.15, hood.getBallVelFromFly(hood.getFinalFlyVel(motors.getFlyVel()))/100, isTheta1);
            checkHoodAngle = 90 - hood.getLaunchAngle(1.5, 1.15, hood.getBallVelFromFly(hood.getFinalFlyVel(motors.getFlyVel()))/100, isTheta1);
            if (checkHoodAngle > 0 && checkHoodAngle < 90) {
                seekHoodAngle = checkHoodAngle;
            }
            telemetry.addData("We are looking at:", checkHoodAngle);
        }

        //Homing sequence
        if (gamepad1.left_bumper) {
            isHoming = true;
        }
        if (isHoming) {
            hood.hoodHome();
            isHoming = hood.hasHomed();
        }


        motors.setHoodServoPower(hood.getHoodServoPowerPID(seekHoodAngle, getRuntime()));

        motors.setIntakePower(-gamepad1.left_stick_y);

        if (gamepad2.y) {
            isShooting = true;
        }
        if (gamepad2.x) {
            isShooting = false;
        }
        if (isShooting) {
            motors.setGate(0.75);
            motors.setIntakePower(1);
        } else if (!isShooting) {
            motors.setGate(0.5);
        }



        if (gamepad2.left_stick_y != 0) {
            flyWheelPower = Math.abs(gamepad2.left_stick_y);
        } else if (gamepad2.dpad_up) {
            setVel = 3500;
        }  else if (gamepad2.dpad_down) {
            setVel = 0;
        }
        if (gamepad2.left_stick_y == 0) {
            motors.setFlyWheelVelocity(setVel);
        }

        motors.setFlyWheelPower(flyWheelPower);
        telemetry.addData("Seek Hood Angle Is:", seekHoodAngle);
        telemetry.addData("Hood Angle Is:", hood.getHoodAngle());
        telemetry.addData("Angle Error Is:", hood.getAngleError());

        telemetry.addData("Flywheel Velocity is:", motors.getFlyVel());
        telemetry.addData("Flywheel Linear Velocity (mm) is:", motors.getFlyLinVel());
        telemetry.addData("Touch sensor isn't pressed", motors.getTouchSensor());

        //telemetry.addData("Color sensor sees purple:", motors.isColorSensorPurple());
        //telemetry.addData("Color sensor sees green:", motors.isColorSensorGreen());

        telemetry.addData("Rev Encoder Position Is:", motors.getRevEncoderPos());
        telemetry.addData("Change time is:", hood.getChangeTime());

        telemetry.addData("Chosen hood angle is:", hood.getLaunchAngle(1.5, 1.15, motors.getFlyLinVel(), isTheta1)); //lin vel in M/s, x and y in M
        telemetry.addData("Chosen hood angle from ball speed is:", hood.getBallVelFromFly(motors.getFlyVel())/100);
        telemetry.addData("Chosen hood angle from ball speed 2 is:", hood.getBallVelFromFly(motors.getFlyVel()/100));
        telemetry.addData("Theta 1 is:", hood.getTheta1());
        telemetry.addData("Theta 2 is:", hood.getTheta2());

        telemetry.addData("Speed of ball is:", hood.getBallVelFromFly(motors.getFlyVel()));
        telemetry.addData("Final speed of ball is:", hood.getBallVelFromFinalFly(motors.getFlyVel()));
        telemetry.addData("Technically possibly more accurate speed ball is:", hood.getAccurateBallVelFromFinalFly(motors.getFlyVel()));
        telemetry.addData("Final flywheel Velocity is:", hood.getFinalFlyVel(motors.getFlyVel()));

        //Set hood power using PID. Input values are the distance from april tag in X and Y, as well as flywheel velocity
        //hood.setHoodAngle(hood.getLaunchAngle((odo.getShootingDistance()*10), (46*25.4), motors.getFlyLinVel()));
    }
}
