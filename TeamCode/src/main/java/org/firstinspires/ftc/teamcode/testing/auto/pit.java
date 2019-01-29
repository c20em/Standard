package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(group="Autonomous", name="pit")
public class pit extends autonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        setup();
        telemetry.addLine("waiting for start");
        telemetry.update();
        waitForStart();
        if(opModeIsActive()) {
            robot.web.initTfod(hardwareMap);
        }
        while(opModeIsActive()) {
            run();
        }
    }

    public void run() {
        telemetry.addLine("running");
        telemetry.update();
        mineralLoc(5000);
        telemetry.addLine(getFinalPos());
        telemetry.update();
        deploy(6000);

    }
}
