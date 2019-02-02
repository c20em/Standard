package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="servoTest",group="Linear Opmode")
@Disabled
public class servoTest extends LinearOpMode {
    private Servo servo;

    @Override
    public void runOpMode() throws InterruptedException{
        servo = hardwareMap.get(Servo.class, "marker");
        servo.setPosition(.6);
        sleep(1000);
        servo.setPosition(.4);
        sleep(1000);
        servo.setPosition(.2);
        sleep(1000);
        servo.setPosition(.8);
        sleep(1000);
        servo.setPosition(1);

        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.a && servo.getPosition() < 1) {
                servo.setPosition(.9);
            } else if(gamepad1.b && servo.getPosition() > 0) {
                servo.setPosition(.4);
            } else if(gamepad1.x) {
                servo.setPosition(.2);
            } else if(gamepad1.y) {
                servo.setPosition(.75);
            }
            telemetry.addData("servo", servo.getPosition());

            telemetry.update();
        }
    }
}
