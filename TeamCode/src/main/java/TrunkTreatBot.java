import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class TrunkTreatBot extends OpMode {
    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotorEx.class, "lfm");
        rightMotor = hardwareMap.get(DcMotorEx.class, "rfm");
    }
    double forward;
    double rotate;
    double forwardDirection = 1;
    double rotateDirection = 1;
    @Override
    public void loop() {
        rotate = -gamepad1.left_stick_y * forwardDirection;
        forward = gamepad1.right_stick_x * rotateDirection;

        if (gamepad1.xWasPressed()) {
            rotateDirection *= -1;
        }
        if (gamepad1.yWasPressed()) {
            forwardDirection *= -1;
        }
        telemetry.addData("Left motor power is", forward + rotate);
        telemetry.addData("Right motor power is:", forward - rotate);
        telemetry.addData("Forward Direction is", forwardDirection);
        telemetry.addData("Rotate direction is", rotateDirection);

        leftMotor.setPower(forward + rotate);
        rightMotor.setPower(forward - rotate);
    }
}
