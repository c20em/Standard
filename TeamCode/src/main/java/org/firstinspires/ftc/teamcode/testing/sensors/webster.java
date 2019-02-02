package org.firstinspires.ftc.teamcode.testing.sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class webster {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AalGs3r/////AAABmcqSqH1br0jamaDPd0c/OFgU2+NySZWZ/2AaDRU1dRzS+rwkYqqvoLvh3kS0UbU+q2aEaJavguV28TJc5oNXrVnYw25jaN3Nxyfe2pFQkSjhy9fzjqmIyXxGtLB/Ofm3/dVeW1vh5hXifwLfdGmzUZMWqmr2VChoZCZIqSIdnbAkC5o5Xbk2QF2/vWWKmhGJdB28pJaOgIElL1blHpVev1317F6MIyugjl95e8YA6WafETflVbX82WWzWSkLLrFTnbHd3rlLml+AXldog+4Mr0wYIZHseQ79qSY48SIUCHUPSCU75SDlSwaM4uuflocD1oaGz21m15KBno3GXsveL6Mpb5IlNKb3/aMwIKAIfQAa";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    int pos = -2;

    public webster(WebcamName web) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = web;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    public void initTfod(HardwareMap hwmap) {
        int tfodMonitorViewId = hwmap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwmap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void activateTfod() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    public boolean tfodNull() {
        if (tfod == null) return true;
        else return false;
    }

    public void shutdownTfod() { tfod.shutdown(); }

    public int position(LinearOpMode opMode) {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                opMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                            recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            opMode.telemetry.addData("Gold Mineral Position", "Left");
                            return -1;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            opMode.telemetry.addData("Gold Mineral Position", "Right");
                            return 1;
                        } else {
                            opMode.telemetry.addData("Gold Mineral Position", "Center");
                            return 0;
                        }
                    }
                } if(updatedRecognitions.size() == 2) {
                    int goldMineral = -1;
                    int silverMineral = -1;
                    int otherSilverMineral = -1;
                        for(Recognition recognition : updatedRecognitions) {
                            if(recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                if(recognition.getLeft() < recognition.getImageWidth()/3) {
                                    goldMineral = 0;
                                } else if(recognition.getLeft() < recognition.getImageWidth()*2/3) {
                                    goldMineral = 1;
                                } else {
                                    goldMineral = 2;
                                }
                            } else if(silverMineral == -1) {
                                if(recognition.getLeft() < recognition.getImageWidth()/3) {
                                    silverMineral = 0;
                                } else if(recognition.getLeft() < recognition.getImageWidth()*2/3) {
                                    silverMineral = 1;
                                } else {
                                    silverMineral = 2;
                                }
                            } else {
                                if(recognition.getLeft() < recognition.getImageWidth()/3) {
                                    otherSilverMineral = 0;
                                } else if(recognition.getLeft() < recognition.getImageWidth()*2/3) {
                                    otherSilverMineral = 1;
                                } else {
                                    otherSilverMineral = 2;
                                }
                            }
                        }

                        if(goldMineral == 0 && silverMineral == 1) {
                            return -1;
                        } else if(goldMineral == 1 && silverMineral == 0) {
                            return 0;
                        } else if(otherSilverMineral != -1) {
                            return 1;
                        } else if(goldMineral == 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                }
                opMode.telemetry.update();
            }
        }
        return -3;
    }
    public void checkPos(LinearOpMode opMode) { pos = position(opMode); }

    public int getPos() { return pos; }
}
