package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


// Single Line Comment
/*
Block Comment

A class is a collection of code that the computer can execute(?)
A method is a specific function type thing
Public means other thins can see it
Extends means that it includes other things that it imports
Imports import bits of code, such as a program to tell a servo to go to a certain value
Void means something

We need at least an init(), which is what it runs when we hit init on gamepad, and a loop(), which it loops through when we press run.
Telemetry is the values you can see on the driver hub (Think system.out.println())
@Teleop means its a teleop program
@Override means it overrides something (I don't remember)

Adding @Disabled - makes it not show up in program selections

TO ADD A MOTOR:
Inside the class, write "Private DCmotor [motor name};"
Private means it's only in that class
DCmotor should import something
Name the motor without any spaces, using captials, like LeftFrontMotor, for example
 */

@TeleOp(name="Test1")
public class Test1 extends OpMode {

    @Override
    public void init() {
        telemetry.addData("Hello", "Carl");
    }

    @Override
    public void loop() {
        telemetry.addData("Status", "Running");
        telemetry.update();
    }
}

/*
1. Change form "Hello World" to "Hello: Carl"

2. Run in autonomouns (@Autonomous instead of @Teleop)
 */
