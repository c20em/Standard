package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.testing.chassis.driving;

public abstract class autonomous extends LinearOpMode{
    driving robot;

    /**
     * initializes robot
     * @throws InterruptedException
     */
    public void setup() throws InterruptedException {
        telemetry.addLine("setup running");
        telemetry.update();
        robot = new driving();
        robot.init(hardwareMap, true, true);
        robot.markerIn();
        telemetry.addLine("setu up done");
        telemetry.update();
    }

    /**
     * uses encoder to move ticks forwards or backwards relative to initial heading
     * @param pow
     * @param angle to move in relation to forwards direction
     * @param ticks number of ticks (related to rotations of the motor) to move
     * @param timeout
     */
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

    /**
     * uses webcam object in order to attempt to detect the minerals, until timeout or mineral
     * detected
     * @param timeout
     */
    public void mineralLoc(int timeout) {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        while(timeout > currentTime - startTime && opModeIsActive() && robot.web.getPos() == -3) {
            robot.web.checkPos(this);
            currentTime = System.currentTimeMillis();
        }
    }

    /**
     * unlatches for a certain number of ticks, or until time expires
     * @param timeout
     */
    public void deploy(int timeout) {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        robot.resetLiftTicks();
        robot.setHook(1);
        while(opModeIsActive() && timeout > currentTime - startTime && robot.getMaxLiftPos() > robot.getLiftTicks()) {
            currentTime = System.currentTimeMillis();
            telemetry.addData("distanceFromEnd", robot.getLiftTicks() - robot.getMaxLiftPos());
            telemetry.addLine("deploying");
            telemetry.update();
        }
        robot.setHook(0);
//        rotate(-Math.PI/2, 1, 5000);
    }

    /**
     * samples mineral depending on predetermined location, rotates then moves if on the left or
     * right, moves forwards if in the center or unknown, right now is specific for zone not crater
     */
    public void hitMineral() {
        if(robot.web.getPos() == -3 || robot.web.getPos() == 0) {
            moveTicks(.5, Math.PI, 700, 2500);
            dropMarker();
            rotate(Math.PI/4, .5, 1000);
            moveTicks(1, -Math.PI/2, 700, 1000);
            rotate(-Math.PI/3, .5, 1000);
            moveTicks(1, 0, 10000, 4000);

        } else {
            rotate(Math.PI/6 * robot.web.getPos(), .5, 1000);
            moveTicks(1, Math.PI, 500, 1000);
            rotate(Math.PI/4 * -robot.web.getPos(), .5, 1000);
            moveTicks(1, Math.PI, 500, 1000);

            //untested ish
            rotate(Math.PI/4+Math.PI/8*robot.web.getPos(), .5, 1000);
            dropMarker();
            sleep(500);
            moveTicks(1, -Math.PI/2, 500, 1000);
            moveTicks(1, -Math.PI, 100, 300);
            rotate(-Math.PI/4, .5, 1000);
            moveTicks(1, 0, 1000, 2000);
            //untested ish
//            rotate(Math.PI, .5, 2000);
//            dropMarker();
//            sleep(500);
//            moveTicks(1, -Math.PI/2, 500, 1000);
        }
    }

    /**
     * rotates a certain amount of radians depending on the current angle, if the time exceeds the
     * maximum amount of time or op mode is stopped the robot stops moving
     * @param rads radians to turn
     * @param pow
     * @param timeout milliseconds before exiting unless angle reached before then
     */
    public void rotate(double rads, double pow, int timeout) {
        //rads = rads/2; //idk why figure this out eventually
        robot.imu.resetHeading();
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        robot.imu.update();
        if(rads < 0) {
            robot.move(0,  0, -pow);
        } else {
            robot.move(0, 0, pow);
        }
        telemetry.addLine("turning");
        telemetry.update();
        while(opModeIsActive() && Math.abs(robot.imu.heading()) < Math.abs(rads) && currentTime - startTime < timeout) {
            telemetry.addData("rads", rads);
            telemetry.addData("heading", robot.imu.heading());
            telemetry.update();
            currentTime = System.currentTimeMillis();
        }
        robot.stop();
    }

    public void dropMarker() {
        robot.markerOut();
    }
}
