import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class Eaglebytes extends OpMode {
    DcMotorEx front_left_motor;
    DcMotorEx front_right_motor;
    DcMotorEx back_left_motor;
    DcMotorEx back_right_motor;
    Limelight3A ll;
    double LYinput;
    double rotate;
    double LXinput;
    double speedMod = 1.0;
    double tx;
    double ty;
    double ta;
    boolean autoDrive = true;
    LLResult llResult;
    double scale = 0.3;

    @Override
    public void init() {
        front_left_motor = hardwareMap.get(DcMotorEx.class, "motor");
        front_right_motor = hardwareMap.get(DcMotorEx.class, "motor2");
        back_right_motor = hardwareMap.get(DcMotorEx.class, "motor3");
        back_left_motor = hardwareMap.get(DcMotorEx.class, "motor4");
        ll = hardwareMap.get(Limelight3A.class, "limeLight");
        ll.start();
        ll.pipelineSwitch(0);
    }


    @Override
    public void loop() {
        // gamepad1.right_stick_y - input from right joystick
        // gamepad1.left_stick_y - input from left joystick

        // gamepad1.dpadUpWasPressed() - True when dpad up was pressed once
        // gamepad1.dpadDownWasPressed() - True when dpad down was pressed once

        llResult = ll.getLatestResult();
        tx = llResult.getTx();
        ty = llResult.getTy();
        ta = llResult.getTa();
        if (gamepad1.xWasPressed()) {
            autoDrive = !autoDrive;
        }
        if (!autoDrive) {
            LYinput = (gamepad1.left_stick_y);
            LXinput = (gamepad1.left_stick_x);
            rotate = (gamepad1.right_stick_x);
        }

        if (autoDrive) {

            if (tx < -10) {
                rotate = -scale * Math.abs(tx) / 10;
            }
            if (tx > 10){
                rotate = scale * Math.abs(tx) / 10;
            }


            /*
            if (tx >= 5 || tx <= -5) {
                rotate = tx * scale;
            }
            */

        }
        if (gamepad1.dpadUpWasPressed()) {
            speedMod = speedMod + 0.1;
        }

        if (gamepad1.dpadDownWasPressed()) {
            speedMod = speedMod - 0.1;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            scale = scale + 0.01;
        }

        if (gamepad1.dpadRightWasPressed()) {
            scale = scale - 0.01;
        }

        front_left_motor.setPower((LYinput - LXinput - rotate) * speedMod);
        back_left_motor.setPower((LYinput + LXinput - rotate) * speedMod);
        front_right_motor.setPower(-(LYinput + LXinput + rotate) * speedMod);
        back_right_motor.setPower(-(LYinput - LXinput + rotate) * speedMod);

        telemetry.addData("scale", scale);


    }

}