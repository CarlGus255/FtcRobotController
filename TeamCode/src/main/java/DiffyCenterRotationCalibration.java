import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamcode.CSVLogger;
import org.firstinspires.ftc.teamcode.teamcode.TimerImplement;

import java.io.IOException;

@TeleOp
public class DiffyCenterRotationCalibration extends OpMode {
    DiffySwerveDriver swerve;
    TimerImplement rotationTimer;
    CSVLogger logger;
    TelemetryManager telemetryM;
    double rotationTime = 10;
    double lastTime;
    double currentTime;
    double deltaTime;
    boolean rotate;
    double rotateVel = 6;
    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        swerve = new DiffySwerveDriver();
        swerve.init(hardwareMap);
        lastTime = getRuntime();
        swerve.leftPod.homePod(5);
        swerve.rightPod.homePod(5);

        try {
            logger = new CSVLogger(
                    "RotationTest1",
                     "Time",
                    "x",
                    "y",
                    "Heading (deg)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rotationTimer = new TimerImplement();
    }

    @Override
    public void init_loop() {
        super.init_loop();
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = getRuntime();
        swerve.updateSwerve(deltaTime);
    }

    @Override
    public void loop() {
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = getRuntime();
        rotationTimer.setTimer(currentTime, rotationTime);

        if (gamepad1.xWasPressed()) {
            rotate = true;
            rotationTimer.resetTimer();
        }

        if (rotate) {
            if (!rotationTimer.timeMet) {
                swerve.driveBot(0, 0, rotateVel);
                logger.log(
                        swerve.robotPos[0],
                        swerve.robotPos[1],
                        swerve.robotPos[2]
                );
            } else {
                rotate = false;
            }
        } else {
            swerve.driveBot(0, 0, 0);
        }

        if (gamepad1.leftTriggerWasPressed()) {
            rotateVel -= 0.5;
        }
        if (gamepad1.rightTriggerWasPressed()) {
            rotateVel += 0.5;
        }
        if (gamepad1.dpadUpWasPressed()) {
            rotationTime += 1;
        }
        if (gamepad1.dpadDownWasPressed()) {
            rotationTime -= 1;
        }

        if (gamepad2.dpadUpWasPressed()) {
            swerve.leftPodOffsets[0] += 0.01;
            swerve.rightPodOffsets[0] += 0.01;
            swerve.changePPOOffsets(0, 10);
        }
        if (gamepad2.dpadDownWasPressed()) {
            swerve.leftPodOffsets[0] -= 0.01;
            swerve.rightPodOffsets[0] -= 0.01;
            swerve.changePPOOffsets(0, -10);
        }
        if (gamepad2.dpadLeftWasPressed()) {
            swerve.leftPodOffsets[1] += 0.01;
            swerve.rightPodOffsets[1] -= 0.01;
            swerve.changePPOOffsets(-10, 0);
        }
        if (gamepad2.dpadRightWasPressed()) {
            swerve.leftPodOffsets[1] -= 0.01;
            swerve.rightPodOffsets[1] += 0.01;
            swerve.changePPOOffsets(10, 0);
        }



        telemetry.addData("We are rotating", rotate);
        telemetry.addData("timer is", rotationTimer.timeMet);
        telemetryM.addData("X is", swerve.robotPos[0]);
        telemetryM.addData("Y is", swerve.robotPos[1]);
        telemetryM.addData("Heading is", swerve.robotPos[2]);
        telemetryM.addData("Y Offset is", swerve.leftPodOffsets[0]);
        telemetryM.addData("X Offset is", swerve.leftPodOffsets[1]);
        telemetryM.addData("PPOX is", swerve.ppoX);
        telemetryM.addData("PPOY is", swerve.ppoY);
        telemetry.addData("Rotate Vel is", rotateVel);
        telemetry.addData("Rotate time is", rotationTime);



        telemetryM.update();

        swerve.updateSwerve(deltaTime);
    }
}
