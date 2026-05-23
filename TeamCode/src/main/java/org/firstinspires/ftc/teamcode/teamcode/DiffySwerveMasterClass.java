package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DiffySwervePodDriver;
import org.firstinspires.ftc.teamcode.PIDFImplement;

@TeleOp

public class DiffySwerveMasterClass extends OpMode {
    PIDFImplement leftRotationPID;
    PIDFImplement rightRotationPID;
    ActuatorsSensorsDiffySwerve motors;
    DiffySwervePodDriver drive;
    @Override
    public void init() {
        leftRotationPID = new PIDFImplement();
        rightRotationPID = new PIDFImplement();
        motors = new ActuatorsSensorsDiffySwerve();
        drive = new DiffySwervePodDriver();

        motors.init(hardwareMap);
        drive.init(motors, leftRotationPID);
    }
    boolean pidChange;
    boolean isleftRotationPID;
    double lastTime;
    boolean isLeftHoming;

    @Override
    public void loop() {
        //Want to do once per loop
        drive.readHeading();

        //Experimental Two Step Homing
        if (gamepad1.yWasPressed()) {
            isLeftHoming = true;
        }
        if (isLeftHoming) {
            drive.homeLeftPodTwoStep(gamepad1.xWasPressed());
        }
        telemetry.addData("Speculative Turning Ratio Is:", drive.getSpecRatio());
        //Experimental driving the motors
        drive.setTranslation(gamepad1.left_stick_y);
        drive.manualChangeLeftRotation(gamepad1.right_stick_x * 20);
        if (gamepad1.aWasPressed()) {
            drive.manualChangeLeftRotation(90);
        }
        drive.setHeading(getRuntime() - lastTime);
        lastTime = getRuntime();
        drive.driveMotors();

        telemetry.addData("Left Translation is:", drive.getLeftTranslation());
        telemetry.addData("Left Rotation is:", drive.getLeftRotation());
        telemetry.addData("Left Heading is:", drive.getLeftHeading());
        telemetry.addData("Left Seek Heading is:", drive.getSeekLeftHeading());
    }
}
