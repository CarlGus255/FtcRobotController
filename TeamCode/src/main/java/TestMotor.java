import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestMotor extends OpMode {
    MotorsBioBuzz motors;
    @Override
    public void init() {
        motors = new MotorsBioBuzz();
        motors.init(hardwareMap);
    }

    @Override
    public void loop() {
        motors.turret.setPower(gamepad1.left_stick_x);
    }
}
