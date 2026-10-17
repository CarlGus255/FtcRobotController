import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.teamcode.PIDFImplement;

public class BioBuzzDiffySwerve {
    DcMotorEx ltm;
    DcMotorEx lbm;
    DcMotorEx rtm;
    DcMotorEx rbm;
    DigitalChannel leftMagnetHome;
    DigitalChannel rightMagnetHome;
    PIDFImplement leftPodRotatePID;
    PIDFImplement rightPodRotatePID;
    PIDFImplement headingPID;
    PIDFImplement leftPodHomingPID;
    //GoBildaPinpointDriver ppo;
    public BioBuzzDiffySwerve (HardwareMap hwMap) {
        ltm = hwMap.get(DcMotorEx.class, "ltm");
        lbm = hwMap.get(DcMotorEx.class, "lbm");
        rtm = hwMap.get(DcMotorEx.class, "rtm");
        rbm = hwMap.get(DcMotorEx.class, "rbm");
        
        leftMagnetHome = hwMap.get(DigitalChannel.class, "lss");
        rightMagnetHome = hwMap.get(DigitalChannel.class, "rss");

        //resetLeftEncoders();
        //resetRightEncoders();

        ltm.setDirection(DcMotorSimple.Direction.FORWARD);
        lbm.setDirection(DcMotorSimple.Direction.FORWARD);
        rtm.setDirection(DcMotorSimple.Direction.FORWARD);
        rbm.setDirection(DcMotorSimple.Direction.FORWARD);

        ltm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rtm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftPodRotatePID = new PIDFImplement();
        leftPodRotatePID.setConstants(0.06, 0.02, 0.002, 0, 0.2, 0.001);
        leftPodHomingPID = new PIDFImplement();
        leftPodHomingPID.setConstants(0.02, 0.05, 0, 0, 0.2, 0.01);
        rightPodRotatePID = new PIDFImplement();
        rightPodRotatePID.setConstants(0, 0, 0, 0, 0.2, 0.001);
        headingPID = new PIDFImplement();
        headingPID.setConstants(0, 0, 0, 0, 0.2, 0.01);

        //lastLeftMagnetState = leftMagnetHome.getState();
        //ppo = hwMap.get(GoBildaPinpointDriver.class, "ppo");
    }
    /*
    double[] fieldPos = new double[3];
    double[] fieldOffsetPos = new double[3];
    public void updateFieldLocation(){
        fieldPos[0] = ppo.getPosX(DistanceUnit.CM) - fieldOffsetPos[0];
        fieldPos[1] = ppo.getPosY(DistanceUnit.CM) - fieldOffsetPos[1];
        fieldPos[2] = ppo.getHeading(AngleUnit.DEGREES) - fieldOffsetPos[2];
    }
    public void resetFieldLocation() {
        fieldOffsetPos = fieldPos;
    }

     */

