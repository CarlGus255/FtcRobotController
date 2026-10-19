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
        spindexColor = hwMap.get(ColorSensor.class, "cs");
        

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Use this encoder for the spindex encoder
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindex.setTargetPosition(0);
        spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        diffy = new BioBuzzDiffySwerve(hwMap);

        turretRange[0] = -200;
        turretRange[1] = 200;

        csHSV = new float[3];
        csValue = new int[3];
    }

    double spindexSetPos;
    boolean spindexHoming;
    public void homeSpindex() {
        spindexHoming = true;
    }
    double spindexHomingPower = 0.1;
    boolean foundTape;
    int[] csValue;
    float[] csHSV;
    public void computeCSHSVValues() {
        csValue[0] = spindexColor.red();
        csValue[1] = spindexColor.green();
        csValue[2] = spindexColor.blue();

        Color.RGBToHSV(csValue[0], csValue[1], csValue[2], csHSV);
    }
    boolean colorSensorFoundTape() {
        computeCSHSVValues();
        if (0 < csHSV[0] && csHSV[0] < 40) {
            foundTape = true;
        } else {
            foundTape = false;
        }
        return foundTape;
    }

    public void updateSpindex() {
        if (spindexHoming) {
            spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spindex.setPower(spindexHomingPower);
        }
    }
    public void manualSpindex(double power) {
        spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spindex.setPower(power);
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
}
