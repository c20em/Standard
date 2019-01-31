package org.firstinspires.ftc.teamcode.testing.chassis;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.testing.sensors.Gyro;
import org.firstinspires.ftc.teamcode.testing.sensors.webster;


public class driving {
    private int encoder = 0;
    private int maxLiftPos = 8000; // add value! equivalent to 6 secs
    private int liftTicks = 0;
    private static final int maxArmPos = 0;
    private static final int maxSpoolPos = 0;


    private DcMotor fl;
    private DcMotor bl;
    private DcMotor fr;
    private DcMotor br;
    private DcMotor hook;
    private DcMotor nom;
    private DcMotor extend;
    private DcMotor pivot;

    private Servo marker;

    private LinearOpMode opmode;

    public Gyro imu;
    public webster web;

    public void init(HardwareMap hwmap, boolean initgyro, boolean initvision) {
        fl = hwmap.get(DcMotor.class, "fl");
        fr = hwmap.get(DcMotor.class, "fr");
        bl = hwmap.get(DcMotor.class, "bl");
        br = hwmap.get(DcMotor.class, "br");

        hook = hwmap.get(DcMotor.class, "hook");
        nom = hwmap.get(DcMotor.class, "nom");
        extend = hwmap.get(DcMotor.class, "extend");
        pivot = hwmap.get(DcMotor.class, "pivot");

        //marker = hwmap.get(Servo.class, "marker");

        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.FORWARD);
        hook.setDirection(DcMotor.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        if(initgyro) {
            imu = new Gyro(hwmap.get(BNO055IMU.class, "imu"));
        }
        if(initvision) {
            web = new webster(hwmap.get(WebcamName.class, "webbie"));
            web.initTfod(hwmap);
        }
    }

    public void drive(double FL, double FR, double BL, double BR) {
        fl.setPower(FL);
        fr.setPower(FR);
        bl.setPower(BL);
        br.setPower(BR);
    }

    public void move(double pow, double angle, double rot) {
        pow = Range.clip(pow, -1, 1);
        rot = Range.clip(rot, -1, 1);

        double vx = pow*Math.cos(angle+Math.PI/4);
        double vy = pow*Math.sin(angle+Math.PI/4);

        double[] V = {vx + rot, vy - rot, vy + rot, vx - rot};

        double m = 0.0;
        for(double v : V)
            if(Math.abs(v) > m)
                m = v;

        double mult = Math.max(Math.abs(pow), Math.abs(rot));

        if(m != 0)
            for(int i = 0; i < V.length; i++)
                V[i] = Math.abs(mult) * V[i] / Math.abs(m);

        fl.setPower(Range.clip(V[0], -1, 1));
        fr.setPower(Range.clip(V[1], -1, 1));
        bl.setPower(Range.clip(V[2], -1, 1));
        br.setPower(Range.clip(V[3], -1, 1));
    }

    public void moveStraight(double pow, double angle, double actual, double target) {
        double error = actual - target;

        double P = error*getKp();

        this.move(pow, angle, Range.clip(P, -1, 1));
    }

    public void stop() { drive(0,0,0,0); }

    public void resetTicks() {
        encoder = fl.getCurrentPosition();
    }

    private double getKp() {
        return -0.02;
    }

    public int getTicks() {
        return fl.getCurrentPosition() - encoder;
    }

    public void resetLiftTicks() {
        liftTicks = hook.getCurrentPosition();
    }

    public int getLiftTicks() {
        return hook.getCurrentPosition() - liftTicks;
    }

    public int getMaxLiftPos() { return maxLiftPos; }

    public void setHook(int pow) { hook.setPower(pow); }

    public double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