    public void resetLeftEncoders() {
        ltm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lbm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ltm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lbm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void resetRightEncoders() {
        rtm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rbm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rtm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rbm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    double leftTranslation;
    double leftRotation;
    double rightTranslation;
    double rightRotation;
    double leftHomePower = 0.1;
    double rightHomePower = 0.1;
    boolean leftHoming;
    boolean rightHoming;
    double leftPodHeading;
    double rightPodHeading;
    double setLeftPodHeading;
    double setRightPodHeading;
    double leftPodHeadingError;
    double rightPodHeadingError;
    double deltaTime;
    boolean leftFirstHome;
    boolean leftLastHome;
    double leftFirstHomeValue;
    double leftLastHomeValue;
    double leftHomingError = 1;
    double leftHomePosition;
    /*
    public void updateLeftHoming() {
        if (!leftFirstHome && !leftLastHome) {
            if (leftMagnetHome.getState()) {
                ltm.setPower(leftHomePower);
                lbm.setPower(leftHomePower);
            }
            if (!leftMagnetHome.getState()) {
                ltm.setPower(0);
                lbm.setPower(0);
                leftFirstHomeValue = leftPodHeading;
                leftFirstHome = true;
            }
        }
        if (leftFirstHome && !leftLastHome) {
            if (!leftMagnetHome.getState()) {
                ltm.setPower(leftHomePower * 0.8);
                lbm.setPower(leftHomePower * 0.8);
            }
            if (leftMagnetHome.getState()) {
                ltm.setPower(0);
                lbm.setPower(0);
                leftLastHomeValue = leftPodHeading;
                leftLastHome = true;
                leftHomePosition = (leftFirstHomeValue + leftLastHomeValue)/2;
            }
        }
        if (leftFirstHome && leftLastHome) {
            leftHomingError = leftHomePosition - leftPodHeading;
            if (Math.abs(leftHomingError) > 0.01) {
                leftRotation = leftPodHomingPID.PIDFOutPut(leftHomingError, deltaTime);
                ltm.setPower(leftRotation + leftTranslation);
                lbm.setPower(leftRotation - leftTranslation);
            }
            else {
                resetLeftEncoders();
                ltm.setPower(0);
                rbm.setPower(0);
                leftFirstHome = false;
                leftLastHome = false;
                leftHoming = false;
            }
        }
    }


     */


    /*
    int pointsLeftHome;
    //Set the number of probe points for the left wheel
    public void setLeftHomePoints(int numPoints) {
        if (numPoints < 2) {
            pointsLeftHome = 2;
        } else {
        pointsLeftHome = numPoints;
        }
    }
    //Directionality of homing
    int directionLeftHome = 1;
    public void swapDirectionLeftHome () {
        directionLeftHome *= -1;
    }
    //Count the number of points
    int leftHomePointsReached = 0;
    // Start Accurate Homing Sequence
    public void activateAccurateLeftHoming(int numPoints) {
        leftHoming = true;
        setLeftHomePoints(numPoints);
        leftHomePointsReached = 0;
        lastLeftHomingSwitch = leftMagnetHome.getState();
    }
    boolean lastLeftHomingSwitch;
    //A homing method to home the wheel to a specified number of points
    public void updateAccurateLeftHoming() {
        if (leftHomePointsReached < pointsLeftHome) {
            ltm.setPower(leftHomePower * directionLeftHome);
            lbm.setPower(leftHomePower * directionLeftHome);
            if (leftMagnetHome.getState() != lastLeftHomingSwitch) {
                ltm.setPower(0);
                lbm.setPower(0);
                if (directionLeftHome > 0) {
                    leftFirstHomeValue = leftPodHeading;
                }
                if (directionLeftHome < 0) {
                    leftLastHomeValue = leftPodHeading;
                }
                swapDirectionLeftHome();
                leftHomePointsReached++;
            }
        } else {
            leftHomePosition = (leftFirstHomeValue + leftLastHomeValue)/2;
            leftHomingError = leftHomePosition - leftPodHeading;
            if (Math.abs(leftHomingError) > 0.01) {
                leftRotation = leftPodHomingPID.PIDFOutPut(leftHomingError, deltaTime);
                ltm.setPower(leftRotation);
                lbm.setPower(leftRotation);
            } else {
                ltm.setPower(0);
                lbm.setPower(0);
                resetLeftEncoders();
                leftHoming = false;
            }
        }
        lastLeftHomingSwitch = leftMagnetHome.getState();
    }

     */
    //boolean accurateMode = true;

    public void updateSwerveMotors (double dt) {
        deltaTime = dt;

        //Get heading values for each wheel
        leftPodHeading = (ltm.getCurrentPosition() + lbm.getCurrentPosition()) / 383.6;
        rightPodHeading = (rtm.getCurrentPosition() + rbm.getCurrentPosition()) / 383.6;

        //Get Errors for each heading value
        leftPodHeadingError = setLeftPodHeading - leftPodHeading;
        rightPodHeadingError = setRightPodHeading - rightPodHeading;

        if (!leftHoming) {
            //Run the left wheel in translation and rotation if not homing
            leftRotation = leftPodRotatePID.PIDFOutPut(leftPodHeadingError, deltaTime);
            ltm.setPower(leftRotation + leftTranslation);
            lbm.setPower(leftRotation - leftTranslation);
        }
        if (leftHoming) {
            /*if (!accurateMode) {
                //updateLeftHoming();
            }
             else {
             */
            //updateAccurateLeftHoming();

            updateLeftHomingPoints();
        //}
       }
        if (!rightHoming) {
            //Run the right wheel in translation and rotation if not homing
            rightRotation = rightPodRotatePID.PIDFOutPut(rightPodHeadingError, deltaTime);
            rtm.setPower(rightRotation + rightTranslation);
            rbm.setPower(rightRotation - rightTranslation);
        }
        if (rightHoming) {
            //Homing logic right wheel
            if (rightMagnetHome.getState()) {
                //Until switch is tripped
                rtm.setPower(rightHomePower);
                rbm.setPower(rightHomePower);
            } else {
                //Once switch is tripped
                rtm.setPower(0);
                rbm.setPower(0);
                resetRightEncoders();
                rightHoming = false;
            }
        }
        
    }

    public void homeLeftSwerve () {
        leftHoming = true;
        leftFirstHome = false;
        leftLastHome = false;
    }

    public void homeRightSwerve () {
        rightHoming = true;
    }
    double diffLeftPodHeading;
    double leftPodHeadingOffset;
    public void vectorLeftPod (double magnitude, double direction) {
        //Direction changing code - find the closest way to get to the sought direction while 
        //maintaining wheel direction by flipping seek direction if needed
        diffLeftPodHeading = direction - leftPodHeading + leftPodHeadingOffset;
        /*
        if (diffLeftPodHeading > 180) {
            leftPodHeadingOffset -= 360;
        } else if (diffLeftPodHeading < -180) {
            leftPodHeadingOffset += 360;
        }
        */


        setLeftPodHeading = direction - leftPodHeading + leftPodHeadingOffset;
        //Drives wheel
        leftTranslation = Math.max(-1, Math.min(magnitude, 1));
    }
    double diffRightPodHeading;
    double rightPodHeadingOffset;
    public void vectorRightPod (double magnitude, double direction) {
        //Direction changing code - find the closest way to get to the sought direction while 
        //maintaining wheel direction by flipping seek direction if needed
        diffRightPodHeading = direction - rightPodHeading + rightPodHeadingOffset;
        if (diffRightPodHeading > 180) {
            rightPodHeadingOffset -= 360;
        } else if (diffRightPodHeading < -180) {
            rightPodHeadingOffset += 360;
        }
        setRightPodHeading = direction - rightPodHeading + rightPodHeadingOffset;
        //Drives wheel
        rightTranslation = Math.max(-1, Math.min(magnitude, 1));
    }
    double directionOffset;
    public void driveBot (double direction, double magnitude, double botHeadingChange) {
        directionOffset = headingPID.PIDFOutPut(botHeadingChange, deltaTime);
        vectorLeftPod(magnitude - directionOffset, direction);
        vectorRightPod(magnitude + directionOffset, direction);
    }

    public void debugLeftWheel(double rotation, double translation) {
        ltm.setPower(rotation + translation);
        lbm.setPower(rotation - translation);
    }
    public void debugRightWHeel(double rotation, double translation) {
        rtm.setPower(rotation + translation);
        rbm.setPower(rotation - translation);
    }
    int leftHomingPoints;
    boolean pointsFound;
    double[] leftHomingValues;
    public void startLeftHomePoints (int numPoints) {
        if (numPoints >= 2) {
            leftHomingPoints = numPoints;
        }
        else {
            leftHomingPoints = 2;
        }
        leftHoming = true;
        leftHomingPointsReached = 0;
        pointsFound = false;
        leftHomingValues = new double[leftHomingPoints];
    }
    int leftHomingPointsReached;
    int leftHomingDirection = 1;
    public void swapLeftHomingDirection() {
        leftHomingDirection *= -1;
    }
    boolean hitEdgeLeftHome;
    boolean leftHomingDectedEdge;
    boolean lastLeftMagnetState;
    double lastEdgePosLeftHoming;
    final double leftHomingThreshold = 0.2;
    double diffEdgeHeading;
    int acceptedEdges;
    public void computeLeftHomingEdgeDetection() {
        leftPodHeading = (ltm.getCurrentPosition() + lbm.getCurrentPosition()) / 383.6;
        rightPodHeading = (rtm.getCurrentPosition() + rbm.getCurrentPosition()) / 383.6;
        hitEdgeLeftHome = false;
        leftHomingDectedEdge = false;
        if (lastLeftMagnetState != leftMagnetHome.getState()) {
            leftHomingDectedEdge = true;
        }
        if (leftHomingDectedEdge) {
            diffEdgeHeading = lastEdgePosLeftHoming - leftPodHeading;
            if (Math.abs(diffEdgeHeading) > leftHomingThreshold) {
                hitEdgeLeftHome = true;
                lastEdgePosLeftHoming = leftPodHeading;
                acceptedEdges ++;
            } else {
                leftHomingDectedEdge = false;
                edgeDetectionRejected++;
            }
        }

        lastLeftMagnetState = leftMagnetHome.getState();
    }
    public boolean leftHomingEdgeDetection() {
        return hitEdgeLeftHome;
    }
    public boolean leftEdgeDetected() {
        if (lastLeftMagnetState != leftMagnetHome.getState()) {
            leftHomingDectedEdge = true;
        }
        lastLeftMagnetState = leftMagnetHome.getState();
        return leftHomingDectedEdge;
    }
    double leftHomingPos;
    double[] firstLeftHomeValues;
    double[] lastLeftHomeValues;
    boolean leftGoToHomePos;

    public void updateLeftHomingPoints () {
        if (leftHomingPoints <= leftHomingPointsReached) {
            pointsFound = true;
        }
        if (!pointsFound) {
            if (leftHomingEdgeDetection()) {
                swapLeftHomingDirection();
                leftHomingValues[leftHomingPointsReached] = leftPodHeading;
                leftHomingPointsReached += 1;
                ltm.setPower(0);
                lbm.setPower(0);
            } else {
                ltm.setPower(leftHomePower * leftHomingDirection);
                lbm.setPower(leftHomePower * leftHomingDirection);
            }
        }
        if (pointsFound) {
            firstLeftHomeValues = new double[leftHomingPoints/2 + leftHomingPoints%2];
            lastLeftHomeValues = new double[leftHomingPoints/2];
            for(int i = 0; i < leftHomingPoints; i++) {
                if (i%2 == 0) { // if i is even
                    firstLeftHomeValues[i/2 ] = leftHomingValues[i];
                } else { // if i is odd
                    lastLeftHomeValues[i/2] = leftHomingValues[i];
                }
            }
            leftHoming = false;
            resetLeftEncoders();
        }
    }

    int edgeDetectionRejected;
}
