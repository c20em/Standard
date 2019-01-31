package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(group="Autonomous", name="square")
public class square extends autonomous{
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
        }
    }

    public void run() {
        moveTicks(.5, 0, 10000, 10000);
    }
}
