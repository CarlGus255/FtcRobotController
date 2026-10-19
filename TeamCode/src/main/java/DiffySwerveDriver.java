import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.teamcode.PIDFImplement;

public class DiffySwerveDriver {
    DcMotorEx ltm;
    DcMotorEx lbm;
    DcMotorEx rtm;
    DcMotorEx rbm;
    DigitalChannel leftMagnetHome;
    DigitalChannel rightMagnetHome;
    PIDFImplement leftPodRotatePID;
    PIDFImplement rightPodRotatePID;
    PIDFImplement leftPodHomingPID;
    PIDFImplement rightPodHomingPID;
    DiffySwervePod leftPod;
    DiffySwervePod rightPod;
    GoBildaPinpointDriver ppo;
    public double[] setArrayConstants(double v1, double v2, double v3, double v4, double v5, double v6) {
        double[] arrayConstants = new double[6];
        arrayConstants[0] = v1;
        arrayConstants[1] = v2;
        arrayConstants[2] = v3;
        arrayConstants[3] = v4;
        arrayConstants[4] = v5;
        arrayConstants[5] = v6;
        return arrayConstants;
    }
    double[] leftRotationPIDConstants;
    double[] rightRotationPIDConstants;
    double[] leftHomingPIDConstants;
    double[] rightHomingPIDConstants;
    public void init(HardwareMap hwMap) {
        leftPodRotatePID = new PIDFImplement();
        leftRotationPIDConstants = setArrayConstants(0.02, 0, 0.0003,0, 0, 0.005);
        rightPodRotatePID = new PIDFImplement();
        rightRotationPIDConstants = setArrayConstants(0.02, 0, 0.0003,0, 0.1, 0.005);
        leftPodHomingPID = new PIDFImplement();
        leftHomingPIDConstants = setArrayConstants(0.01, 0.1, 0, 0, 0.05, 0.01);
        rightPodHomingPID = new PIDFImplement();
        rightHomingPIDConstants = setArrayConstants(0.01, 0.1, 0, 0, 0.05, 0.01);

        ltm = hwMap.get(DcMotorEx.class, "ltm");
        lbm = hwMap.get(DcMotorEx.class, "lbm");
        rtm = hwMap.get(DcMotorEx.class, "rtm");
        rbm = hwMap.get(DcMotorEx.class, "rbm");

        leftMagnetHome = hwMap.get(DigitalChannel.class, "lss");
        rightMagnetHome = hwMap.get(DigitalChannel.class, "rss");

        ltm.setDirection(DcMotorSimple.Direction.FORWARD);
        lbm.setDirection(DcMotorSimple.Direction.FORWARD);
        rtm.setDirection(DcMotorSimple.Direction.REVERSE);
        rbm.setDirection(DcMotorSimple.Direction.REVERSE);

        ltm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rtm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftPod = new DiffySwervePod(ltm, lbm, leftMagnetHome, leftPodRotatePID, leftRotationPIDConstants, leftPodHomingPID, leftHomingPIDConstants);
        leftPod.resetEncoders();
        rightPod = new DiffySwervePod(rtm, rbm, rightMagnetHome, rightPodRotatePID, rightRotationPIDConstants, rightPodHomingPID, rightHomingPIDConstants);
        rightPod.resetEncoders();

        ppo = hwMap.get(GoBildaPinpointDriver.class, "ppo");
        ppo.resetPosAndIMU();
        ppo.setOffsets(182, -98, DistanceUnit.MM);
        robotPos = new double[3];

        leftPodOffsets = new double[2];
        leftPodOffsets[0] = -0.038;
        leftPodOffsets[1] = 0.167;
        rightPodOffsets = new double[2];
        rightPodOffsets[0] = -0.038;
        rightPodOffsets[1] = -0.167;

    }
    public void debugLeftWheel(double translation, double rotation) {
        leftPod.setTranslation(translation);
        leftPod.rotation = rotation;
    }
    double[] robotPos;
    public void updateSwerve(double deltaTime) {
        leftPod.updatePod(deltaTime);
        rightPod.updatePod(deltaTime);
        ppo.update();
        robotPos[0] = -ppo.getPosY(DistanceUnit.CM);
        robotPos[1] = -ppo.getPosX(DistanceUnit.CM);
        robotPos[2] = ppo.getHeading(AngleUnit.DEGREES);
    }
    public void vectorBotRough (double angle, double power, double rotation, double angleOffset) {
        leftPod.vectorPod(angle - angleOffset, power + rotation);
        rightPod.vectorPod(-angle + angleOffset, power - rotation);
    }
    double forwardVel;
    double strafeVel;
    double rotateVel;
    double lX;
    double lY;
    double[] leftPodOffsets; //(x,y) in m. X is facing forward, Y is facing laterally left.
    double rX;
    double rY;
    double[] rightPodOffsets;
    double leftPodTargetHeading;
    double rightPodTargetHeading;
    private static final double deadBand = 0.03;
    public void driveBot(double fV, double sV, double rV) {
        forwardVel = fV;
        strafeVel = sV;
        rotateVel = rV;


        lX = forwardVel - leftPodOffsets[1] * rotateVel;
        lY = strafeVel + leftPodOffsets[0] * rotateVel;

        rX = forwardVel - rightPodOffsets[1] * rotateVel;
        rY = strafeVel + rightPodOffsets[0] * rotateVel;

        if (
            Math.abs(lX )> deadBand ||
            Math.abs(lY) > deadBand
        ) {
            leftPodTargetHeading = Math.toDegrees(Math.atan2(lY, lX));
        }
        if (
            Math.abs(rX) > deadBand ||
            Math.abs(rY) > deadBand
        ) {
            rightPodTargetHeading = Math.toDegrees(Math.atan2(rY, rX));
        }

        leftPod.vectorPod(
                leftPodTargetHeading,
                Math.hypot(lY, lX)
        );
        rightPod.vectorPod(
                -rightPodTargetHeading,
                Math.hypot(rY, rX)
        );
    }
    double robotXV;
    double robotYV;
    public void driveFieldCentric(
            double fieldForward,
            double fieldStrafe,
            double rotation
    ) {
        // Pinpoint heading in degrees -> radians
        double heading = Math.toRadians(robotPos[2]);

        double cos = Math.cos(heading);
        double sin = Math.sin(heading);

        // Convert field-centric commands to robot-centric commands
        double robotForward =
                fieldForward * cos + fieldStrafe * sin;

        double robotStrafe =
                -fieldForward * sin + fieldStrafe * cos;

        // Use your existing, working inverse kinematics
        driveBot(robotForward, robotStrafe, rotation);
    }
}
