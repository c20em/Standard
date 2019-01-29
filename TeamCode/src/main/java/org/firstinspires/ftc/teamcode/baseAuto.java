package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

abstract class baseAuto extends LinearOpMode {
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;

    DcMotor hook = null;
    DcMotor nom = null;
    DcMotor extend = null;
    DcMotor pivot = null;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AalGs3r/////AAABmcqSqH1br0jamaDPd0c/OFgU2+NySZWZ/2AaDRU1dRzS+rwkYqqvoLvh3kS0UbU+q2aEaJavguV28TJc5oNXrVnYw25jaN3Nxyfe2pFQkSjhy9fzjqmIyXxGtLB/Ofm3/dVeW1vh5hXifwLfdGmzUZMWqmr2VChoZCZIqSIdnbAkC5o5Xbk2QF2/vWWKmhGJdB28pJaOgIElL1blHpVev1317F6MIyugjl95e8YA6WafETflVbX82WWzWSkLLrFTnbHd3rlLml+AXldog+4Mr0wYIZHseQ79qSY48SIUCHUPSCU75SDlSwaM4uuflocD1oaGz21m15KBno3GXsveL6Mpb5IlNKb3/aMwIKAIfQAa";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    BNO055IMU imu;
    GradientDrawable.Orientation angles;
    public double start;
    public static long timeDown = 6000;

    public void dec(HardwareMap hwmap) {
        frontLeft = hwmap.get(DcMotor.class, "fl");
        frontRight = hwmap.get(DcMotor.class, "fr");
        backLeft = hwmap.get(DcMotor.class, "bl");
        backRight = hwmap.get(DcMotor.class, "br");

        hook = hwmap.get(DcMotor.class, "hook");
        nom = hwmap.get(DcMotor.class, "nom");
        extend = hwmap.get(DcMotor.class, "extend");
        pivot = hwmap.get(DcMotor.class, "pivot");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        hook.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void gyro() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    double currentAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
    }

    public void startAngle() {
        start = currentAngle();
    }

    public void forwards(double drivePower) {
        frontLeft.setPower(drivePower);
        frontRight.setPower(drivePower);

        backLeft.setPower(-drivePower);
        backRight.setPower(-drivePower);
    }

    public void counter(double drivePower) {
        frontLeft.setPower(drivePower);
        frontRight.setPower(-drivePower);

        backLeft.setPower(drivePower);
        backRight.setPower(-drivePower);
    }

    public void clock(double drivePower) {
        frontLeft.setPower(-drivePower);
        frontRight.setPower(drivePower);

        backLeft.setPower(-drivePower);
        backRight.setPower(drivePower);
    }

    public void pullUp(double power) {
        hook.setPower(power);
    }

    public void pullDown(double power) {
        hook.setPower(power);
    }

    public void nomIn(double power) {
        nom.setPower(power);
    }

    public void nomOut(double power) {
        nom.setPower(power);
    }

    public void pivOut(double power) {
        pivot.setPower(-power);
    }

    public void pivIn(double power) {
        pivot.setPower(power);
    }

    public void extend(double power) {
        extend.setPower(power);
    }

    public void retract(double power) {
        extend.setPower(-power);
    }

    public void strafeLeft(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);

        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void strafeRight(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);

        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public void downSeq() {
        pullUp(1);
        sleep(timeDown);
        pullUp(0);
        counter(.7);
        sleep(500);
        counter(0);
    }

    public void initVuforia(HardwareMap hwmap) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hwmap.get(WebcamName.class, "webbie");

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

    public void tfodd() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    public boolean tfodNull() {
        if (tfod == null) return true;
        else return false;
    }

    public void shutdownTfod() { tfod.shutdown(); }


    public String hello() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            return("left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            return("right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            return("center");
                        }
                    }
                }
                telemetry.update();
            }
        }
        return "nope";
    }

}