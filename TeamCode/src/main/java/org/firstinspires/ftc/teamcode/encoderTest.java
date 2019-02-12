package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(group="Linear OpMode", name="encodertest")
public class encoderTest extends LinearOpMode {
    private DcMotor motor;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "hook");
        motor.setDirection(DcMotor.Direction.REVERSE);
        int ticks = motor.getCurrentPosition();

        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.left_bumper) {
                motor.setPower(1);
            } else if(gamepad1.right_bumper) {
                motor.setPower(-1);
            } else {
                motor.setPower(0);

            }
            telemetry.addData("encoder", motor.getCurrentPosition() - ticks);
            telemetry.update();
            idle();
        }
    }
}
