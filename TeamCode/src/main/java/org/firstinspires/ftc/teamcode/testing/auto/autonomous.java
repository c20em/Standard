package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.testing.chassis.driving;

public abstract class autonomous extends LinearOpMode{
    driving robot;
    String finalPos = "nope";

    public void setup() throws InterruptedException {
        telemetry.addLine("setup running");
        telemetry.update();
        robot = new driving();
        robot.init(hardwareMap, true, false);
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
        while(timeout < currentTime - startTime && opModeIsActive() && finalPos.equals("nope")) {
            robot.web.position(this);
            finalPos = robot.web.getPos();
        }
    }

    public String getFinalPos() { return finalPos; }

    public void deploy(int timeout) {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        while(opModeIsActive() && timeout < currentTime - startTime && robot.getMaxLiftPos() < robot.getLiftTicks()) {
            robot.setHook(1);
        }
        robot.setHook(0);
        robot.move(0, 0, Math.PI/4);
        robot.stop();
    }
}
