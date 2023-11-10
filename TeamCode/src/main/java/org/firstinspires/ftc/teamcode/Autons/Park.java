package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Park")
public class Park extends LinearOpMode {
    DcMotor motor1,motor2,motor3,motor4, intake, slide, leftWheel, rightWheel, sideWheel;
    CRServo lock, bs, fire;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");
        leftWheel = hardwareMap.get(DcMotor.class , "frontRight");
        rightWheel = hardwareMap.get(DcMotor.class , "backLeft");
        sideWheel = hardwareMap.get(DcMotor.class , "backRight");

        waitForStart();
        forward(3);

    }
    public void forward(int distance){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(-(int)(distance*1440));
        motor2.setTargetPosition((int)(distance*1440));
        motor3.setTargetPosition(-(int)(distance*1440));
        motor4.setTargetPosition((int)(distance*1440));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor1.setPower(.25);
        motor2.setPower(0.25);
        motor3.setPower(0.25);
        motor4.setPower(0.25);

        while (motor1.isBusy())

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
}