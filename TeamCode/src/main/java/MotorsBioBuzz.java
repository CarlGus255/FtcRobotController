import android.graphics.Color;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.teamcode.PIDFImplement;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.ArrayList;


public class MotorsBioBuzz {
    BioBuzzDiffySwerve diffy;
    DcMotorEx shooter;
    DcMotorEx spindex;
    DcMotorEx intake; // this encoder port will be used for external bore encoder
    DcMotorEx turret;
    TouchSensor indexHomingSwitch;
    PIDFImplement turretPID;
    //VisionPortal webcam;
    // CRServo spindex;
    ArrayList<AprilTagReading> aprilTagReadings = new ArrayList<>();
    ColorSensor spindexColor;

    Servo hood;


    public void init (HardwareMap hwMap) {


        /*
        webcam = new VisionPortal.Builder()
                .setCamera(hwMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

         */

        turretPID = new PIDFImplement();
        turretPID.setConstants(0.1, 0, 0, 0, 0.1, 0.001);
        
        shooter = hwMap.get(DcMotorEx.class, "shooter");
        turret = hwMap.get(DcMotorEx.class, "turret");
        spindex = hwMap.get(DcMotorEx.class, "indexer");
        intake = hwMap.get(DcMotorEx.class, "intake");
        hood = hwMap.get(Servo.class, "hood");
        indexHomingSwitch = hwMap.get(TouchSensor.class, "indexSwitch");
        //spindexColor = hwMap.get(ColorSensor.class, "cs");
        spindexColor = hwMap.get(ColorSensor.class, "cs");

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Use this encoder for the spindex encoder
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindex.setTargetPosition(0);
        spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spindex.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        diffy = new BioBuzzDiffySwerve(hwMap);

        turretRange[0] = -200;
        turretRange[1] = 200;

        csHSV = new float[3];
        csValue = new int[3];
    }


    //Color Sensor Thresholds - H, S, V, % Confidence H, % Confidence S, % Confidence V
    double[] pollenRangeHSV  = {52.0, 0.554, 0.900, 0.05, 0.10, 0.40};
    double[] blueNectarRangeHSV = {209.5, 0.646, 0.227, 0.05, 0.15, 0.10};
    double[] redNectarRangeHSV = {4.0, 0.57, 0.360, 0.05, 0.15, 0.15};
    double[] homingTapeRangeHSV = {52.5, 0.138, 0.278, 0.05, 0.10, 0.10};
    //Store cell values - 0 -> nothing, 1 -> pollen, 2 -> red Nectar, 3 -> blue Nectar
    int[] cellValues = {0, 0, 0, 0};
    int cellNum = 0;
    double spindexSetPos;
    boolean spindexHoming;

    public void homeSpindex() {
        spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spindexHoming = true;
    }

    double spindexHomingPower = 0.2;
    boolean foundTape;
    int[] csValue;
    float[] csHSV;

    public boolean inRangeHSV(double[] metric, float[] sample) {
        boolean hueInRange = false;
        boolean satInRange = false;
        boolean valueInRange = false;
        double hueDiff;
        double satDiff;
        double valueDiff;
        double percentHue = metric[3];
        double percentSat = metric[4];
        double percentValue = metric[5];
        hueDiff = sample[0] - metric[0];
        if (Math.abs(hueDiff) < (percentHue * 360)) {
            hueInRange = true;
        }
        satDiff = sample[1] - metric[1];
        if (Math.abs(satDiff) < (percentSat * 1)) {
            satInRange = true;
        }
        valueDiff = sample[2] - metric[2];
        if (Math.abs(valueDiff) < (percentValue * 1)) {
            valueInRange = true;
        }
        if (hueInRange && satInRange && valueInRange) {
            return true;
        } else {
            return false;
        }
    }
    public void computeCSHSVValues() {
        csValue [0] = spindexColor.red();
        csValue [1] = spindexColor.green();
        csValue [2] = spindexColor.blue();

        Color.RGBToHSV(csValue[0],csValue[1], csValue[2], csHSV);

        //Color.RGBToHSV(csValue[0], csValue[1], csValue[2], csHSV);
    }
    /*
    boolean colorSensorFoundTape() {
        computeCSHSVValues();
        if (35 < csHSV[0] && csHSV[0] < 50) {
            foundTape = true;
        } else {
            foundTape = false;
        }
        return foundTape;
    }

     */
    double ticksPerRev = 537.7 * 14/10;

