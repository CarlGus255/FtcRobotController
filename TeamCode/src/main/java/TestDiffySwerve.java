import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
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
    double intakePower;
    double indexPower;

    @Override
    public void loop() {
        currentTime = getRuntime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;


        motors.intake.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
        if (gamepad1.xWasPressed()) {
            fieldCentric = !fieldCentric;
        }
        motors.runSpindex(spindexDirection);
        if (gamepad1.aWasPressed()) {
            spindexDirection *= -1;
        }

        if (gamepad2.xWasPressed()) {
            motors.runSpindexDeg(90);
        }
        if (gamepad2.yWasPressed()) {
            motors.runSpindexDeg(-90);
        }
        if (gamepad2.bWasPressed()) {
            motors.homeSpindex();
        }
        telemetry.addData("Driving speed is", swerve.leftPod.scale);
        if (gamepad1.yWasPressed()) {
            swerve.leftPod.homePod(3);
            swerve.rightPod.homePod(3);
            homing = true;
        }
        if (homing) {

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
                //swerve.driveFieldCentric(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x * offset);
                //swerve.driveBotFieldCentricHeadingLock(-gamepad1.left_stick_y, -gamepad1.left_stick_x, deltaTime);
                //swerve.changeHeading(-gamepad1.right_stick_x * 12);
                swerve.driveBotFieldCentricDriveChangeLock(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x * offset, deltaTime);
                telemetry.addData("Field Centric","");
                telemetry.addData("angRateThresh is", swerve.angRateThresh);
                if (gamepad2.leftTriggerWasPressed()) {
                    swerve.angRateThresh -= 0.5;
                }
                if (gamepad2.rightTriggerWasPressed()) {
                    swerve.angRateThresh += 0.5;
                }

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

        //Debug
        indexPower = gamepad2.right_stick_y;
        motors.runSpindex(indexPower);
        

        motors.hood.setPosition(hoodPos);

        //PID Tuning
        swerve.headingPID.tuneConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
        swerve.headingPID.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
        if (swerve.headingPID.getMode() % 4 == 0) {
            telemetry.addData("KP is", swerve.headingPID.getkP());
        }
        if (swerve.headingPID.getMode() % 4 == 1) {
            telemetry.addData("KI is", swerve.headingPID.getkI());
        }
        if (swerve.headingPID.getMode() % 4 == 2) {
            telemetry.addData("KD is", swerve.headingPID.getkD());
        }
        if (swerve.headingPID.getMode() % 4 == 3) {
            telemetry.addData("KF is", swerve.headingPID.getkF());
        }
        
        
        telemetry.addData("Translation is", swerve.leftPod.translation);
        telemetry.addData("Rotaiton is", swerve.leftPod.rotation);

        telemetry.addData("Left Pod Heading Degrees is", swerve.leftPod.heading);


        telemetry.addData("Robot X is", swerve.robotPos[0]);
        telemetry.addData("Robot Y is", swerve.robotPos[1]);
        telemetry.addData("Robot Heading is", swerve.robotPos[2]);
        telemetry.addData("Angular Rate is", swerve.angularRate());


        telemetry.addData("Found Tape", motors.foundTape);
        telemetry.addData("Is Tape", motors.isTape());
        telemetry.addData("Is game element", motors.hasGameElement());
        telemetry.addData("Has pollen", motors.isPollen());
        /*
        telemetry.addData("Hue is", motors.csHSV[0]);
        telemetry.addData("Saturation is", motors.csHSV[1]);
        telemetry.addData("Value is", motors.csHSV[2]);
        telemetry.addData("Pollen In Range", motors.inRangeHSV(motors.pollenRangeHSV, motors.csHSV));
        telemetry.addData("Blue Nectar In Range", motors.inRangeHSV(motors.blueNectarRangeHSV, motors.csHSV));
        telemetry.addData("Red Nectar In Range", motors.inRangeHSV(motors.redNectarRangeHSV, motors.csHSV));
        telemetry.addData("Homing Tape In Range", motors.inRangeHSV(motors.homingTapeRangeHSV, motors.csHSV));
         */

        /*
        telemetry.addData("Green is", motors.csValue[1]);
        telemetry.addData("Spindex Homing", motors.spindexHoming);
        telemetry.addData("Spindex Encoder", motors.spindex.getCurrentPosition());
        telemetry.addData("Spindex Mode", motors.spindex.getMode());
         */
        //telemetry.addData("Spindex set pos is", motors.spindexSetPos);
        //telemetry.addData("Spindex run power is", motors.runPower);

        motors.update(getRuntime());
        telemetry.addData("Hood Position is", motors.hood.getPosition());
        swerve.updateSwerve(deltaTime);

        telemetry.update();
    }
}
