package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CarlSwerveDemo extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {

        //Increment is what we'll use to adjust the speed
        int Increment = 0;

        //When you press up on the Dpad it will increase, as long as it is below 10 (Max is 10)
        if (gamepad1.dpad_up && Increment < 10) {

            Increment ++;
        }

        //When you press down on the Dpad it will decrease, as long as it above 0 (Lowest is 0) (If motors got negative, they spin backwards)
        if (gamepad1.dpad_down && Increment > 0) {

            Increment --;
        }

    }
}
