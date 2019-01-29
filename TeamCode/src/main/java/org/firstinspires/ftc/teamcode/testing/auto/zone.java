package org.firstinspires.ftc.teamcode.testing.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(group="Autonomous", name="turntoangle")
public class zone extends autonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        setup();
        telemetry.addLine("waiting for start");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            run();
            break;
        }
    }

    public void run() {
        robot.rotate(Math.PI/2, 1, 100000, this);
    }
}
