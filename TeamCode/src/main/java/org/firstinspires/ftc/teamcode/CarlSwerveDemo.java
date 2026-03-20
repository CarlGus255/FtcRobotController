package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="CarlSwerveDemo")
public class CarlSwerveDemo extends OpMode {

    MotorsSwerveDemo motors = new MotorsSwerveDemo();

    @Override
    public void init() {
        motors.init(hardwareMap);
    }
    //Increment is what we'll use to adjust the speed
    int Increment = 0;
    boolean LastDPadUp = false;
    boolean LastDPadDown = false;

    @Override
    public void loop() {



        //When you press up on the Dpad it will increase, as long as it is below 10 (Max is 10)
        if (gamepad1.dpad_up && !LastDPadUp && Increment < 10) {

            Increment ++;
        }

        //When you press down on the Dpad it will decrease, as long as it above 0 (Lowest is 0) (If motors got negative, they spin backwards)
        if (gamepad1.dpad_down && !LastDPadDown && Increment > 0) {

            Increment --;
        }

        LastDPadUp = gamepad1.dpad_up;
        LastDPadDown = gamepad1.dpad_down;


        motors.SetSwerveSpeed(0.1 * Increment);
        motors.SetMecanumSpeed(0.1 * Increment);
    }
}
