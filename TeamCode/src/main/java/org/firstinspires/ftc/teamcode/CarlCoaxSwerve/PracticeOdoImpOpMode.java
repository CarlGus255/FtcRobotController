package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice.CarlCoaxSwerveWheelHeading;
@TeleOp
public class PracticeOdoImpOpMode extends OpMode {

        CarlOdometryExampleImplementation odo;
        WebCamCarlCoaxSwerve cam;


        //Velocity values for teleop implementation
        //last2y for gamepad 2 y. This is to test reseting the field variables with the camera
        boolean last2y = false;

        public void init() {
            cam = new WebCamCarlCoaxSwerve();
            cam.init(hardwareMap);

            odo = new CarlOdometryExampleImplementation(cam);
            odo.init(hardwareMap);

        /*
        //Set field centric using camera to read the april tag on the red bin
        odo.resetFieldWebcamRedBin();

         */
        }

        public void loop () {

            //test wheel heading
            //telemetry.addData("Wheel Heading is:", wheelHeading.getOverallWheelHeading());
            //telemetry.addData("Left Wheel Heading is:", wheelHeading.getLeftWheelHeading());
            //telemetry.addData("Right Wheel Heading is:", wheelHeading.getRightWheelHeading());


            //Prints Field variables for testing (Cm)
            telemetry.addData("Field X is:", odo.getFieldX());
            telemetry.addData("Field Y is:", odo.getFieldY());
            telemetry.addData("Field Yaw is:", odo.getFieldYaw());

            //Test camera reset
            if (gamepad2.y && !last2y) {
                odo.resetFieldWebcamRedBin();
                telemetry.addData("Field is:", "Reset!");
            }
            last2y = gamepad2.y;

            telemetry.addData("Raw Odo X", odo.updateRobotX());
            telemetry.addData("Raw Odo Y", odo.updateRobotY());
            telemetry.addData("Raw Odo Yaw", odo.updateRobotYaw());

            telemetry.addData("Bearing is:", cam.getAprilBearing(24));
            telemetry.addData("Distance is:", cam.getAprilDistance(24));
        }
    }