    public void setSpindexDeg(double pos) {
        spindexSetPos = ((pos/360)*ticksPerRev);
        spindex.setTargetPosition((int) spindexSetPos);
    }
    double spindexSetPosDeg;
    public void runSpindexDeg(double inc) {
        spindexSetPosDeg += inc;
    }
    boolean switching;
    boolean spindexZereod;
    double spindexError;
    int spindexState;
    public void updateSpindex() {
        if (spindexHoming) {
            computeCSHSVValues();
            if (spindex.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            spindex.setPower(spindexHomingPower);
            if (isTape()) {
                spindex.setPower(0);
                spindexSetPosDeg = 0;
                spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                runSpindexDeg(45);
                //spindexHoming = false;
                spindexZereod = true;
                cellNum = 0;
            }
            if (spindexZereod) {
                spindexError = Math.abs(spindex.getCurrentPosition() - spindexSetPos);
                if (cellNum <= 3) {
                    if (spindexError <= 20) {
                        readCell();
                        incrementSpindex(1);
                    }
                }
            }
        }
        else if (runPower != 0){
            if (spindex.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            spindex.setPower(runPower);
        } else {
            //Make sure correct mode is set
            if (spindex.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            //Auto rotate when pollen in current slot
            /*
            if (Math.abs(spindex.getCurrentPosition() - spindexSetPos) < 20) {
                if (isPollen() && !switching) {
                    runSpindexDeg(90);
                }
            }
             */
            //Set the spindex
            setSpindexDeg(spindexSetPosDeg);
            //Fight stalls
            if (Math.abs(spindex.getCurrentPosition() - spindexSetPos) < 20) {
                spindex.setPower(0);
            } else {
                spindex.setPower(0.9);
            }

        }
    }
    public void readCell() {
        computeCSHSVValues();
        // 0 is nothing, 1 is pollen, 2 is red nectar, 3 is blue nectar
        if (isPollen()) {
            cellValues[cellNum] = 1;
        } else if (isRedNectar()) {
            cellValues[cellNum] = 2;
        } else if (isBlueNectar()) {
            cellValues[cellNum] = 3;
        } else {
            cellValues[cellNum] = 0;
        }
    }
    double runPower;
    public void runSpindex(double power) {
        runPower = power;
    }
    public boolean isPollen() {
        return inRangeHSV(pollenRangeHSV, csHSV);
    }
    public boolean hasGameElement() {
        return inRangeHSV(pollenRangeHSV, csHSV) || inRangeHSV(blueNectarRangeHSV, csHSV) || inRangeHSV(redNectarRangeHSV, csHSV);
    }
    public boolean isRedNectar() {
        return inRangeHSV(redNectarRangeHSV, csHSV);
    }
    public boolean isBlueNectar() {
        return inRangeHSV(blueNectarRangeHSV, csHSV);
    }
    public boolean isTape() {
        return inRangeHSV(homingTapeRangeHSV, csHSV);
    }
    public void incrementSpindex(int numInc) {
        runSpindexDeg(90 * numInc);
        cellNum += 1;
    }
    public void decrementSpindex() {
        runSpindexDeg(-90);
        cellNum -= 1;
    }
    public int cyclesTillNext(int gameElement) {
        int rotationsTillNext = 5;
        for (int i = 0; i < cellValues.length; i++) {
            if (cellValues[cellNum + i] == gameElement && i < rotationsTillNext) {
                rotationsTillNext = i;
            }
        }
        if (rotationsTillNext != 5) {
            return rotationsTillNext;
        }  else {
            return -1;
        }
    }
    public void selectIntakeEmptyCell() {
        int cycles = cyclesTillNext(0);
        if (hasEmpty()) {
            incrementSpindex(cycles);
        }
    }
    public boolean hasEmpty() {
        if (cyclesTillNext(0) == -1) {
            return false;
        } else {
            return true;
        }
    }
    /*
    public void updateSpindexPower() {
        computeSpidnexError();
        if (isHoming) {
            isPressed = indexHomingSwitch.isPressed();
            if (!hasHomed && !isPressed) {
                spindex.setPower(homingPower);
            }
            if (!hasHomed && isPressed) {
                spindex.setPower(0);
                hasHomed = true;
                isHoming = false;
                intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
        if (autoSpin) {
            if (!doneSpin) {
                if (!searchedSpin) {
                    incrementSpindex(nextDesiredIncrements);
                    searchedSpin = true;
                }
                if (searchedSpin && spindexInRange()) {
                    doneSpin = true;
                }
            }
            if (doneSpin && notEmpty) {
                incrementSpindex((-1));
                autoSpin = false;
            } else if (doneSpin && !notEmpty) {
                autoSpin = false;
            }
        }
        if (!isHoming) {
            spindex.setPower(spindexPID.PIDFOutPut(spindexError, deltaTime)/8192);
        }
    }

     */
    boolean notEmpty;
    int nextDesiredIncrements;
    boolean autoSpin;
    boolean hasHomed;
    boolean isHoming;
    double homingPower = -1;
    boolean isPressed;
    /*
    public void homeSpindex() {
        hasHomed = false;
        isHoming = true;
    }

     */
    //final public float[][] cellsHSV = new float[4][3];
    /*
    public void readColorSensors () {
        Color.RGBToHSV(cs1.red() * 255, cs1.green() * 255, cs1.blue() * 255, cellsHSV[0]);
        Color.RGBToHSV(cs2.red() * 255, cs2.green() * 255, cs2.blue() * 255, cellsHSV[1]);
        Color.RGBToHSV(cs3.red() * 255, cs3.green() * 255, cs3.blue() * 255, cellsHSV[2]);
        Color.RGBToHSV(cs4.red() * 255, cs4.green() * 255, cs4.blue() * 255, cellsHSV[3]);
    }


    double[] pollenHueRange = new double[2];
    public void setHueRange() {
        pollenHueRange[0] = 40;
        pollenHueRange[1] = 80;
        nectarRangeRed[0] = 0; //Lower red range for nectar HSV value
        nectarRangeRed[1] = 20;
        nectarRangeBlue[0] = 100;//Lower blue range for nectar HSV value
        nectarRangeBlue[1] = 140;
    }
    public float[] getCS1HSV() {
        readColorSensors();
        return cellsHSV[0];
    }

    public double[] nectarRangeBlue = new double[2];
    public double[] nectarRangeRed = new double[2];
    int[] spinCells = new int[4];
    int cs1Value;
    int cellsValues[] = new int[4];
    public void computeColorCells (){
        for (int i = 0; i < 4; i++) {
            if (
                cellsHSV[i][0] > pollenHueRange[0] &&
                cellsHSV[i][0] < pollenHueRange[1]
            ) {
                //Searches for pollen and inputs 1 if pollen
                cellsValues[i] = 1;
            }
            if (
                cellsHSV[i][0] > nectarRangeRed[0] &&
                cellsHSV[i][0] < nectarRangeRed[1]
            ) {
                //Searches for nectar and inputs 2 if red nectar
                cellsValues[i] = 2;
            }
            if (
                cellsHSV[i][0] > nectarRangeBlue[0] &&
                cellsHSV[i][0] < nectarRangeBlue[1]
            ) {
                //Searches for nectar and inputs 2 if red nectar
                cellsValues[i] = 3;
            }
            else {
                //If nothing is found its given 0 for empty
                cellsValues[i] = 0;
            }
        }
    }
    int nextIncsPollen;
    int incCount;
    int selectedCell;
    public void findNextPollenIncrements() {
        computeColorCells();
        nextIncsPollen = 4;
        selectedCell = (incCount%4);
        for (int i = 0; i < 4; i++) {
            if (spinCells[(i+selectedCell)%4] == 1 && i < nextIncsPollen) {
                nextIncsPollen = i;
            }
        }

    }
    int nextIncsNectar;
    public void findNextNectarIncrements() {
        computeColorCells();
        nextIncsNectar = 4;
        for (int i = 0; i < 4; i++) {
            if ((spinCells[(i+(incCount%4)-1)%4] == 2 || spinCells[(i+(incCount%4)-1)%4] == 3) && i < nextIncsNectar) {
                nextIncsNectar = i;
            }
        }
    }
    public void findNextEmptyIncrements() {
        computeColorCells();
        nextIncsEmpty = 4;
        selectedCell = (incCount%4);
        for (int i = 0; i < 4; i++) {
            if (spinCells[(i+selectedCell)%4] == 0 && i < nextIncsEmpty) {
                nextIncsEmpty = i;
            }
        }
    }
    boolean spindexHasEmpty;
    public boolean spindexHasEmpty() {
        spindexHasEmpty = false;
        for (int i = 0; i < 4; i++) {
            if (spinCells[i] == 0) {
                spindexHasEmpty = true;
            }
        }
        return spindexHasEmpty;
    }


    int nextIncsEmpty;
    boolean doneSpin;
    boolean searchedSpin = true;
    boolean foundOut;
    public void spindexOutNextPollen () {
        findNextPollenIncrements();
        nextDesiredIncrements = nextIncsPollen;
        doneSpin = false;
        searchedSpin = false;
        autoSpin = true;
        notEmpty = true;
    }
    public void spindexTowardsNextEmpty () {
        findNextEmptyIncrements();
        nextDesiredIncrements = nextIncsEmpty;
        doneSpin = false;
        searchedSpin = false;
        autoSpin = true;
        notEmpty = false;
    }

    public void setIntakePower (double power) {
        intake.setPower(power);
    }

     */

    double turretPower;
    double turretError;
    double deltaTime;
    double lastTime;
    public void updateDeltaTime (double currentTime) {
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
    }
    public double getDeltaTime () {
        return deltaTime;
    }
    boolean manualTurret = true;
    public void debugSetTurretError(double error) {
        turretError = error;
        manualTurret = true;
    }
    double seekTurretPos;
    public void changeTurretPos(double increment) {
        seekTurretPos += increment;
        manualTurret = false;
    }
    double[] turretRange = new double[2];
    double turretGearRatio = 0.2;
    double getTurretPositionDeg() {
        return (turret.getCurrentPosition()*turretGearRatio/383.6) * 360; // from ticks from motor to ticks on output to rev on output to deg on output
    }
    boolean getTurretInBounds() {
        if (getTurretPositionDeg() > turretRange[0] && getTurretPositionDeg() < turretRange[1]) {
            return true;
        } else {
            return false;
        }
    }
    public void updateTurretPower () {
        //Manual override logic
        if (!manualTurret) {
            turretError = seekTurretPos - getTurretPositionDeg();
        }

        turretPower = turretPID.PIDFOutPut(turretError, deltaTime);

        //BoundingLogic
        if ((getTurretPositionDeg() <= turretRange[0] && turretPower < 0) || (getTurretPositionDeg() >= turretRange[1] && turretPower > 0)) {
            turret.setPower(0);
        }
        else {
            turret.setPower(turretPower);
        }


    }
    int numTags;
    /*
    public void readTags() {
        aprilTagReadings.clear();


        for (AprilTagDetection detection : aprilTag.getDetections()) {

            if (detection.ftcPose != null) {
                double distance = detection.ftcPose.range;
                double bearing = detection.ftcPose.bearing;

                double pitch = detection.ftcPose.pitch;
                double roll = detection.ftcPose.roll;
                double yaw = detection.ftcPose.yaw;

                double x = detection.ftcPose.x;
                double y = detection.ftcPose.y;
                double z = detection.ftcPose.z;


                aprilTagReadings.add(
                        new AprilTagReading(
                                detection.id,
                                distance,
                                bearing,
                                pitch,
                                roll,
                                yaw,
                                x,
                                y,
                                z
                        )
                );
            }
        }
    }
    public AprilTagReading getTag(int id) {
        for (AprilTagReading reading : aprilTagReadings) {
            if (reading.id == id) {
                return reading;
            }
        }

        return null;
    }
    public ArrayList<AprilTagReading> getAprilTagReadings() {
        return aprilTagReadings;
    }
    int[] redTags1 = {38, 39, 40, 41};
    public AprilTagReading[] getTags(int[] tagIDs) {
        AprilTagReading[] tags = new AprilTagReading[tagIDs.length];

        for (int i = 0; i < tagIDs.length; i++) {
            tags[i] = getTag(tagIDs[i]);
        }

        return tags;
    }

     */
    /**
     * Old Shooter Code attempt ig
     */
/*
    final double shooterTicksPerRad = 23.09;
    double shooterVelocityRad;
    double optimalAngleDeg = 45;

    public void calculateShooterVel () {

    }

    public void updateShooterVel() {
        //calculateShooterVel();
        shooter.setVelocity(shooterVelocityRad/shooterTicksPerRad);
    }
    public void setManualShooterVel(double increment) {
        shooterVelocityRad += (increment * 3.1415/30);//increments in rpm converting to shooter vel
    }
    double boundFlyVel = 200;
    public double getShooterVelocityRpm () {
        return shooter.getVelocity()/shooterTicksPerRad * 30/3.1415; // converting ticks/second to rev/minute
    }
    public double getSetShooterVelocityRpm () {
        calculateShooterVel();
        return shooterVelocityRad*(30/3.1415);
    }
    double flyVelError;
    public double getError() {
        flyVelError = getShooterVelocityRpm() - getSetShooterVelocityRpm();
        return flyVelError;
    }
    public boolean flyVelInBounds () {
        getError();
        if (flyVelError < boundFlyVel && flyVelError >  -boundFlyVel) {
            return true;
        }
        else {
            return false;
        }
    }
    public int flyVelInBounds01 () {
        if (flyVelInBounds()) {
            return 1;
        }
        else {
            return 0;
        }
    }

 */
    boolean isTilt;
    public boolean hiveIsTilted () {
        return isTilt;
    }
    /*
    public AprilTagProcessor getAprilTagProcessor() {
        return aprilTag;
    }

     */
    double hoodPos;
    double hoodAngle;
    final double hoodLowPos = 0.5; // servo always set to 0.5 as zero -> set at 15 degrees angle (lower limit)
    final double hoodUpperPos = 0.5 + ((double) 30 /360); // Up to 30 degrees more
    public void setHoodAngle (double angle) {
        hoodPos = (angle/360) + 0.5;
        hoodAngle = hoodPos * 360;
        if (hoodAngle < 15) {
            hoodPos = hoodLowPos;
        }
        if (hoodAngle > 45) {
            hoodPos = hoodUpperPos;
        }
        hood.setPosition(hoodPos);
    }

    double initLaunchVel;
    double shooterRpm;
    public void updateShooter(double xDist, double yDist) {
        /*


         */

    }
    double g = 9.81;
    double angle;
    public void updateLaunchAngle(double xDist, double yDist) {
        shooterRpm = shooter.getVelocity() * 2.143;
        initLaunchVel = 0.003768 * shooterRpm;

        double sqrtContents = (initLaunchVel*initLaunchVel*initLaunchVel*initLaunchVel) - (g * g * xDist * xDist) + (2 * yDist * initLaunchVel * initLaunchVel);
        double numerator = (initLaunchVel*initLaunchVel) - Math.sqrt(sqrtContents);
        double denominator = g * xDist;
        double quotient = numerator/denominator;

        angle = Math.atan(quotient);

        setHoodAngle(angle);
    }
    double maxHeight;
    public void updateShooterRPM(double xDist, double yDist) {
        maxHeight = yDist + 0.2;
        double sqrt = 2 * maxHeight/g;
        double divisor = 2 * Math.sqrt(sqrt);
        //double square =
    }



    public void update(double currentTime) {
        updateDeltaTime(currentTime);
        updateSpindex();
        //updateShooterVel();
        //updateTurretPower();
    }
}
