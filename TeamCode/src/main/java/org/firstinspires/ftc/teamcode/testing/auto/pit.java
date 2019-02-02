package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;

@Autonomous(group="Autonomous", name="pit")
public class pit extends autonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        setup();
        telemetry.addLine("waiting for start");
        telemetry.update();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            robot.web.initTfod(hardwareMap);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        waitForStart();
        if(opModeIsActive()) {
            robot.web.activateTfod();
        }
        while(opModeIsActive()) {
            run();
        }
    }

    public void run() {
        telemetry.addLine("running");
        telemetry.update();
        mineralLoc(5000);
        telemetry.addData("mineral pos", robot.web.getPos());
        telemetry.update();
        deploy( 6000);
        hitMineral();

    }
}
