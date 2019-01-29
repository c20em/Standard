package org.firstinspires.ftc.teamcode.testing.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.testing.chassis.driving;

@TeleOp(group="Linear OpMode", name="drive")
public class testDrive extends LinearOpMode {
    driving robot = new driving();
    @Override
    public void runOpMode() {
        robot.init(hardwareMap, true, false);
        waitForStart();
        while(opModeIsActive()) {
            drive(gamepad1);
            robot.stop();
        }
    }

    public void drive(Gamepad gamepad) {
        double angle;
        double pow;
        double rot = 0;

        double x = gamepad.left_stick_x;
        double y = gamepad.left_stick_y;
        double ž = gamepad.right_stick_x;

        angle = Math.tan(y/x + Math.PI/4);

        pow = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        if(Math.abs(pow) < .3) {
            pow = 0;
        }
        if(Math.abs(angle) < .3) {
            angle = 0;
        }

        robot.move(pow, angle, rot);
    }
}
