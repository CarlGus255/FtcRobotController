package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class DiffySwervePodPIDTune extends OpMode {
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

    @Override
    public void loop() {
        //Want to do once per loop
        drive.readHeading();


        //Toggles whether driver can change PID with Y
        if (gamepad1.yWasPressed()) {
            pidChange = !pidChange;
        }
        //Toggles which PID controller is being affected
        if (gamepad1.xWasPressed()) {
            isleftRotationPID = !isleftRotationPID;
        }
        //Uses dpad up and down to cycle through modes and left and right to change the PID values
        if (pidChange && isleftRotationPID) {
            leftRotationPID.changeMode(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
            leftRotationPID.tuneConstants(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
        } else if (pidChange && !isleftRotationPID) {
            rightRotationPID.changeMode(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
            rightRotationPID.tuneConstants(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
        }


        //Display the Mode and the value
        leftRotationPID.readMode();
        rightRotationPID.readMode();
        telemetry.addData("Can Change pid Values:", pidChange);
        telemetry.addData("leftRotationPID:", isleftRotationPID);
        if (isleftRotationPID) {
            if (leftRotationPID.getMode() % 4 == 0) {
                telemetry.addData("KP is:", leftRotationPID.getkP());
            }
            if (leftRotationPID.getMode() % 4 == 1) {
                telemetry.addData("KI is:", leftRotationPID.getkI());
            }
            if (leftRotationPID.getMode() % 4 == 2) {
                telemetry.addData("KD is:", leftRotationPID.getkD());
            }
            if (leftRotationPID.getMode() % 4 == 3) {
                telemetry.addData("KF is:", leftRotationPID.getkF());
            }
        } else if (!isleftRotationPID) {
            if (rightRotationPID.getMode() % 4 == 0) {
                telemetry.addData("KP is:", rightRotationPID.getkP());
            }
            if (rightRotationPID.getMode() % 4 == 1) {
                telemetry.addData("KI is:", rightRotationPID.getkI());
            }
            if (rightRotationPID.getMode() % 4 == 2) {
                telemetry.addData("KD is:", rightRotationPID.getkD());
            }
            if (rightRotationPID.getMode() % 4 == 3) {
                telemetry.addData("KF is:", rightRotationPID.getkF());
            }
        }


        //Experimental driving the motors
        drive.setTranslation(gamepad1.left_stick_y);
        drive.manualChangeLeftRotation(gamepad1.right_stick_x * 40);
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
        telemetry.addData("Ltm Encoder is:", motors.readLeftTopMotorEncoder());
        telemetry.addData("Lbm Encoder is:", motors.readLeftBottomMotorEncoder());
        //telemetry.addData("PID is:", leftRotationPID.pidOutPut(drive.getSeekLeftHeading()-drive.getLeftHeading(), getRuntime()-lastTime));
    }
}

