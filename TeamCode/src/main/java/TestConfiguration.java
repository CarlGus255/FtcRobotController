import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestConfiguration extends OpMode {
    MotorsBioBuzz motors;
    BioBuzzDiffySwerve swerve;
    @Override
    public void init() {
        swerve = new BioBuzzDiffySwerve(hardwareMap);

        motors = new MotorsBioBuzz();
        motors.init(hardwareMap);

    }
    int mode = 0;
    double motorPower;
    @Override
    public void loop() {
        if (gamepad1.dpadUpWasPressed()) {
            mode ++;
        }
        if (gamepad1.dpadDownWasPressed()) {
            mode --;
        }

        motorPower = gamepad1.left_stick_y;

        if (mode%8 == 0) {
            telemetry.addData("This is ltm", "");
            swerve.ltm.setPower(motorPower);
        }
        if (mode%8 == 1) {
            telemetry.addData("This is lbm", "");
            swerve.lbm.setPower(motorPower);
        }
        if (mode%8 == 2) {
            telemetry.addData("This is rtm", "");
            swerve.rtm.setPower(motorPower);
        }
        if (mode%8 == 3) {
            telemetry.addData("This is rbm", "");
            swerve.rbm.setPower(motorPower);
        }
        if (mode%8 == 4) {
            telemetry.addData("This is shooter", "");
            motors.shooter.setPower(motorPower);
        }
        if (mode%8 == 5) {
            telemetry.addData("This is turret", "");
            motors.turret.setPower(motorPower);
        }
        if (mode%8 == 6) {
            telemetry.addData("This is intake", "");
            motors.manualSpindex(motorPower);
        }
        if (mode%8 == 7) {
            telemetry.addData("This is index", "");
            motors.spindex.setPower(motorPower);
        }
    }
}
