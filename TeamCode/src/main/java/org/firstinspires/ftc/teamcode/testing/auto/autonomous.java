package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.testing.chassis.driving;

public abstract class autonomous extends LinearOpMode{
    driving robot;
    int finalPos = -3;

    public void setup() throws InterruptedException {
        telemetry.addLine("setup running");
        telemetry.update();
        robot = new driving();
        robot.init(hardwareMap, true, true);
    }

    public void moveTicks(double pow, double angle, int ticks, int timeout) {
        robot.resetTicks();
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        double h = robot.imu.heading();

        while(ticks > robot.getTicks() && currentTime - startTime < timeout && opModeIsActive()) {
            robot.imu.update();
            robot.moveStraight(pow, angle, robot.imu.heading(), h);
            currentTime = System.currentTimeMillis();
        }
        robot.stop();
    }

    public void mineralLoc(int timeout) {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        while(timeout > currentTime - startTime && opModeIsActive() && finalPos == -3) {
            robot.web.position(this);
            finalPos = robot.web.getPos();
        }
    }

    public void deploy(int timeout) {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        robot.resetLiftTicks();
        robot.setHook(1);
        while(opModeIsActive() && timeout > currentTime - startTime && robot.getMaxLiftPos() < robot.getLiftTicks()) {
            currentTime = System.currentTimeMillis();
            telemetry.addLine("deploying");
            telemetry.update();
        }
        robot.setHook(0);
        rotate(Math.PI/2, 1, 5000);
        robot.stop();
    }

    public void hitMineral() {
        if(finalPos == -3 || finalPos == 0) {
            moveTicks(1, 0, 1000, 5000);
        } else {
            rotate(Math.PI/2 * finalPos, 1, 1000);
            moveTicks(1, 0, 1000, 5000);
        }
    }

    public void rotate(double degs, double pow, int timeout) {
        robot.imu.resetHeading();
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
//        double currentAngle = imu.heading();
//        double newPow = pow;
//
//        while(currentTime - startTime < timeout && opmode.opModeIsActive() && Math.abs(degs) > Math.abs(currentAngle)) {
//            drive(Range.clip(degs, -1, 1) * newPow,-Range.clip(degs, -1, 1) * newPow,
//                    Range.clip(degs, -1, 1) * newPow, -Range.clip(degs, -1, 1) * newPow);
//            newPow = map(Math.abs(degs) - Math.abs(currentAngle), 0, Math.abs(degs), .2, fpow);
//            imu.update();
//            currentAngle = imu.heading();
//            currentTime = System.currentTimeMillis();
//        }
        robot.imu.update();
        robot.move(0, 0, pow);
        telemetry.addLine("turning");
        telemetry.update();
        while(opModeIsActive() && Math.abs(robot.imu.heading()) < Math.abs(degs) && currentTime - startTime < timeout) {
            telemetry.addData("degs", degs);
            telemetry.addData("heading", robot.imu.heading());
            telemetry.update();
            currentTime = System.currentTimeMillis();
        }
        stop();
    }
}
