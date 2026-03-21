package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CarlCoaxSwervePractice extends OpMode {

    //Defines translation (Wheel movement) and rotation (Wheel swivel) variables.
    double translation = 0;
    double rotation = 0;

    //Set in degrees to use properly with the proportional factor.
    double heading = 0;
    double seekHeading = 0;
    double proportionalHeading = 0;
    double rotationalAcceleration = 1;
    //For testing purposes with button
    boolean lastY = false;


    MotorsCarlCoaxSwervePractice servo = new MotorsCarlCoaxSwervePractice();

    @Override
    public void init() {
        servo.init(hardwareMap);
    }

    @Override
    public void loop() {
        //increases the seek heading by 90 degrees once for every time button is depressed (not looped)
        if (gamepad1.y && !lastY) {
            seekHeading = seekHeading + 90;
        }
        //tracks when the button has been depressed
        lastY = gamepad1.y;


        //tracks heading
        heading = servo.readRevEncoder()*360;
        //defines and tracks proportionalHeading
        proportionalHeading = seekHeading - heading;
        //implements proportionalHeading with rotation
        rotation = proportionalHeading*rotationalAcceleration/360;

        /*
        This is the coder from the differential swerve project, and is what transcends the discrete input into full rotation. Originally,
        it was meant to optimize rotation to find the shortest path, as if it was ever so this value was greater than 180, then it
        could more quickely just rotate the other way. This is also useful for the current swerve, but with potentiometer readings, it
        could do the exact same thing in a more generalized manner according to my understandings.

        essentially, it just equals the potentiometer readings (replace the revEncoder with a servo pot), until a big jump is detected,
        where it will either add or subtact a full rotation. This has worked really well with the differntial swerve, and I think it can
        be used for servos and the like in a more generalized sense, although limitations most certainly probably exist.


        //Seek the shortest path
        while (proportionalHeading > 180) {
            proportionalHeading -= 360;
        }
        while (proportionalHeading < -180) {
            proportionalHeading += 360;
        }
        */

        //powers the module
        servo.runMotor1(translation);
        servo.runServo1(proportionalHeading);
    }
}
