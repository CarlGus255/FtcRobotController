import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class TurretTest extends OpMode {
    MotorsBioBuzz motors;
    boolean pidMode;
    boolean changePID;
    boolean displayAprilTags;

    @Override
    public void init() {
        motors = new MotorsBioBuzz();
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {
        //Pre logic update
        motors.updateDeltaTime(getRuntime());
        telemetry.addData("Number of degrees is", motors.getTurretPositionDeg());
        telemetry.addData("Turret in bounds", motors.getTurretInBounds());

        //Toggle modes
        if (gamepad1.yWasPressed()) {
            pidMode = !pidMode;
        }

        //Manual mode
        if (!pidMode) {
            telemetry.addData("Manual mode", "");
            motors.debugSetTurretError(gamepad1.left_stick_x * 10);
        }

        //"Automatic" ish mode - usingpid
        if (pidMode) {
            telemetry.addData("Position mode", "");
            if (gamepad1.leftBumperWasPressed()) {
                motors.changeTurretPos(10);
            }
            if (gamepad1.rightBumperWasPressed()) {
                motors.changeTurretPos(-10);
            }

        }

        //Toggle changing pid
        if (gamepad1.xWasPressed()) {
            changePID = !changePID;
        }
        if (changePID) {
            telemetry.addData("PID tuning!", "");
            motors.turretPID.changeMode(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
            motors.turretPID.tuneConstants(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
            telemetry.addData("Pan PID mode is:", motors.turretPID.getMode());
            if (motors.turretPID.getMode() % 4 == 0) {
                telemetry.addData("KP is:", "kP=%.6f",
                        motors.turretPID.getkP());
            }
            if (motors.turretPID.getMode() % 4 == 1) {
                telemetry.addData("KI is:", "kI=%.6f", motors.turretPID.getkI());
            }
            if (motors.turretPID.getMode() % 4 == 2) {
                telemetry.addData("KD is:", "kD=%.6f", motors.turretPID.getkD());
            }
            if (motors.turretPID.getMode() % 4 == 3) {
                telemetry.addData("KF is:", "%.6f", motors.turretPID.getkF());
            }
        }

        if (gamepad2.xWasPressed()) {
            displayAprilTags = !displayAprilTags;
        }
        /*
        if (displayAprilTags){

            motors.readTags();
            /*
            AprilTagReading[] red = motors.getTags(motors.redTags1);
            for (int i = 0; i < red.length; i++) {

                if (red[i] != null) {
                    telemetry.addData(
                            "Red Tag " + i,
                            "ID: %d | Distance: %.1f | Bearing: %.1f | Pitch: %.1f | Yaw: %.1f",
                            red[i].id,
                            red[i].distance,
                            red[i].bearing,
                            red[i].pitch,
                            red[i].yaw
                    );
                } else {
                    telemetry.addData(
                            "Red Tag " + i,
                            "Not detected"
                    );
                }
            }

             */
        /*
            for (AprilTagDetection detection :
                    motors.getAprilTagProcessor().getDetections()) {

                telemetry.addData(
                        "RAW TAG",
                        "ID: %d | Pose: %s",
                        detection.id,
                        detection.ftcPose != null
                );
                telemetry.addData("Metadata exists", detection.metadata != null);

                if (detection.metadata != null) {
                    telemetry.addData("Tag name", detection.metadata.name);
                    telemetry.addData("Tag size", detection.metadata.tagsize);
                }
            }

            telemetry.addData(
                    "RAW DETECTIONS",
                    motors.getAprilTagProcessor().getDetections().size()
            );
        }

         */


        //Post logic update
        motors.updateTurretPower();

        telemetry.addData("Set error is", motors.turretError);
        telemetry.addData("Turret Power is", motors.turretPower);

        telemetry.update();
    }

}
