package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice.CarlCoaxSwerveWheelHeading;

@TeleOp
public class CarlTeleOpClass extends OpMode {
    CarlCoaxSwerveDecodeImplementation swerveImp;
    CarlOdometryExampleImplementation odo;
    WebCamCarlCoaxSwerve cam;
    MotorsCarlCoaxSwervePractice motors;
    CarlCoaxSwerveWheelHeading wheelHeading;
    PIDTuning PID;


    //Velocity values for teleop implementation
    double xVelocity;
    double yVelocity;
    double yawVelocity;
    //lastY for state detection for gamepad 1, so driver a can just press Y to activtae run to position 1, which is defined in swerve implement.
    boolean lastY = false;
    boolean runPos1 = false;
    //last2y for gamepad 2 y. This is to test reseting the field variables with the camera
    boolean last2y = false;

    public void init() {

        cam = new WebCamCarlCoaxSwerve();
        cam.init(hardwareMap);

        odo = new CarlOdometryExampleImplementation(cam);
        odo.init(hardwareMap);

        motors = new MotorsCarlCoaxSwervePractice();
        motors.init(hardwareMap);

        wheelHeading = new CarlCoaxSwerveWheelHeading(motors);
        PID = new PIDTuning(wheelHeading);

        swerveImp = new CarlCoaxSwerveDecodeImplementation(odo, motors, wheelHeading, PID);
        //Set field centric using camera to read the april tag on the red bin
        odo.resetFieldWebcamRedBin();


    }

    public void loop () {
        //Sets the gamepad 1 left stick to XY control and right stick to Yaw control using acceleration values
        xVelocity = -gamepad1.left_stick_y;
        yVelocity = gamepad1.left_stick_x;
        yawVelocity = -gamepad1.right_stick_y;

        //test wheel heading
        telemetry.addData("Wheel Heading is:", wheelHeading.getOverallWheelHeading());
        telemetry.addData("Left Wheel Heading is:", wheelHeading.getLeftWheelHeading());
        telemetry.addData("Right Wheel Heading is:", wheelHeading.getRightWheelHeading());

        //If we want to seek to a field position, this will override driver control until finished looping when driver presses y
        //Begins seek sequence
        if (gamepad1.y && !lastY) {
            runPos1 = true;
        }
        lastY = gamepad1.y;
        //Sets seekField variables to the set position in swerveImp
        if (runPos1) {
            swerveImp.swerveImplement(swerveImp.xPos1, swerveImp.yPos1, swerveImp.yawPos1);
        } else {
            //Driver control implementation for when we aren't seeking a position
            swerveImp.swerveImplement(swerveImp.swerveImplementaitonXVelocityToXPos(xVelocity), swerveImp.swerveImplementaitonYVelocityToYPos(yVelocity), swerveImp.swerveImplementaitonYawVelocityToYawPos(yawVelocity));
        }

        //End seek sequence
        if (-2 < (swerveImp.fieldX - swerveImp.xPos1) || (swerveImp.fieldX - swerveImp.xPos1) < 2 && -2 < (swerveImp.fieldY - swerveImp.yPos1) || (swerveImp.fieldY - swerveImp.yPos1) < 2 && -2 < (swerveImp.fieldYaw - swerveImp.yawPos1) || (swerveImp.fieldYaw - swerveImp.yawPos1) < 2) {
            //Camera update?
            runPos1 = false;
        }
        //Prints Field variables for testing (Cm)
        telemetry.addData("Field X is:", swerveImp.fieldX);
        telemetry.addData("Field Y is:", swerveImp.fieldY);
        telemetry.addData("Field Yaw is:", swerveImp.fieldYaw);

        //Test camera reset
        if (gamepad2.y && !last2y) {
            odo.resetFieldWebcamRedBin();
            telemetry.addData("Field is:", "Reset!");
        }
        last2y = gamepad2.y;

        telemetry.addData("X Velocity is:", xVelocity);
        telemetry.addData("Y Velocity is:", yVelocity);
        telemetry.addData("Yaw Velocity is:", yawVelocity);

        telemetry.addData("Left Wheel Translation is:", swerveImp.translationLeft);
        telemetry.addData("Right Wheel Translation is:", swerveImp.translationRight);

        telemetry.addData("Change Direction is:", swerveImp.changeDirection);

    }
}
