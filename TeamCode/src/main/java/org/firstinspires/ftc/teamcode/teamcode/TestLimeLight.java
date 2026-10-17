package org.firstinspires.ftc.teamcode.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;


@TeleOp
public class TestLimeLight extends OpMode {
    ActuatorsSensorsDiffySwerve motors;
    PIDFImplement tiltAxonPID;
    PIDFImplement panAxonPid;
    PIDFImplement panAxonPidFar;
    DataBuffer tiltData;
    DataBuffer tiltServoData;
    DataBuffer tiltLimelightData;
    CSVLogger logger;
    TimerImplement logTimer;
    double lastTime;
    double deltaTime;
    boolean panPIDMode = false;
    boolean tiltPIDMode = false;
    boolean panPIDModeFar = false;
    boolean calibrateTilt;
    double calibrateTiltPower;
    boolean calibratePan;
    double calibratePanPower;
    double tiltPower;
    double panPower;
    boolean goTiltMiddle;
    int tiltPosNum;
    //For finding tilt home
    boolean testTilt;
    boolean testTelem;
    boolean timerDone;
    private TelemetryManager telemetryManager;

    double limitUpperTilt = 2.45;
    double limitLowerTilt = 1.8;
    double limitLowerPan = 1.1;
    double limitUpperPan = 1.6;
    double powerScale = 0.5;
    double panPowerClose;
    double panPowerFar;
    int changeMode = 0;
    boolean useClose;
    double closeBandIncrease;
    double logarithmicTuning = 1;
    double logScale = 0.1;
    double logPower;
    boolean useLogTune;
    double tx;


    @Override
    public void init() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        motors = new ActuatorsSensorsDiffySwerve();
        tiltAxonPID = new PIDFImplement();
        panAxonPid = new PIDFImplement();
        panAxonPidFar = new PIDFImplement();
        tiltData = new DataBuffer(3000);
        tiltServoData = new DataBuffer(3000);
        tiltLimelightData = new DataBuffer(3000);
        logTimer = new TimerImplement();

        tiltAxonPID.setConstants(0, 0, 0, 0, 0.05, 0.0001);
        panAxonPid.setConstants(0.003,0.02,0,0,0.05,0.0001);
        panAxonPidFar.setConstants(0.004, 0.02, 0.015, 0, 0.05, 0.0001);

        motors.init(hardwareMap);
        motors.limeLight.start();
        motors.limeLight.pipelineSwitch(2);

