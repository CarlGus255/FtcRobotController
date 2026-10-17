import android.sax.StartElementListener;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class TestSwerveOpMode extends OpMode {
    BioBuzzDiffySwerve swerve;
    boolean debug;
    double lastTime;
    double leftHeading;
    double deltaTime;

    @Override
    public void init() {
        swerve = new BioBuzzDiffySwerve(hardwareMap);
    }
    boolean simpleHomingEdge = true;
    @Override
    public void loop() {
        deltaTime = getRuntime() - lastTime;
        /*
        if (gamepad1.bWasPressed()) {
            swerve.accurateMode = !swerve.accurateMode;
        }

         */

        if (gamepad2.leftBumperWasPressed()) {
            simpleHomingEdge = !simpleHomingEdge;
        }
        if (simpleHomingEdge) {
            telemetry.addData("Simple point crossed", swerve.leftEdgeDetected());
        } else {
            swerve.computeLeftHomingEdgeDetection();
            telemetry.addData("Bound point crossed", swerve.hitEdgeLeftHome);
            telemetry.addData("Simple point crossed", swerve.leftHomingDectedEdge);
            telemetry.addData("diff is", swerve.diffEdgeHeading);
            telemetry.addData("Last heading is", swerve.lastEdgePosLeftHoming);
            telemetry.addData("Rejected edges", swerve.edgeDetectionRejected);
            telemetry.addData("Accepted edges", swerve.acceptedEdges);
        }
        if (gamepad1.yWasPressed()) {
            swerve.startLeftHomePoints(4);
        }
        if (gamepad2.xWasPressed()) {
            swerve.resetLeftEncoders();
        }
        if (gamepad1.xWasPressed()) {
            debug = !debug;
        }
        if (debug) {
            swerve.debugLeftWheel(gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
        else {
            swerve.vectorLeftPod(gamepad1.left_stick_y, leftHeading);
            swerve.updateSwerveMotors(deltaTime);
            telemetry.addData("Heading is", leftHeading);
            if (gamepad1.leftBumperWasPressed()) {
                leftHeading -= 1;
            }
            if (gamepad1.rightBumperWasPressed()) {
                leftHeading += 2;
            }
        }

        swerve.leftPodRotatePID.tuneConstants(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
        swerve.leftPodRotatePID.changeMode(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
        if (swerve.leftPodRotatePID.getMode() % 4 == 0) {
            telemetry.addData("KP is", swerve.leftPodRotatePID.getkP());
        }
        if (swerve.leftPodRotatePID.getMode() % 4 == 1) {
            telemetry.addData("KI is", swerve.leftPodRotatePID.getkI());
        }
        if (swerve.leftPodRotatePID.getMode() % 4 == 2) {
            telemetry.addData("KD is", swerve.leftPodRotatePID.getkD());
        }
        if (swerve.leftPodRotatePID.getMode() % 4 == 3) {
            telemetry.addData("KF is", swerve.leftPodRotatePID.getkF());
        }

        telemetry.addData("Translation is", swerve.leftTranslation);
        telemetry.addData("Rotaiton is", swerve.leftRotation);
        telemetry.addData("Switch is activated", swerve.leftMagnetHome.getState());
        telemetry.addData("Right magnet is", swerve.rightMagnetHome.getState());
        telemetry.addData("Left position", swerve.leftPodHeading);
        telemetry.addData("rotation error left pod", swerve.leftPodHeadingError);

        telemetry.addData("Left Top Motor Is", swerve.ltm.getCurrentPosition());
        telemetry.addData("Left Bottom Motor is", swerve.lbm.getCurrentPosition());

        /*
        telemetry.addData("Left First Home is", swerve.leftFirstHome);
        telemetry.addData("Left Last Home is", swerve.leftLastHome);

         */
        telemetry.addData("Left First Home Value is", swerve.leftFirstHomeValue);
        telemetry.addData("Left Last Home Value is", swerve.leftLastHomeValue);


        telemetry.addData("Left Home Error is", swerve.leftHomingError);
        telemetry.addData("Left Home Position is", swerve.leftHomePosition);

        telemetry.addData("Left Homing Points Reached", swerve.leftHomingPointsReached);
        telemetry.addData("Left Homing Points", swerve.leftHomingPoints);
        telemetry.addData("Edge Detected", swerve.leftHomingDectedEdge);
        if (swerve.leftHomingValues != null && swerve.leftHomingPointsReached > 0) {
            telemetry.addData("Left Home Value 0", swerve.leftHomingValues[0]);
        } else {
            telemetry.addData("Left Home Value 0", "Not recorded yet");
        }
        lastTime = getRuntime();

    }
}
