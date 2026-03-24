package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CarlTeleOpClass extends OpMode {

    CarlCoaxSwerveDecodeImplementation swerveImp = new CarlCoaxSwerveDecodeImplementation();
    CarlOdometryExampleImplementation odo = new CarlOdometryExampleImplementation();
    WebCamCarlCoaxSwerve cam = new WebCamCarlCoaxSwerve();
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();

    double xVelocity;
    double yVelocity;
    double yawVelocity;
    boolean lastY = false;
    boolean runPos1 = false;

    public void init() {
        //Initialize the camera, odometry, and mechanisms
        cam.init(hardwareMap);
        motors.init(hardwareMap);
        odo.init(hardwareMap);

        //Set field centric using camera to read the april tag on the red bin
        odo.resetFieldWebcamRedBin();
    }

    public void loop () {
        //Sets the gamepad 1 left stick to XY control and right stick to Yaw control using acceleration values
        xVelocity = -gamepad1.left_stick_y;
        yVelocity = gamepad1.left_stick_x;
        yawVelocity = -gamepad1.right_stick_y;

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

    }
}
