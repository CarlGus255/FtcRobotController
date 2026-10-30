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
    double scale = 1;
    double translationBound = 1.0;
    double rotationBound = 2.0; // Ensure your kinematics expect rotation to be weighted higher
    double motorPowerMax = 0.9;

    public void driveMotors() {
        double truncatedTranslation = Math.max(-translationBound, Math.min(translationBound, translation));
        double truncatedRotation = Math.max(-rotationBound, Math.min(rotationBound, rotation));

        double rawTop = truncatedTranslation + truncatedRotation;
        double rawBottom = -truncatedTranslation + truncatedRotation;

        double maxRequested = Math.max(Math.abs(rawTop), Math.abs(rawBottom));

        if (maxRequested > motorPowerMax) {
            rawTop = (rawTop / maxRequested) * motorPowerMax;
            rawBottom = (rawBottom / maxRequested) * motorPowerMax;
        }

        // 5. Send optimized power to the motor controllers
        topMotor.setPower(rawTop);
        bottomMotor.setPower(rawBottom);
    }
    public void oldDriveMotors() {
        topMotor.setPower(translation + rotation);
        bottomMotor.setPower(translation - rotation);
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

        optimizedReversed = false;
        translation = 0;
        setHeading = 0;

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
        headingError = normalize180(headingError);
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
    /*
    public void vectorPod(double angle, double translation) {
        this.translation = translation;
        setHeading = angle;

    }

     */
    public void vectorPod(double angle, double requestedTranslation) {

        // Update the current wheel heading
        computeHeadingDegree();

        // Candidate A: preserve wheel direction
        double normalTarget = normalize360(angle);
        double normalError = normalize180(normalTarget - heading);

        // Candidate B: reverse wheel direction
        double reversedTarget = normalize360(angle + 180);
        double reversedError = normalize180(reversedTarget - heading);

        double normalErrorAbs = Math.abs(normalError);
        double reversedErrorAbs = Math.abs(reversedError);

        // Choose the shortest steering path, with hysteresis
        if (!optimizedReversed) {

            if (reversedErrorAbs + optimizationHysteresis
                    < normalErrorAbs) {
                optimizedReversed = true;
            }

        } else {

            if (normalErrorAbs + optimizationHysteresis
                    < reversedErrorAbs) {
                optimizedReversed = false;
            }
        }

        // Apply the chosen target and drive direction
        if (optimizedReversed) {
            setHeading = reversedTarget;
            translation = -requestedTranslation;
        } else {
            setHeading = normalTarget;
            translation = requestedTranslation;
        }
    }
    // Tracks which wheel direction is currently selected
    boolean optimizedReversed = false;

    // Prevents rapid switching when both choices are nearly equal
    double optimizationHysteresis = 5; // degrees

    private double normalize360(double angle) {
        return ((angle % 360) + 360) % 360;
    }

    private double normalize180(double angle) {
        return ((angle + 180) % 360 + 360) % 360 - 180;
    }

}
