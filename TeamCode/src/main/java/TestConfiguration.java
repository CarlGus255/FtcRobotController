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
            telemetry.addData("Encoder is", swerve.ltm.getCurrentPosition());
            swerve.ltm.setPower(motorPower);
        }
        if (mode%8 == 1) {
            telemetry.addData("This is lbm", "");
            telemetry.addData("Encoder is", swerve.lbm.getCurrentPosition());
            swerve.lbm.setPower(motorPower);
        }
        if (mode%8 == 2) {
            telemetry.addData("This is rtm", "");
            telemetry.addData("Encoder is", swerve.rtm.getCurrentPosition());
            swerve.rtm.setPower(motorPower);
        }
        if (mode%8 == 3) {
            telemetry.addData("This is rbm", "");
            telemetry.addData("Encoder is", swerve.rbm.getCurrentPosition());
            swerve.rbm.setPower(motorPower);
        }
        if (mode%8 == 4) {
            telemetry.addData("This is shooter", "");
            telemetry.addData("Encoder is", motors.shooter.getCurrentPosition());
            motors.shooter.setPower(motorPower);
        }
        if (mode%8 == 5) {
            telemetry.addData("This is turret", "");
            telemetry.addData("Encoder is", motors.turret.getCurrentPosition());
            motors.turret.setPower(motorPower);
        }
        if (mode%8 == 6) {
            telemetry.addData("This is intake", "");
            telemetry.addData("Encoder is", motors.intake.getCurrentPosition());
            motors.intake.setPower(motorPower);
        }
        if (mode%8 == 7) {
            telemetry.addData("This is index", "");
            telemetry.addData("Encoder is", motors.spindex.getCurrentPosition());
            motors.runSpindex(motorPower);
        }
    }
}
