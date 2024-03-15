package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

public class MoveToPoint {
    private final double TICKS_PER_REV = 8192;
    private final double WHEEL_CIRCUMFERENCE = 2* Math.PI;
    private final double TICKS_PER_INCH = TICKS_PER_REV/WHEEL_CIRCUMFERENCE;
    private final double ACCEL_GAIN = 0.001;

    LinearOpMode linearOpMode;

    DcMotor motor1, motor2, motor3, motor4, parallel, perp;
    IMU imu;

    public MoveToPoint(LinearOpMode l_op){
        linearOpMode = l_op;
        motor1 = linearOpMode.hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = linearOpMode.hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = linearOpMode.hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = linearOpMode.hardwareMap.get(DcMotor.class , "backRight");
        parallel = linearOpMode.hardwareMap.get(DcMotor.class, "rightRig");
        perp = linearOpMode.hardwareMap.get(DcMotor.class, "leftRig");
        imu = linearOpMode.hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        linearOpMode.telemetry.addData("Ready!","");
        linearOpMode.telemetry.update();
    }
    public void moveWithOutIMU(double x, double y){

        parallel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        perp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        parallel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        perp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int yError = (int)(y*TICKS_PER_INCH);
        int xError = (int)(x*TICKS_PER_INCH);

        perp.setTargetPosition(xError);
        parallel.setTargetPosition(yError);

        parallel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        perp.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
        double motor1Power = (y + x) / denominator;  //motor1 is top left corner
        double motor2Power = (y - x) / denominator;  //motor2 is top right corner
        double motor3Power = (y - x) / denominator;  //motor3 is bottom left corner
        double motor4Power = (y + x) / denominator;  //motor4 is bottom right corner

        while (Math.abs(yError)>20||Math.abs(xError)>20){
            xError = perp.getTargetPosition() - perp.getCurrentPosition();
            yError = parallel.getTargetPosition() - parallel.getCurrentPosition();
            denominator = Math.max(Math.abs(yError) + Math.abs(xError), 1);
            motor1Power = (yError +xError) / denominator;  //motor1 is top left corner
            motor2Power = (yError -xError) / denominator;  //motor2 is top right corner
            motor3Power = (yError -xError) / denominator;  //motor3 is bottom left corner
            motor4Power = (yError +xError) / denominator;  //motor4 is bottom right corner

            linearOpMode.telemetry.addData("y error: ",yError);
            linearOpMode.telemetry.addData("x error: ",xError);
            linearOpMode.telemetry.addData("motorPower: ",motor1.getPower());
            linearOpMode.telemetry.addData("motor1Power: ",motor1Power);
            linearOpMode.telemetry.addData("avg of mPower: ",(motor1.getPower()+motor1Power)/2);
            linearOpMode.telemetry.update();

            motor1.setPower(motor1Power/3);
            motor2.setPower(-motor2Power/3);
            motor3.setPower(motor3Power/3);
            motor4.setPower(-motor4Power/3);
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);

    }
}
