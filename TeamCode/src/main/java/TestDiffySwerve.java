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
    MotorsBioBuzz motors;
    double currentTime;
    @Override
    public void init() {
        motors = new MotorsBioBuzz();
        motors.init(hardwareMap);
        swerve = new DiffySwerveDriver();
        swerve.init(hardwareMap);

        try {
            logger = new CSVLogger(
                    "Homing Values",
                    "LeftValues",
                    "RightValues"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        swerve.leftPod.homePod(5);
        swerve.rightPod.homePod(5);
    }

    @Override
    public void init_loop() {
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        super.init_loop();

        swerve.updateSwerve(deltaTime);

        motors.setHoodAngle(0);
    }

    double lastTime;
    boolean homing;
    double deltaTime;
    double offset = 5;
    boolean fieldCentric;
    double spindexDirection = 0;
    double hoodPos;

    @Override
    public void loop() {
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;


        motors.intake.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
        if (gamepad1.xWasPressed()) {
            fieldCentric = !fieldCentric;
        }
        motors.manualSpindex(spindexDirection);
        if (gamepad1.aWasPressed()) {
            spindexDirection *= -1;
        }
        if (gamepad2.dpadUpWasPressed()) {
            swerve.leftPod.scale += 0.1;
            swerve.rightPod.scale += 0.1;
        }
        if (gamepad2.dpadDownWasPressed()) {
            swerve.leftPod.scale -= 0.1;
            swerve.rightPod.scale -= 0.1;
        }
        telemetry.addData("Driving speed is", swerve.leftPod.scale);
        if (gamepad1.yWasPressed()) {
            swerve.leftPod.homePod(5);
            swerve.rightPod.homePod(5);
            homing = true;
        }
        if (homing) {
            /*
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
             */
            if (swerve.leftPod.detectedThreshold) {
                logger.log(swerve.leftPod.heading, swerve.rightPod.heading);
            } else {
            }
            homing = swerve.leftPod.homing;
        } else {
            //swerve.leftPod.vectorPod(Math.toDegrees(Math.atan2((-gamepad1.left_stick_y),gamepad1.left_stick_x)) - offset, Math.sqrt((gamepad1.left_stick_x*gamepad1.left_stick_x)+(gamepad1.left_stick_y*gamepad1.left_stick_y)));
            //swerve.rightPod.vectorPod(-(Math.toDegrees(Math.atan2((-gamepad1.right_stick_y),gamepad1.right_stick_x))) + offset, Math.sqrt((gamepad1.right_stick_x*gamepad1.right_stick_x)+(gamepad1.right_stick_y*gamepad1.right_stick_y)));

            //swerve.vectorBotRough(Math.toDegrees(Math.atan2((-gamepad1.left_stick_y),gamepad1.left_stick_x)), Math.sqrt((gamepad1.left_stick_x*gamepad1.left_stick_x)+(gamepad1.left_stick_y*gamepad1.left_stick_y)), gamepad1.right_stick_x, offset);

            if (!fieldCentric) {
                swerve.driveBot(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x * offset);
                telemetry.addData("Robot Centric","");
            } else if (fieldCentric) {
                swerve.driveFieldCentric(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x * offset);
                telemetry.addData("Field Centric","");
            }
            if (gamepad1.leftBumperWasPressed()) {
                offset -= 1;
            }
            if (gamepad1.rightBumperWasPressed()) {
                offset += 1;
            }
            telemetry.addData("Offset is", offset);
            telemetry.addData("Left set heading is", swerve.leftPod.setHeading);

            telemetry.addData("lx", swerve.lX);
            telemetry.addData("ly", swerve.lY);
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

        //Hood and flywheel logic
        //motors.setHoodAngle(-gamepad2.left_stick_y * 30);
        if (gamepad2.leftBumperWasPressed()) {
            hoodPos -= 0.05;
        }
        if (gamepad2.rightBumperWasPressed()) {
            hoodPos += 0.05;
        }
        motors.hood.setPosition(hoodPos);
        telemetry.addData("Hood Position is", motors.hood.getPosition());
        swerve.updateSwerve(deltaTime);

        telemetry.addData("Translation is", swerve.leftPod.translation);
        telemetry.addData("Rotaiton is", swerve.leftPod.rotation);

        telemetry.addData("Left Pod Heading Degrees is", swerve.leftPod.heading);

        telemetry.addData("Robot X is", swerve.robotPos[0]);
        telemetry.addData("Robot Y is", swerve.robotPos[1]);
        telemetry.addData("Robot Heading is", swerve.robotPos[2]);

        telemetry.update();
    }
}
