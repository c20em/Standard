package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous(name="Web", group="Autonomous")
@Disabled
public class WebAutoTesting extends baseAuto {
    @Override
    public void runOpMode() {
        dec(this.hardwareMap);
        initVuforia(this.hardwareMap);
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod(this.hardwareMap);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        waitForStart();

        if(opModeIsActive()) {
            tfodd();
        }
        while(opModeIsActive()) {
            String ugh = hello();
            if(!ugh.equals("nope")) {
                telemetry.addLine(ugh);
                if(ugh.equals("left")) {
                    forwards(1);
                } else if(ugh.equals("center")) {
                    strafeRight(1);
                } else if(ugh.equals("right")) {
                    forwards(-1);
                }
                sleep(2000);
                forwards(0);
                break;
            }
            telemetry.update();
        }
        while(opModeIsActive()) {
            telemetry.addLine("helloooo");
            telemetry.update();
        }
        if (!tfodNull()) {
            shutdownTfod();
        }

    }
}

