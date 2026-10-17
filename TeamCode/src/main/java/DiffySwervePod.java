import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.teamcode.DiffySwervePodDriver;
import org.firstinspires.ftc.teamcode.teamcode.PIDFImplement;

import java.util.ArrayList;

public class DiffySwervePod {
    DcMotorEx topMotor;
    DcMotorEx bottomMotor;
    DigitalChannel homingSwitch;
    PIDFImplement rotationPID;
    PIDFImplement homingPID;
    double[] rotationPIDConstants;
    double[] homingPIDConstants;
    public DiffySwervePod (
            DcMotorEx topMotor,
            DcMotorEx bottomMotor,
            DigitalChannel homingSwitch,
            PIDFImplement rotationPID,
            double[] rotationPIDConstants,
            PIDFImplement homingPID,
            double[] homingPIDConstants
    ) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        this.homingSwitch = homingSwitch;
        this.rotationPID = rotationPID;
        this.homingPID = homingPID;
        this.rotationPIDConstants = rotationPIDConstants;
        rotationPID.setConstants(
                rotationPIDConstants[0],
                rotationPIDConstants[1],
                rotationPIDConstants[2],
                rotationPIDConstants[3],
                rotationPIDConstants[4],
                rotationPIDConstants[5]
        );
        this.homingPIDConstants = homingPIDConstants;
        homingPID.setConstants(
                homingPIDConstants[0],
                homingPIDConstants[1],
                homingPIDConstants[2],
                homingPIDConstants[3],
                homingPIDConstants[4],
                homingPIDConstants[5]
        );
    }
    public void driveMotors() {
        topMotor.setPower(translation + rotation);
        bottomMotor.setPower(-translation + rotation);
    }
    public void debugWheel(double debugTranslation, double debugRotation) {
        topMotor.setPower(debugTranslation + debugRotation);
        bottomMotor.setPower(-debugTranslation + debugRotation);
        translation = debugTranslation;
        rotation = debugRotation;
    }
    public void resetEncoders() {
        topMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        topMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    double heading;
    double headingGearRatio = (384.5) * (2); //Ticks per rev * belt ratio
    public void computeHeadingDegree() {
        heading = ((topMotor.getCurrentPosition() + bottomMotor.getCurrentPosition())/(2*headingGearRatio))*360;
    }
    double setHeading;
    public void setHeading(double setHeadingValue) {
        setHeading = setHeadingValue;
    }
    double translation;
    public void setTranslation(double translationValue) {
        translation = translationValue;
    }
    double rotation;
    boolean homing;
    boolean initialState;
    public void homePod(double numPoints) {
        resetEncoders();
        homing = true;
        initialState = homingSwitch.getState();
        lastState = initialState;
        homingDetections = new ArrayList<Double>();
        this.numPoints = numPoints;
        homingRunToCenter = false;
        homePosSum = 0;
        homePosNum = 0;
        lowerAvgVal = 0;
        lowerAvgNum = 0;
        upperAvgVal = 0;
        upperAvgNum = 0;
        lastEdgeHeadingValue = 600;
        detections = 0;
    }
    int homingDirection = 1;
    public void switchHomingDirection() {
        homingDirection *= -1;
    }
    boolean currentState;
    boolean lastState;
    boolean edgeDetected;
    double lastEdgeHeadingValue;
    double detectionThreshold = 20;
    boolean detectedThreshold;
    int detections;
    int rejections;
    double homingSpeed = 0.1;
    double numPoints;
    boolean homingRunToCenter;
    final double acceptableHomingRange = 130;
    double homePosSum;
    double homePosNum;
    double homeDetMeanValue;
    ArrayList<Double> homingDetections;
    double lowerAvgVal;
    double lowerAvgNum;
    double upperAvgVal;
    double upperAvgNum;
    double centerValue;
    double homingError;
    double headingError;
    public void updatePod(double deltaTime) {
        computeHeadingDegree();
        headingError = setHeading - heading;

        // Normalize error to [-180, 180].
        headingError = ((headingError + 180) % 360 + 360) % 360 - 180;
        if (!homing) {
            rotation = rotationPID.PIDFOutPut((headingError), deltaTime);
            driveMotors();
        }
        if (homing) {
            edgeDetected = false;
            detectedThreshold = false;

            if (detections < numPoints) {
                //If we aren't fully homed
                currentState = homingSwitch.getState();
                if (currentState != lastState) {
                    edgeDetected = true;
                }
                if (edgeDetected) {
                    if (Math.abs(heading - lastEdgeHeadingValue) > detectionThreshold) {
                        detectedThreshold = true;
                    } else {
                        rejections++;
                    }
                    if (detectedThreshold) {
                        detections++;
                        if (!initialState) {
                            switchHomingDirection();
                        } else {
                            initialState = false;
                        }
                        homingDetections.add(heading);
                        lastEdgeHeadingValue = heading;
                    }
                }
                rotation = homingDirection * homingSpeed;
            } else if(!homingRunToCenter) {
                rotation = 0;
                //Calculate the average value of the detections
                for (int i = 0; i<homingDetections.size(); i++) {
                    homePosSum += homingDetections.get(i);
                    homePosNum ++;
                }
                homeDetMeanValue = homePosSum/homePosNum;
                //Split into upper and lower values
                for (int i = 0; i<homingDetections.size(); i++) {
                    if (homingDetections.get(i) <= homeDetMeanValue) {
                        lowerAvgVal += homingDetections.get(i);
                        lowerAvgNum ++;
                    }
                    if (homingDetections.get(i) > homeDetMeanValue) {
                        upperAvgVal += homingDetections.get(i);
                        upperAvgNum ++;
                    }
                }
                //Calculate the average lower value, upper value, and center of rotation
                lowerAvgVal = lowerAvgVal/lowerAvgNum;
                upperAvgVal = upperAvgVal/upperAvgNum;
                centerValue = (lowerAvgVal + upperAvgVal)/2;

                //Fail safe - if the difference is too big, that means that it homed "Outside" the
                //range of the sensor, and will end up 180 degrees off, so this compensates.
                if (Math.abs(upperAvgVal - lowerAvgVal) > acceptableHomingRange) {
                    centerValue += 180;
                }
                homingRunToCenter = true;
            } else if (homingRunToCenter) {
                homingError = centerValue - heading;
                rotation = homingPID.PIDFOutPut(homingError, deltaTime);
                if (Math.abs(homingError)  <= 0.5) {
                    resetEncoders();
                    homing = false;
                }
            }
            translation = 0;
            driveMotors();
            lastState = currentState;
        }
    }
    public void vectorPod(double angle, double translation) {
        this.translation = translation;
        setHeading = angle;

    }

}
