package org.firstinspires.ftc.teamcode.testing.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.ImprovedMechTest;
import org.firstinspires.ftc.teamcode.testing.chassis.driving;

@TeleOp(group="Linear OpMode", name="drive")
public class drive extends LinearOpMode {
    driving robot;
    controllerPos previousDrive = controllerPos.ZERO;

    @Override
    public void runOpMode() {
        robot = new driving();
        robot.init(hardwareMap, true, false);
        waitForStart();

        while(opModeIsActive()) {
            moveRobot();
            intake();
            hook();

            telemetry.update();
            idle();
        }
    }


    public void moveRobot() {
        double drivePow = -gamepad1.left_stick_y;
        double strafePow = gamepad1.left_stick_x;
        double turnPow = gamepad1.right_stick_x;

        if (drivePow > 0.1 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_FOWARD;
            robot.drive(drivePow);
        } else if (drivePow < -0.1 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_BACK;
            Drive(drivePow);
        } else if (strafePow < -.4 && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_RIGHT;
            Strafe(-1);
        } else if (strafePow > .4 && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_LEFT;
            Strafe(1);
        } else if (turnPow > 0.25 && (previousDrive == controllerPos.TURN_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.TURN_RIGHT;
            turn(turnPow);
        } else if (turnPow < -0.25 && (previousDrive == controllerPos.TURN_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.TURN_LEFT;
            turn(turnPow);
        } else {
            previousDrive = controllerPos.ZERO;
            robot.stop();
        }


    }
    public void Strafe(int strafedirection) {
        double flbr = -strafedirection;
        double frbl = strafedirection;
        double BRpower = -strafedirection;
        double BLpower = strafedirection;

        robot.drive(flbr, frbl, frbl, flbr);
    }

    public void Drive(double pow) {
        robot.drive(pow);
    }

    public enum controllerPos {
        STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
    }

    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }

    public void turn(double turn) {
        robot.drive(turn, -turn, turn, -turn);
    }

    public void intake() {
        if(gamepad2.dpad_up) {
            robot.setPivot(1);
        } else if(gamepad2.dpad_down) {
            robot.setPivot(-.6);
        } else {
            robot.setPivot(0);
        }

        if (gamepad2.right_bumper) {
            robot.setNom(.9);
        } else if (gamepad2.left_bumper) {
            robot.setNom(-.9);
        } else {
            robot.setNom(0);
        }

        if(gamepad2.right_stick_y > .3) {
            robot.setExtend(-.8);
        } else if(gamepad2.right_stick_y < -.3) {
            robot.setExtend(.8);
        } else {
            robot.setExtend(0);
        }
    }

    public void hook() {
        if(gamepad2.left_stick_y < -.3) {
            robot.setHook(1);
        } else if(gamepad2.left_stick_y > .3) {
            robot.setHook(-1);
        } else {
            robot.setHook(0);
        }
    }
}
