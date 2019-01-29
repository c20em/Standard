package org.firstinspires.ftc.teamcode.testing.sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Gyro {
    BNO055IMU imu;
    Orientation angles;
    double forwardHeading = 0.0;

    public Gyro(BNO055IMU imu) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);
        this.imu = imu;
    }

    public double heading() {
        update();
        if(angles != null) {
            double h = angles.firstAngle - forwardHeading;
            if (h < -180) h += 360;
            if (h > 180) h -= 360;
            return h;
        } else {
            return -366;
        }
    }

    public void update() {
        angles = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ);
    }

    public void resetHeading() {
        if(angles != null)
            forwardHeading = angles.firstAngle;
    }

}
