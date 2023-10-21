package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "tourqe")
public class tourqe extends LinearOpMode {
    private DcMotor motor1,motor2,motor3,motor4;
    private Servo bs;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");

        bs = hardwareMap.get(Servo.class, "bigServo");

        while (!opModeIsActive()) {
            bs.setPosition(0);
        }
        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.circle){
                bs.setPosition(-1);
            } else if (gamepad1.triangle) {
                bs.setPosition(1);

            }


        }
    }
}