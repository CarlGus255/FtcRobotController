package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CarlTeleOpClass extends OpMode {

    CarlCoaxSwerveDecodeImplementation swerveImp = new CarlCoaxSwerveDecodeImplementation();
    CarlOdometryExampleImplementation odo = new CarlOdometryExampleImplementation();
    WebCamCarlCoaxSwerve cam = new WebCamCarlCoaxSwerve();
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();

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
    }
}