        //Kinda vibecode ngl
        try {
            logger = new CSVLogger(
                    "Tilt Test 01",
                    "TimeStamp", 
                    "Raw Tilt2",
                    "Mean Tilt 2",
                    "StdDev Tilt 2",
                    "Range Tilt 2",
                    "Median Tilt2",
                    "Raw LL",
                    "Mean LL",
                    "Range LL",
                    "Raw Servo",
                    "Mean Servo",
                    "Range Servo"
            );
        } catch (IOException e) {
            telemetry.addData("Logger Error", e.getMessage());
        }
    }

    @Override
    public void loop() {
        timerDone = logTimer.setTimer(getRuntime(), 0.1);
        motors.LimeLightResult();
        motors.createPairs();
        motors.createClusters();
        deltaTime = getRuntime() - lastTime;
        tx = motors.limeLight.getLatestResult().getTx();
        /*
        tiltData.add(motors.getTiltAbs2());
        tiltLimelightData.add(motors.limeLight.getLatestResult().getTy());
        tiltServoData.add(motors.getTilt());

         */


        // telemetry.addData("Ball Count is:", motors.getBallCount());
        //telemetry.addData("LimeLight Target X is:", motors.getTarget(0));
        //telemetry.addData("LimeLight Target Y is:", motors.getTarget(1));
        //telemetry.addData("LimeLight Target Area is:", motors.getTarget(2));
        //telemetry.addData("Total Pan is:", motors.getPanAbs());
        if (gamepad1.rightBumperWasPressed()) {
            testTelem = !testTelem;
        }
        if (!testTelem) {
            tiltData.add(motors.getTiltAbs2());
        }
        /*
        telemetry.addData("Total Tilt is:", motors.getTiltAbs());
        telemetry.addData("Total Tilt 2 is:", motors.getTiltAbs2());
        telemetry.addData("Tilt 2 mean is:", tiltData.getMean());
        telemetry.addData("Tilt 2 range is:", tiltData.getRange());
        telemetry.addData("Tilt 2 StdDev is:", tiltData.getDeviation());

         */

        int i = 1;
        /*
        for (BallData ball : motors.getBalls()) {
            telemetry.addData("Ball " + i + " X", ball.x);
            telemetry.addData("Ball " + i + " Y", ball.y);
            telemetry.addData("Ball " + i + " Area", ball.area);
            telemetry.addData("Ball" + i + "Id", ball.id);
            telemetry.addData("Ball" + i + "Normalized X", ball.getNormalizedX());
            i++;
        }

         */
        /*
        for (BallPair pair : motors.getPairs()) {

            telemetry.addData("Pair", pair.ball1.id + "," + pair.ball2.id);
            telemetry.addData("dx", pair.dx);
            telemetry.addData("area", pair.areaRatio);
        }

         */

        int clusterNum = 1;
            /*
        for (BallCluster cluster : motors.getClusters()) {

            telemetry.addData(
                    "Cluster " + clusterNum,
                    cluster.balls.size()
            );



            clusterNum++;
        }

             */
        //telemetry.addData("Pairs", motors.getPairs().size());
        int j = 1;

        for (BallCluster cluster : motors.getClusters()) {
            double x = motors.getCenterX(cluster);
            double y = motors.getCenterY(cluster);
            /*
            telemetry.addData(
                    "Cluster " + j,
                    "size=%d avgArea=%.3f x=%.3f y=%.3f relX=%.3f relY=%.3f relRadius=%.3f",
                    cluster.balls.size(),
                    motors.getAverageArea(cluster),
                    x,
                    y,
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [0],
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [1],
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [2]
            );

             */

            j++;
        }




        //Debug
        //telemetry.addData("LL Tx is:", tx);
        //telemetry.addData("LL Ty is:", motors.limeLight.getLatestResult().getTy());


        lastTime = getRuntime();

        telemetry.addData("Tilt is:", motors.getTilt());
        telemetry.addData("Pan is:", motors.getPan());
        // telemetry.addData("Pan PID out is:", panAxonPid.PIDFOutPut(tx, deltaTime));

        if (gamepad1.bWasPressed()) {
            motors.readDistSens();
        }
        //telemetry.addData("Distance Sensor Says (Cm):", motors.getDistCm());





        telemetry.addData("Delta Time is:", deltaTime);

        // Get Servo Powers
        tiltPower = tiltAxonPID.PIDFOutPut(motors.limeLight.getLatestResult().getTy(), deltaTime);
        panPowerClose = panAxonPid.PIDFOutPut(tx, deltaTime);
        panPowerFar = panAxonPidFar.PIDFOutPut(tx, deltaTime);

        if (gamepad2.rightBumperWasPressed()) {
            useLogTune = !useLogTune;
        }

        if (!useLogTune) {
            if (gamepad2.dpadUpWasPressed()) {
                closeBandIncrease += 0.5;
            }
            if (gamepad2.dpadDownWasPressed()) {
                closeBandIncrease -= 0.5;
            }
            if (gamepad2.leftTriggerWasPressed()) {
                useClose = !useClose;
            }


            //PID tuning for pan
            if (changeMode % 3 == 0) {
                panPIDModeFar = true;
                telemetryManager.addData("Far Pan PID Mode:", panPIDModeFar);
            } else if (changeMode % 3 == 1) {
                panPIDMode = true;
                telemetryManager.addData("Pan PID Mode:", panPIDMode);
            } else if (changeMode % 3 == 2) {
                tiltPIDMode = true;
                telemetryManager.addData("Tilt PID Mode:", tiltPIDMode);
            }
            if (panPIDMode) {
                panAxonPid.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
                panAxonPid.tuneConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
                telemetry.addData("Pan PID mode is:", panAxonPid.getMode());
                if (panAxonPid.getMode() % 4 == 0) {
                    telemetry.addData("KP is:", "kP=%.6f",
                            panAxonPid.getkP());
                }
                if (panAxonPid.getMode() % 4 == 1) {
                    telemetry.addData("KI is:", "kI=%.6f", panAxonPid.getkI());
                }
                if (panAxonPid.getMode() % 4 == 2) {
                    telemetry.addData("KD is:", "kD=%.6f", panAxonPid.getkD());
                }
                if (panAxonPid.getMode() % 4 == 3) {
                    telemetry.addData("KF is:", "%.6f", panAxonPid.getkF());
                }
            } else if (tiltPIDMode) {
                tiltAxonPID.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
                tiltAxonPID.tuneConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
                telemetry.addData("Tilt PID mode is:", tiltAxonPID.getMode());
                if (tiltAxonPID.getMode() % 4 == 0) {
                    telemetry.addData("KP is:", "kP=%.6f", tiltAxonPID.getkP());
                }
                if (tiltAxonPID.getMode() % 4 == 1) {
                    telemetry.addData("KI is:", tiltAxonPID.getkI());
                }
                if (tiltAxonPID.getMode() % 4 == 2) {
                    telemetry.addData("KD is:", tiltAxonPID.getkD());
                }
                if (tiltAxonPID.getMode() % 4 == 3) {
                    telemetry.addData("KF is:", tiltAxonPID.getkF());
                }
            } else if (panPIDModeFar) {
                panAxonPidFar.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
                panAxonPidFar.tuneConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
                telemetry.addData("Tilt PID mode is:", panAxonPidFar.getMode());
                if (panAxonPidFar.getMode() % 4 == 0) {
                    telemetry.addData("KP is:", "kP=%.6f", panAxonPidFar.getkP());
                }
                if (panAxonPidFar.getMode() % 4 == 1) {
                    telemetry.addData("KI is:", panAxonPidFar.getkI());
                }
                if (panAxonPidFar.getMode() % 4 == 2) {
                    telemetry.addData("KD is:", panAxonPidFar.getkD());
                }
                if (panAxonPidFar.getMode() % 4 == 3) {
                    telemetry.addData("KF is:", panAxonPidFar.getkF());
                }
            }
            if (gamepad1.aWasPressed()) {
                changeMode += 1;
            }


            if (tx <= 5 + closeBandIncrease && tx >= -5 - closeBandIncrease && useClose) {
                panPower = panPowerClose;
                telemetryManager.addData("Using close!", panPowerClose);
                telemetryManager.addData("Band Increase:", closeBandIncrease);
            } else {
                panPower = panPowerFar;
                telemetryManager.addData("Using far!", panPowerFar);
            }
        }
        /**
         * Logarithm Code
         */
        else {
            logPower =  Math.min(0.08, logScale * Math.log(Math.abs((logarithmicTuning * (Math.abs(tx))) + 1)));

            telemetryManager.addData("Using LogTune! Logtune is:", logarithmicTuning);
            telemetryManager.addData("LogScale is:", logScale);
            if (tx > 0) {
                panPower = logPower;
            } else {
                panPower = - logPower;
            }
            if (gamepad2.dpadRightWasPressed()) {
                logarithmicTuning += 0.01;
            }
            if (gamepad2.dpadLeftWasPressed()) {
                logarithmicTuning -= 0.01;
            }
            if (gamepad2.dpadUpWasPressed()) {
                logScale += 0.001;
            }
            if (gamepad2.dpadDownWasPressed()) {
                logScale -= 0.001;
            }
        }

        telemetryManager.addData("Tx is", tx);
        telemetryManager.addData("Ty is:", motors.limeLight.getLatestResult().getTy());
        telemetryManager.addData("Integral is:", panAxonPid.getInt());

        telemetryManager.addData("TiltPower Initial is:", tiltPower);
        telemetryManager.addData("PanPower is", panPower);
        telemetryManager.addData("TestValue", 5);
        /**
         * Limit Code
         */
        if (motors.tiltPot.getVoltage() <= limitLowerTilt && tiltPower > 0) {
            tiltPower = 0;
            motors.tiltAxon.setPower(0);
        } else if (motors.tiltPot.getVoltage() >= limitUpperTilt && tiltPower < 0) {
            tiltPower = 0;
            motors.tiltAxon.setPower(0);
        } else {
            motors.tiltAxon.setPower(tiltPower);
        }

        if (motors.panPot.getVoltage() <= limitLowerPan && panPower > 0) {
            panPower = 0;
            motors.panAxon.setPower(0);
        } else if (motors.panPot.getVoltage() >= limitUpperPan && panPower < 0) {
            panPower = 0;
            motors.panAxon.setPower(0);
        } else {
            motors.panAxon.setPower(panPower);
        }
        /*
        telemetryManager.addData("Total Tilt", motors.getTiltAbs());
        telemetryManager.addData("Total Tilt 2", motors.getTiltAbs2());
        telemetryManager.addData("Tilt 2 Mean", tiltData.getMean());
        telemetryManager.addData("Last Error Tilt:", tiltAxonPID.lastError);
        telemetryManager.addData("Last Error Pan:", panAxonPid.lastError);
        telemetryManager.addData("Tilt Power:", tiltPower);
        telemetryManager.addData("Pan Power:", panPower);

         */
        /*
        telemetryManager.addData("Tilt 2 Range", tiltData.getRange());
        telemetryManager.addData("Tilt 2 StdDev", tiltData.getDeviation());

        telemetryManager.addData("LL Tx", tx);
        telemetryManager.addData("LL Ty", motors.limeLight.getLatestResult().getTy());

        telemetryManager.addData("Tilt", motors.getTilt());
        telemetryManager.addData("Pan", motors.getPan());
        telemetryManager.addData("Distance", motors.getDistCm());

        telemetryManager.addData("Simple", 5);

         */
        telemetryManager.update(telemetry);
    }

    @Override
    public void stop() {
        logger.close();
        super.stop();
    }
}
