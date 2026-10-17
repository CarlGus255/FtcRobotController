import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.teamcode.CSVLogger;

import java.io.IOException;

@TeleOp
public class TestDiffySwerve extends OpMode {
    CSVLogger logger;
    DiffySwerveDriver swerve;
    double currentTime;
    @Override
    public void init() {
        swerve = new DiffySwerveDriver();
        swerve.init(hardwareMap);

        try {
            logger = new CSVLogger(
                    "Homing Values",
                    "Values",
                    "Time"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    double lastTime;
    boolean homing;
    double deltaTime;
    @Override
    public void loop() {
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = getRuntime();

        if (gamepad1.leftBumperWasPressed()) {
            swerve.leftPod.homePod(4);
            homing = true;
        }
        if (homing) {
            telemetry.addData("We are homing", swerve.leftPod.homing);
            telemetry.addData("Detections", swerve.leftPod.detections);
            telemetry.addData("Rejections", swerve.leftPod.rejections);
            telemetry.addData("Sensor says", swerve.leftPod.currentState);
            telemetry.addData("Home mean value is", swerve.leftPod.homeDetMeanValue);
            telemetry.addData("Home Center is", swerve.leftPod.centerValue);
            telemetry.addData("Homing Error is", swerve.leftPod.homingError);
            if (swerve.leftPod.homingDetections != null) {
                for (int i = 0; i < swerve.leftPod.homingDetections.size(); i++) {
                    telemetry.addData("Detection number" + i + "is", swerve.leftPod.homingDetections.get(i));
                }
            }
            if (swerve.leftPod.detectedThreshold) {
                logger.log(swerve.leftPod.heading, currentTime);
            } else {
            }
            homing = swerve.leftPod.homing;
        } else {
            swerve.leftPod.vectorPod(Math.toDegrees(Math.atan2((-gamepad1.left_stick_y),gamepad1.left_stick_x)), Math.sqrt((gamepad1.left_stick_x*gamepad1.left_stick_x)+(gamepad1.left_stick_y*gamepad1.left_stick_y)));
        }

        /**
         * PID Tune Logic
         */
        swerve.leftPod.rotationPID.tuneConstants(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
        swerve.leftPod.rotationPID.changeMode(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
        if (swerve.leftPod.rotationPID.getMode() % 4 == 0) {
            telemetry.addData("KP is", swerve.leftPod.rotationPID.getkP());
        }
        if (swerve.leftPod.rotationPID.getMode() % 4 == 1) {
            telemetry.addData("KI is", swerve.leftPod.rotationPID.getkI());
        }
        if (swerve.leftPod.rotationPID.getMode() % 4 == 2) {
            telemetry.addData("KD is", swerve.leftPod.rotationPID.getkD());
        }
        if (swerve.leftPod.rotationPID.getMode() % 4 == 3) {
            telemetry.addData("KF is", swerve.leftPod.rotationPID.getkF());
        }

        swerve.leftPod.updatePod(deltaTime);

        telemetry.addData("Translation is", swerve.leftPod.translation);
        telemetry.addData("Rotaiton is", swerve.leftPod.rotation);

        telemetry.addData("Heading Degrees is", swerve.leftPod.heading);


        telemetry.update();
    }
}
