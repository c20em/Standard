package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(group="Autonomous", name="zone")
public class zone extends autonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        setup();
        telemetry.addLine("waiting for start");
        telemetry.update();
        waitForStart();
        if(opModeIsActive()) {
            robot.web.activateTfod();
        }
        while(opModeIsActive()) {
            run();
            break;
        }
    }

    public void run() {
//        deploy(6500);
//        robot.drive(-1);
//        sleep(1000);
//        robot.stop();
//        robot.setPivot(-.4);
//        sleep(1000);
//        robot.setPivot(0);
//        robot.setExtend(.5);
//        sleep(2000);
//        robot.setExtend(0);
//        stop();
        mineralLoc(5000);
        deploy(6500);
        rotate(-Math.PI/2, .5, 5000);
        telemetry.addData("loc", robot.web.getPos());
        telemetry.update();
        hitMineral();
        robot.setPivot(-.4);
        sleep(1000);
        robot.setPivot(0);
        robot.setExtend(.5);
        sleep(2000);
        robot.setExtend(0);
        stop();
    }
}
