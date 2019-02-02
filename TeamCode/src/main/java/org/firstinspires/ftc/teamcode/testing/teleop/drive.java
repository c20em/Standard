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

    /**
     * moves a mecanum robot in four directions as well as rotation
     *
     * based on the controls of the gamepad : left stick controls strafing/driving forwards, right
     * stick controls turning
     *
     * opts for a direction based on previous motion in addition to the sticks - will prioritize
     * an action if it is already being carried out
     */
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
            Strafe(1);
        } else if (strafePow > .4 && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_LEFT;
            Strafe(-1);
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

    // strafes mecanum chassis, positive forwards, negative back
    public void Strafe(int strafedirection) {
        double flbr = strafedirection;
        double frbl = -strafedirection;
        double BRpower = strafedirection;
        double BLpower = -strafedirection;

        robot.drive(flbr, frbl, frbl, flbr);
    }

    // drives mecanum chassis forwards/back, positive forwards, negative back
    public void Drive(double pow) {
        robot.drive(pow);
    }

    public enum controllerPos {
        STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
    }

    // turns mecanum chassis, positive right, negative left
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
            robot.setNom(-1);
        } else if (gamepad2.left_bumper) {
            robot.setNom(1);
        } else {
            robot.setNom(0);
        }

        if(gamepad2.right_stick_y > .3 && robot.getSpoolPos() > robot.getMaxSpoolPos()) {
            robot.setExtend(-.8);
        } else if(gamepad2.right_stick_y < -.3 && robot.getSpoolPos() < 0) {
            robot.setExtend(.8);
        } else {
            robot.setExtend(0);
        }

        if(gamepad2.a) {
            if(robot.getMiddleSpool() < robot.getSpoolPos()) {
                robot.setExtend(-.8);
            } else if(robot.getMiddleSpool() > robot.getSpoolPos()) {
                robot.setExtend(.8);
            }
        } else if(gamepad2.b) {
            if(robot.getMaxSpoolPos() < robot.getSpoolPos()) {
                robot.setExtend(-.8);
            }
        }
    }

    public void hook() {
        if(gamepad2.left_stick_y < -.3) {
            setHook(1);
        } else if(gamepad2.left_stick_y > .3) {
            setHook(-1);
        } else {
            setHook(0);
        }
    }

    /* sets the direction of the hook, based off of encoder values does not allow driver to move
        hook beyond the initial height or below the minimum height
     */
    public void setHook(double pow) {
        if((robot.getLiftTicks() > -robot.getMaxLiftPos() && pow < 0) || (robot.getLiftTicks() < 0 && pow > 0)) {
            robot.setHook(0);
        } else { robot.setHook(pow); }
    }

}
