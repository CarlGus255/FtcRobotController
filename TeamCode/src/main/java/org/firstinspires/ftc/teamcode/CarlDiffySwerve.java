package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="CarlDiffySwerve", group="Test")
public class CarlDiffySwerve extends OpMode {

/*
Plan ~ish

1. Motor power control from translation and rotation variables
2. Use left joystick for translation value and right for rotation value
3. Use motor encoders to get current wheel heading
4. Use a touch center to find home
5. Have a button(like x) to rotate the swerve until home then set the current heading to 0 degrees (Reset encoder counts?)

at that point, we're able to control by pod. That should probably be the extent of this class, where I can change the name to include
the pod specification and put it in a diffy swerve group. The next step is robot centric:

1. Add a magnitude and a desired direction variable
2. Translation for now is equal to magnitude, but they should be seperated eventually
3. Add a "rotation acceleration" variable??
4. Use the desired direction variable to change wheel heading by taking the difference between desired direction and current wheel
heading, multiplying it by the rotation acceleration variable, and inputing that as the "Rotation value" in the motor power. This allows
us to tune how fast it trys to rotate while still seeking a desired direction
5. Use gamepad left joystick x and y to get magnitude and direction values using simple trig. First, need to flip polarity of Y value,
then use simple trig to get magnitude (A^2 + B^2 = C^2) and ArcTan to get direction

Now, we get the wheel trying to point in the direction of the stick and go forwards depending on how far forwards it is pressed. This
would be complete for a single pod, but for double pod testing, we need some robot rotation(yaw) changes. This requires two pods in a
chassis

6. Add a variable called YawChange
7. Have the translation values for each pod be seperate, and be seperate from the magnitude value
8. Add a variable called YawAcceleration
9. Have the translation value for one pod be magnitude + (YawChange * YawAcceleration) and the other be magnitude - (YawChange *
YawAcceleration).
10. Assign the YawChange variable to the right joystick Y
11. Tune the YawAcceleration to a normal seeming experience

Then, we should have a good robot-centric drive. Next would be converting that to a field centric drive. Again, this should be a new
class, so we'd have one Pod class, one robot centric class, and one field centric class.

In the field centric class, we need to convert joystick movement to field movement, then to robot movement. This is a big leap, as we
need to know the robot's relation to the field. I'm not entirely sure how odementry works, but this is where it starts getting
implemented.

1. Create new variable called FieldDirection
2. Link the left joystick to FieldDirection instead of directly to the desired direction
3. Use robot yaw relative to the field in tandem with the wheel heading to gather overall wheel direction.
4. Use a similar implementation to get a change in wheel heading based on the difference between overall wheel direction and the
FieldDirection. Essentially, the wheels should be pointed in the dirrection of the controller relative to the field, so we just need
to track the robot yaw and incorporate that.

Now, we have a completed teleop, with yaw tracking and the ability to move field-centric, albeit with a lack of prominent odometry.

Next/last class will be an auto. For now, just plan on using camera. It could also be purely relative to field starting, but using the
camera for closed loop control is better.

1. Establish distance and yaw from the camera to the april tag
2. Create RobotX, RobotY, and RobotRotation variables
3. Convert form root distance and yaw based on april tag to coordinates on the field (X,Y), as well as rotation (RobotRotation). Will
have to use polar coordinates and such, but should be doable.
4. Create GoalX,GoalY, and GoalRotation (Or orientation) variables
5. When a certain button is pressed (Y for example), have the robot run to those coordinates. Use the existing TeleOp drive. Instead of
using gamepad for input, do some trig/vector math to find the wheel heading and translation change to arrive at the desired X and Y
coordinates and use the rotation I/O function to achieve the desired rotation
6. Include deadwheels to track movement change in X/Y. Similar to wheel heading, we can have a button to home the robot to the april tag
once, then just run from there and reset the values with april tag data every time we hit that button, similar to homing the pod.
 */
    @Override
    public void init() {
        double WheelHeading = 90;
        double TopMotorSpeed = 100.2;
        double BottomMotorSpeed = 0;

        telemetry.addData("WheelHeading", WheelHeading);
        telemetry.addData("TopMotorSpeed", TopMotorSpeed);
        telemetry.addData("BottomMotorSpeed", BottomMotorSpeed);
    }

    @Override
    public void loop() {

    }
}





