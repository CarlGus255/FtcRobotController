import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
        leftRotationPIDConstants = setArrayConstants(0.02, 0, 0.0003,0, 0, 0.0005);
        rightPodRotatePID = new PIDFImplement();
        rightRotationPIDConstants = setArrayConstants(0.5, 0, 0.01,0, 0.1, 0.05);
        leftPodHomingPID = new PIDFImplement();
        leftHomingPIDConstants = setArrayConstants(0.01, 0.1, 0, 0, 0.05, 0.01);
        rightPodHomingPID = new PIDFImplement();
        rightHomingPIDConstants = setArrayConstants(0.2, 0.1, 0, 0, 0.3, 0.05);

        ltm = hwMap.get(DcMotorEx.class, "ltm");
        lbm = hwMap.get(DcMotorEx.class, "lbm");
        rtm = hwMap.get(DcMotorEx.class, "rtm");
        rbm = hwMap.get(DcMotorEx.class, "rbm");

        leftMagnetHome = hwMap.get(DigitalChannel.class, "lss");
        rightMagnetHome = hwMap.get(DigitalChannel.class, "rss");

        ltm.setDirection(DcMotorSimple.Direction.FORWARD);
        lbm.setDirection(DcMotorSimple.Direction.FORWARD);
        rtm.setDirection(DcMotorSimple.Direction.FORWARD);
        rbm.setDirection(DcMotorSimple.Direction.FORWARD);

        ltm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rtm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftPod = new DiffySwervePod(ltm, lbm, leftMagnetHome, leftPodRotatePID, leftRotationPIDConstants, leftPodHomingPID, leftHomingPIDConstants);
        leftPod.resetEncoders();
    }
    public void debugLeftWheel(double translation, double rotation) {
        leftPod.setTranslation(translation);
        leftPod.rotation = rotation;
    }
}
